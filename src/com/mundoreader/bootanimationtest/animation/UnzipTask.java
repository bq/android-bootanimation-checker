package com.mundoreader.bootanimationtest.animation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;

import com.mundoreader.bootanimationtest.R;
import com.mundoreader.bootanimationtest.R.string;
import com.mundoreader.bootanimationtest.activities.AnimationActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class UnzipTask extends AsyncTask<String, Void, Boolean> {
	
	private AnimationActivity activity;
	private ProgressDialog dialog;
	
	public UnzipTask(AnimationActivity activity) {
		this.activity = activity;
        dialog = new ProgressDialog(activity);
	}
	
	@Override
	protected void onPreExecute() {
		dialog.setMessage(activity.getString(R.string.unzipping_message));
		dialog.show();
	}

    @Override
    protected Boolean doInBackground(String... params) {
        String filePath = params[0];
        String destinationPath = params[1];

        File archive = new File(filePath);
        try {
        	ZipFile zipfile = new ZipFile(archive);
            for (Enumeration e = zipfile.entries(); e.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                unzipEntry(zipfile, entry, destinationPath);
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }
    
    @Override
    protected void onPostExecute(final Boolean success) {
    	if ((dialog != null) && ((dialog.isShowing()))) {
            dialog.dismiss();
        }
    	
    	if ((activity != null) && (success)) {
    		activity.startAnimation();
    	} else {
    		Toast.makeText(activity, R.string.e_unzipping, Toast.LENGTH_LONG).show();
    	}
    }

    private void unzipEntry(ZipFile zipfile, ZipEntry entry, String outputDir) throws IOException {
        if ((entry != null) && (entry.isDirectory())) {
            createDir(new File(outputDir, entry.getName()));
            return;
        }

        File outputFile = new File(outputDir, entry.getName());
        if ((outputFile != null) && (!outputFile.getParentFile().exists())) {
            createDir(outputFile.getParentFile());
        }

        BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

        try {
            IOUtils.copy(inputStream, outputStream);
        } finally {
        	if ((outputStream != null) && (inputStream != null)) {
        	    outputStream.close();
                inputStream.close();
        	}
        }
    }

    private void createDir(File dir) {
    	if (dir == null) return;
    	
        if (dir.exists()) return;
        if (!dir.mkdirs()) {
        	throw new RuntimeException("Error creating folder");
        }
    }
}
