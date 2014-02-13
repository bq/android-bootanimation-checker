package com.mundoreader.bootanimationtest.animation;

import java.io.File;
import java.util.Arrays;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
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
    
    private boolean imagePushed = false;
    
	private ProgressDialog dialog;
    
    public BootAnimationGeneratorTask(AnimationActivity activity, String zipPath) {
    	this.activity = activity;
		
        descParser = new DescParser();
        
        bootAnimation = new CustomAnimationDrawable(){
            @Override
            public void onAnimationFinish() {
            	if (!imagePushed) {
            		bootAnimation.stop();
                    bootAnimation.start();
            	} else {
            		bootAnimation.stop();
            		imagePushed = false;
            	}
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
    	dialog.setMessage(activity.getString(R.string.generating_animation));
		dialog.show();
    }
    
    @Override
    protected Boolean doInBackground(String... params) {
    	return (buildBootAnimation() != null) ? true : false;
    }
    
    @Override
    protected void onPostExecute(final Boolean success) {
    	if (dialog.isShowing()) {
            dialog.dismiss();
        }
    	
    	if (success) {
    		activity.onAsyncResponse(bootAnimation);
    	} else {
    		activity.onAsyncResponse(null);
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
        
        while (!success) {
        	for (int i = 0; i < descModel.getAnimationPartsCount(); i++) {
        		AnimationPart animationPart = descModel.getAnimationParts().get(i);
        		
        		File animationFolder = new File(AnimationActivity.UNZIPPED_PATH + animationPart.getFolder());
            	
            	if (animationFolder == null) continue;
            	
            	File[] files = animationFolder.listFiles();
            	
            	success = buildDrawableAnimation(files, animationDuration, sampleSize);
            	if (!success) break;
        	}
        	
        	if (!success) {
        		bootAnimation = null;
        		buildAnimationDrawable();
        		sampleSize++;
        	}
        }
        
    	return bootAnimation;
    }
    
    public int getAnimationDuration(int animationFps) {
    	return ((int) 1000 / animationFps);
    }
    
    public void pushImage() {
    	imagePushed = !imagePushed;
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
	
	private boolean buildDrawableAnimation(File[] files, int animationDuration, int sampleSize) {
		if ((files == null) || (bootAnimation == null)) return false;
		
		Arrays.sort(files);
    	try {
    		
    		int i;
    		for(i = 0; i < files.length; i++) {
        		BitmapDrawable animation = createDrawable(files[i], sampleSize);
        		//Drawable animation = Drawable.createFromPath(files[i].getAbsolutePath());
        		bootAnimation.addFrame(animation.getCurrent(), animationDuration);
        	}
    	} catch (OutOfMemoryError e) {
    		e.printStackTrace();
    		return false;
    	}
    	    	
    	return true;
	}
	
	private BitmapDrawable createDrawable(File file, int sampleSize) throws OutOfMemoryError {
		if ((activity == null) || (file == null) || (!file.exists())) return null;
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		
		options.inSampleSize = sampleSize;
	    //options.inPurgeable = true;
	    //options.inDither = true;
	    options.inTempStorage = new byte[32768];
		
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		
		return new BitmapDrawable(activity.getResources(), bitmap);
	}
	
	private void buildAnimationDrawable() {
		bootAnimation = new CustomAnimationDrawable(){
            @Override
            public void onAnimationFinish() {
            	if (!imagePushed) {
            		bootAnimation.stop();
                    bootAnimation.start();
            	} else {
            		bootAnimation.stop();
            		imagePushed = false;
            	}
            }
        };
	}
}
