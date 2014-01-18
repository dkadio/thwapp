package proj.thw.app.activitys;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import proj.thw.app.R;
import proj.thw.app.R.layout;
import proj.thw.app.R.menu;
import proj.thw.app.database.OrmDBHelper;
import proj.thw.app.ie.CSVFile;
import proj.thw.app.ie.FileIE;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ImportDataActivity extends Activity {

	//private static final String appFolder = "//storage//"
	private static final String IMPORT_EXPORT_FILE_FOLDER 	= "IE";
	private static final String DEFAULT_FILE_NAME 			= "default";
	private static final String FILE_EXTENTION 				= ".csv";
	
	private enum FileType{CSV,XML};
	
	private Spinner spLoadFile;
	private ArrayAdapter<FileIE> adpLoadFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_import_data);
		
		spLoadFile = (Spinner) findViewById(R.id.spfilesource);
		adpLoadFile = new ArrayAdapter<FileIE>(this, android.R.layout.simple_spinner_dropdown_item);
		
		//dbHelper = new OrmDBHelper(this);
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
		for(File file : getFileList(ieFolder,FILE_EXTENTION))
		{
			try {
				adpLoadFile.add(new CSVFile(file.getAbsolutePath(),";\""));
			} catch (FileNotFoundException e) {
				Log.e(this.getClass().getName(), e.getMessage());
			} catch (UnsupportedEncodingException e) {
				Log.e(this.getClass().getName(), e.getMessage());
			}
		}
		
		spLoadFile.setAdapter(adpLoadFile);
		adpLoadFile.setNotifyOnChange(true);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.import_data, menu);
        return true;
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
