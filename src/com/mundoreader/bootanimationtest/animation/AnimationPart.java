package com.mundoreader.bootanimationtest.animation;

public class AnimationPart {
	
	private int loops;
	
	private int pause;
	
	private String folder;
	
	public AnimationPart() {}
	
	public AnimationPart(int loops, int pause, String folder) {
		this.loops = loops;
		this.pause = pause;
		this.folder = folder;
	}
	
	public int getLoops() {
		return loops;
	}

	public void setLoops(int loops) {
		this.loops = loops;
	}

	public int getPause() {
		return pause;
	}

	public void setPause(int pause) {
		this.pause = pause;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}
}
