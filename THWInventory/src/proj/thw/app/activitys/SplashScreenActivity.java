package proj.thw.app.activitys;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import proj.thw.app.R;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;

public class SplashScreenActivity extends Activity {

	
	//private static final String appFolder = "//storage//"
	private static final String IMPORT_EXPORT_FILE_FOLDER 	= "IE";
	private static final String DEFAULT_FILE 				= "default.csv";
	
	
	private Spinner spLoadFile;
	private ArrayAdapter<String> adpLoadFile;
	
	private static final int SPLASH_TIME_OUT = 3000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
	
		//init Views
		spLoadFile = (Spinner) findViewById(R.id.sploadfile);
		
		//init Objects
		adpLoadFile = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		
		ContextWrapper c = new ContextWrapper(this);
		String strpath = c.getFilesDir().getAbsolutePath();
		
		String s = strpath; 
		//pruefe, ob App-Folder existieren fuer die import/exportdateien, sonst leg ihn an
		File ieFolder = getDir(IMPORT_EXPORT_FILE_FOLDER,MODE_PRIVATE);
		
		//pruefe, ob dateien vorhanden sind, ansonsten leg init-Datei an
		if(fileList().length == 0)
		{
			/*
			try {
				
				new InputStreamReader(getResources().openRawResource(R.raw.test));
				
			
				FileOutputStream fos = openFileOutput(DEFAULT_FILE, MODE_PRIVATE);
				fos.write(fos.);
				
			} catch (FileNotFoundException e) {
				Log.e(this.getClass().getName(), e.getMessage());
			}
			*/
		}
		
		//add all Files to Adapter and set Adapter to Spinner
		adpLoadFile.addAll(fileList());
		spLoadFile.setAdapter(adpLoadFile);
		
		adpLoadFile.setNotifyOnChange(true);
		
		/*
		 	new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, EquipmentTreeViewListActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
		 */
	
	}

}
