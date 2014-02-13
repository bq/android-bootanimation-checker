package com.mundoreader.bootanimationtest.util;

import java.io.File;

public class Utils {
	
	public static final int IMAGE_NORMAL_SIZE = 1;
	
	public static boolean deleteDirectory(File path) {
	    if(path.exists()) {
	      File[] files = path.listFiles();
	      
	      if (files == null) return true;
	      
	      for(int i = 0; i < files.length; i++) {
	         if(files[i].isDirectory()) {
	           deleteDirectory(files[i]);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    
	    return path.delete();
	}
}
