package proj.thw.app.activitys;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;

import proj.thw.app.R;
import proj.thw.app.ie.THWCSVLoader;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

public class SplashScreenActivity extends Activity {

	//private static final String appFolder = "//storage//"
	private static final String IMPORT_EXPORT_FILE_FOLDER 	= "IE";
	private static final String DEFAULT_FILE_NAME 			= "default";
	private static final String FILE_EXTENTION 				= ".csv";
	
	private Spinner spLoadFile;
	private ArrayAdapter<File> adpLoadFile;
	
	private ProgressBar pbLoad;
	private TextView tvStatus;
	private Button btnGo;
	
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		context = this;
		//init Views
		spLoadFile = (Spinner) findViewById(R.id.sploadfile);
		
		tvStatus = (TextView) findViewById(R.id.tvstatus);
		tvStatus.setVisibility(View.INVISIBLE);
		
		pbLoad = (ProgressBar) findViewById(R.id.pbload);
		pbLoad.setVisibility(View.INVISIBLE);
		
		btnGo = (Button) findViewById(R.id.btngo);
		btnGo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				v.setVisibility(View.INVISIBLE);
				THWCSVLoader csvLoader = new THWCSVLoader(spLoadFile, tvStatus, pbLoad, context);
				csvLoader.execute((File)spLoadFile.getSelectedItem());
			}
		});
		
		//init Objects
		adpLoadFile = new ArrayAdapter<File>(this, android.R.layout.simple_list_item_1);
		init();
	}
	
	
	private void init()
	{
		//pruefe, ob App-Folder existieren fuer die import/exportdateien, sonst leg ihn an
		File sysFolderOnExternalStorage = new File(Environment.getExternalStorageDirectory(),
													getResources().getString(R.string.app_name));
		
		if(!sysFolderOnExternalStorage.exists())
			sysFolderOnExternalStorage.mkdir();
		
		File ieFolder = new File(sysFolderOnExternalStorage, IMPORT_EXPORT_FILE_FOLDER);
		if(!ieFolder.exists())
			ieFolder.mkdir();
		
		
		if(getFileList(ieFolder,FILE_EXTENTION).length == 0)
		{
			int size;
			try {
				InputStream is = getResources().openRawResource(R.raw.thwdefault);
				size = is.available();
				byte[] buffer = new byte[size];
				is.read(buffer);
				is.close();
				
				
				FileOutputStream fos = new FileOutputStream(new File(ieFolder,DEFAULT_FILE_NAME + FILE_EXTENTION));
				fos.write(buffer);
				fos.flush();
				fos.close();
				
			} catch (IOException e) {
				Log.e(this.getClass().getName(), e.getMessage());
			}
		}
		
		
		//add all Files to Adapter and set Adapter to Spinner
		adpLoadFile.addAll(getFileList(ieFolder,FILE_EXTENTION));
		spLoadFile.setAdapter(adpLoadFile);
		adpLoadFile.setNotifyOnChange(true);
	}
	
	private File[] getFileList(File folder, String extention)
	{
		final String ext = extention;
		return folder.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String filename) {
				if(filename.toLowerCase().endsWith(ext)){
					return true;
				}
				else{
					return false;
				}
			}
		});
	}

}
