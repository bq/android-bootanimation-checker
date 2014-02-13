package com.mundoreader.bootanimationtest.parser;

import java.util.ArrayList;
import java.util.List;

import com.mundoreader.bootanimationtest.animation.AnimationPart;

public class DescModel {
	
	private int animationWidth;
	private int animationHeight;
	private int animationFps;
	private int animationPartsCount;
	private List<AnimationPart> animationParts;
	
	public DescModel() {
		animationParts = new ArrayList<AnimationPart>();
	}
	
	public DescModel(int animationWidth, int animationHeight, int animationFps) {
		this.animationWidth = animationWidth;
		this.animationHeight = animationHeight;
		this.animationFps = animationFps;
	}
	
	public int getAnimationWidth() {
		return animationWidth;
	}
	
	public void setAnimationWidth(int animationWidth) {
		this.animationWidth = animationWidth;
	}
	
	public int getAnimationHeight() {
		return animationHeight;
	}
	
	public void setAnimationHeight(int animationHeight) {
		this.animationHeight = animationHeight;
	}
	
	public int getAnimationFps() {
		return animationFps;
	}
	
	public void setAnimationFps(int animationFps) {
		this.animationFps = animationFps;
	}
	
	public int getAnimationPartsCount() {
		return animationPartsCount;
	}
	
	public void setAnimationPartsCount(int animationPartsCount) {
		this.animationPartsCount = animationPartsCount;
	}
	
	public List<AnimationPart> getAnimationParts() {
		return animationParts;
	}
	
	public void setAnimationParts(List<AnimationPart> animationParts) {
		this.animationParts = animationParts;
		this.animationPartsCount = (animationParts != null) ? animationParts.size() : 0;
	}
}
