package com.mundoreader.bootanimationtest.views;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;

public class CustomAnimationDrawable extends AnimationDrawable {

	private Handler mAnimationHandler;

	@Override
	public void start() {
		super.start();
    
		mAnimationHandler = new Handler();
		mAnimationHandler.postDelayed(new Runnable() {

			public void run() {
				onAnimationFinish();
			}
		}, getTotalDuration());
	}

	public int getTotalDuration() {

		int iDuration = 0;

		for (int i = 0; i < this.getNumberOfFrames(); i++) {
			iDuration += this.getDuration(i);
		}

		return iDuration;
	}

	public void onAnimationFinish() {}
}
