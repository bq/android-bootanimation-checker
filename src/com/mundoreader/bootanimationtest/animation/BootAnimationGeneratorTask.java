package com.mundoreader.bootanimationtest.animation;

import java.io.File;
import java.util.Arrays;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.mundoreader.bootanimationtest.R;
import com.mundoreader.bootanimationtest.activities.AnimationActivity;
import com.mundoreader.bootanimationtest.parser.DescModel;
import com.mundoreader.bootanimationtest.parser.DescParser;
import com.mundoreader.bootanimationtest.util.Utils;
import com.mundoreader.bootanimationtest.views.CustomAnimationDrawable;

public class BootAnimationGeneratorTask extends AsyncTask<String, Void, Boolean>{
	
	public interface BootAnimationGeneratorAsyncResponse {
		void onAsyncResponse(CustomAnimationDrawable animation);
	}
		
	private static BootAnimationGeneratorTask bootAnimationGenerator;
	    
	private AnimationActivity activity;
	
    private DescParser descParser;
    private DescModel descModel;
    private CustomAnimationDrawable bootAnimation;
        
	private ProgressDialog dialog;
    
    public BootAnimationGeneratorTask(AnimationActivity activity, String zipPath) {
    	this.activity = activity;
		
        descParser = new DescParser();
        
        bootAnimation = new CustomAnimationDrawable(){
            @Override
            public void onAnimationFinish() {
            		bootAnimation.stop();
                    bootAnimation.start();
            }
        };
        
        dialog = new ProgressDialog(activity);
    }
    
    public static BootAnimationGeneratorTask getBootAnimationGenerator(AnimationActivity context, String zipPath) {
    	if (bootAnimationGenerator == null) {
    		bootAnimationGenerator = new BootAnimationGeneratorTask(context, zipPath);
    	}

        return bootAnimationGenerator;
    }
    
    @Override
    protected void onPreExecute() {
    	if (dialog == null) return;
    	
    	dialog.setMessage(activity.getString(R.string.generating_animation));
		dialog.show();
    }
    
    @Override
    protected Boolean doInBackground(String... params) {
    	return (buildBootAnimation() != null) ? true : false;
    }
    
    @Override
    protected void onPostExecute(final Boolean success) {
    	if ((dialog != null) && (dialog.isShowing())) {
            dialog.dismiss();
        }
    	
    	if (activity != null) {
    		if (success) {
        		activity.onAsyncResponse(bootAnimation);
        	} else {
        		activity.onAsyncResponse(null);
        	}
    	}
    }

    @SuppressWarnings("unused")
	private CustomAnimationDrawable buildBootAnimation() {
    	if (descParser == null) return null;
    	
        descModel = descParser.getDescModel(new File(AnimationActivity.UNZIPPED_PATH+ "/desc.txt"));

        if (descModel == null) return null;
        
        int animationDuration = getAnimationDuration(descModel.getAnimationFps());
        
        boolean success = false;
        int sampleSize = Utils.IMAGE_NORMAL_SIZE;
        
    	buildAnimationDrawable();
        
    	/*
    	 * Because heap consumption problems, bitmaps are created inside a loop
    	 * increasing it's sample size to reduce bitmap resolution until all images are
    	 * stored with success
    	 */
    	while (!success) {
    		// Iterate over all animation parts of bootanimation folder
        	for (int i = 0; i < descModel.getAnimationPartsCount(); i++) {
        		AnimationPart animationPart = descModel.getAnimationParts().get(i);
        		
        		File animationFolder = new File(AnimationActivity.UNZIPPED_PATH + animationPart.getFolder());
            	
            	if (animationFolder == null) continue;
            	
            	File[] files = animationFolder.listFiles();
            	
            	// Build animation for current animation part
            	success = buildDrawableAnimation(files, animationDuration, sampleSize);
            	if (!success) break;
        	}
        	
        	// If heap problems, increase sample size and built bitmaps again
        	if (!success) {
        		bootAnimation = null;
        		buildAnimationDrawable();
        		sampleSize++;
        	}
    	}
        
    	return bootAnimation;
    }
    
    // Calculate animation duration from provided fps readed from desc model
    public int getAnimationDuration(int animationFps) {
    	return ((int) 1000 / animationFps);
    }
    
    public DescModel getDescModel() {
		return descModel;
	}

	public void setDescModel(DescModel descModel) {
		this.descModel = descModel;
	}
	
	public AnimationDrawable getBootAnimation() {
		return bootAnimation;
	}

	public void setBootAnimation(CustomAnimationDrawable bootAnimation) {
		this.bootAnimation = bootAnimation;
	}
	
	// Build animation from provided images
	private boolean buildDrawableAnimation(File[] files, int animationDuration, int sampleSize) {
		if ((files == null) || (bootAnimation == null)) return false;
		
		// Sort images array to build animation bitmaps with the correct order
		Arrays.sort(files);
    	try {
    		
    		int i;
    		for(i = 0; i < files.length; i++) {
        		BitmapDrawable animation = createDrawable(files[i], sampleSize);
    			bootAnimation.addFrame(animation, animationDuration);
        	}
    	} catch (OutOfMemoryError e) {
    		e.printStackTrace();
    		return false;
    	}
    	    	
    	return true;
	}
	
	// Build drawable with some configuration options
	private BitmapDrawable createDrawable(File file, int sampleSize) throws OutOfMemoryError {
		if ((activity == null) || (file == null) || (!file.exists())) return null;
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		
		options.inSampleSize = sampleSize;
	    options.inPurgeable = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
	    options.inTempStorage = new byte[32768];
		
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		
		return new BitmapDrawable(activity.getResources(), bitmap);
	}
	
	private void buildAnimationDrawable() {
		bootAnimation = new CustomAnimationDrawable(){
            @Override
            public void onAnimationFinish() {
            		bootAnimation.stop();
                    bootAnimation.start();
            }
        };
	}
}
