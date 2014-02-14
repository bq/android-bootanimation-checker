package com.mundoreader.bootanimationtest.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.mundoreader.bootanimationtest.animation.AnimationPart;

import android.text.TextUtils;

public class DescParser {
	
	private List<AnimationPart> animationParts;
	
	public DescParser() {		
		animationParts = new ArrayList<AnimationPart>();
	}
	
	public DescModel getDescModel(File file) {
		if ((file == null) || (!file.exists())) return null;
		
		return parse(file.getPath());
	}
	
	private DescModel parse(String file) {
		if (TextUtils.isEmpty(file)) return null;
		
		DescModel descModel = new DescModel();

		BufferedReader bufferedReader = null;
	    try {
			bufferedReader = new BufferedReader(new FileReader(file));

	        String line = bufferedReader.readLine();
	        String[] parts = line.split(" ");
	        
	        descModel.setAnimationWidth(Integer.parseInt(parts[0]));
	        descModel.setAnimationHeight(Integer.parseInt(parts[1]));
	        descModel.setAnimationFps(Integer.parseInt(parts[2]));
	        
	        line = bufferedReader.readLine();

	        while (line != null) {
	        	parts = line.split(" ");
	        	
	        	AnimationPart animationPart = new AnimationPart(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3]);

	        	animationParts.add(animationPart);
	            
	            line = bufferedReader.readLine();
	        }
	        
	        bufferedReader.close();
	    } catch (Exception e) {
	    	
	    }
	    
	    descModel.setAnimationParts(animationParts);
	    
	    return descModel;
	}

}