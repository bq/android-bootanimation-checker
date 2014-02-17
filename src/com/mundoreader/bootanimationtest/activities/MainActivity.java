package com.mundoreader.bootanimationtest.activities;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.coderplus.filepicker.FilePickerActivity;
import com.mundoreader.bootanimationtest.R;

public class MainActivity extends Activity {
	
	private final String TAG = "MainActivity";
	
	public static final int FOLDER_CHOOSER_REQUEST_CODE = 1;
	
	private Button selectFileButton;
	
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		context = this;
		
		selectFileButton = (Button) findViewById(R.id.btn_select_animation);
		selectFileButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(context, FilePickerActivity.class);
				intent.putExtra(FilePickerActivity.EXTRA_FILE_PATH, Environment.getExternalStorageDirectory().getAbsolutePath());
				startActivityForResult(intent, FOLDER_CHOOSER_REQUEST_CODE);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK) {
            switch(requestCode) {
            case FOLDER_CHOOSER_REQUEST_CODE:
            	if(data.hasExtra(FilePickerActivity.EXTRA_FILE_PATH)) {
                    List<File> files = (List<File>) data.getSerializableExtra(FilePickerActivity.EXTRA_FILE_PATH);                   

                    // Get selected path to uncompress bootanimation package
                    if (files != null) {
                    	File file = files.get(0);
                    	if (file != null) {
                    		Intent intent = new Intent(context, AnimationActivity.class);
            				intent.putExtra(AnimationActivity.ZIP_PATH, file.getAbsolutePath());
            				startActivity(intent);
                    	}
                    }
            	}
            }
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                Intent intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

