package com.bq.bootanimationtest.activities;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bq.bootanimation.R;
import com.bq.bootanimationtest.animation.BootAnimationGeneratorTask;
import com.bq.bootanimationtest.animation.BootAnimationGeneratorTask.BootAnimationGeneratorAsyncResponse;
import com.bq.bootanimationtest.animation.UnzipTask;
import com.bq.bootanimationtest.util.Utils;
import com.bq.bootanimationtest.views.CustomAnimationDrawable;

public class AnimationActivity extends Activity implements BootAnimationGeneratorAsyncResponse {
	
	public static final String ZIP_PATH = "com.mundoreader.bootanimationtest:ZIP_PATH";
	// Path where unzipped files from bootanimation.zip will stored
	public static final String UNZIPPED_PATH = Environment.getExternalStorageDirectory() + "/BootAnimationTest/";
	
	private ImageView bootAnimationImageView;
	
	private String zipPath = "";
	
	private CustomAnimationDrawable bootAnimation;	
	
	private BootAnimationGeneratorTask bootAnimationGenerator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animation);
		
		bootAnimationGenerator = BootAnimationGeneratorTask.getBootAnimationGenerator(this, zipPath);
		
		// Hide navigation bars
		View decorView = getWindow().getDecorView();
		if (decorView != null) {
			decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LOW_PROFILE);
		}
		
		bootAnimationImageView = (ImageView) findViewById(R.id.img_animation);
				
		Intent intent = getIntent();
		
		// Get path where bootanimation.zip is stored
		if (intent != null) {
			String path = intent.getStringExtra(AnimationActivity.ZIP_PATH);
			if (!TextUtils.isEmpty(path)) {
				zipPath = path;
			}
		}
		
		// Delete previous unzipped files
		Utils.deleteDirectory(new File(UNZIPPED_PATH));
		// Unzip bootanimation.zip
		new UnzipTask(this).execute(zipPath, UNZIPPED_PATH);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		// Free memory
		bootAnimation = null;
		System.gc();
	}
	
	public void startAnimation() {
		if (bootAnimationGenerator == null) return;
		
		try {
			new BootAnimationGeneratorTask(this, zipPath).execute();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
	}
	
	// Callback called from BootAnimationGenerator when animation is built
	public void onAsyncResponse(CustomAnimationDrawable animation) {
		if (animation == null) {
			Toast.makeText(this, R.string.e_generating, Toast.LENGTH_LONG).show();
			return;
		}
		
		bootAnimation = animation;
		
		bootAnimationImageView.setBackgroundDrawable(bootAnimation);
		bootAnimation.start();
	}
}
