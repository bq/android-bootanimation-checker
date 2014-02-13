package com.mundoreader.bootanimationtest.activities;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mundoreader.bootanimationtest.R;
import com.mundoreader.bootanimationtest.animation.BootAnimationGeneratorTask;
import com.mundoreader.bootanimationtest.animation.BootAnimationGeneratorTask.BootAnimationGeneratorAsyncResponse;
import com.mundoreader.bootanimationtest.animation.UnzipTask;
import com.mundoreader.bootanimationtest.util.Utils;
import com.mundoreader.bootanimationtest.views.CustomAnimationDrawable;

public class AnimationActivity extends Activity implements BootAnimationGeneratorAsyncResponse {
	
	public static final String ZIP_PATH = "com.mundoreader.bootanimationtest:ZIP_PATH";
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
		
		View decorView = getWindow().getDecorView();
		int uiOptions =  View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LOW_PROFILE;
		decorView.setSystemUiVisibility(uiOptions);
		
		bootAnimationImageView = (ImageView) findViewById(R.id.img_animation);
		
		bootAnimationImageView = (ImageView) findViewById(R.id.img_animation);
		/*bootAnimationImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (bootAnimationGenerator != null) {
					bootAnimationGenerator.pushImage();
				}
			}
		});*/
		
		Intent intent = getIntent();
		
		if (intent != null) {
			String path = intent.getStringExtra(AnimationActivity.ZIP_PATH);
			if (!TextUtils.isEmpty(path)) {
				zipPath = path;
			}
		}
		
		Utils.deleteDirectory(new File(UNZIPPED_PATH));
		new UnzipTask(this).execute(zipPath, UNZIPPED_PATH);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
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
