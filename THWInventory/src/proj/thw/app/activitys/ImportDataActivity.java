package proj.thw.app.activitys;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import proj.thw.app.R;
import proj.thw.app.database.OrmDBHelper;
import proj.thw.app.ie.CSVFile;
import proj.thw.app.ie.FileIE;
import proj.thw.app.ie.FilePackage;
import proj.thw.app.ie.ThwCsvImporter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.app.TabActivity;
import android.database.CursorJoiner.Result;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;

public class ImportDataActivity extends Activity {

	private static final String IMAGE_FILE_NAME 			= "EquipmentImage";
	private static final String DEFAULT_FILE_NAME 			= "default";
	private static final String FILE_EXTENTION_CSV 			= ".csv";
	private static final String FILE_EXTENTION_XML 			= ".xml";
	
	
	private enum FileType{CSV,XML};
	private OrmDBHelper dbHelper; 
	
	private Spinner spTypeFile;
	private Spinner spLoadFile;
	private ArrayAdapter<FilePackage> adpLoadFile;
	private ArrayAdapter<FileType> adpTypeFile;

	private CheckBox cbCleanDBbeforImport;
	private TabHost tabImport;
	
	private File ieFolder;
	
	private Handler returnHandler;
	
	public Handler getReturnHandler() {
		return returnHandler;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_import_data);
		
		returnHandler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				setResult(RESULT_OK);
				finish();
			};
		};
		
		ieFolder = new File(Environment.getExternalStorageDirectory(),
										getResources().getString(R.string.app_name) + File.separator + "IE");
	//	tabImport = (TabHost) findViewById();
		//tabImport.setup();
		
		spTypeFile = (Spinner) findViewById(R.id.spformat);
		adpTypeFile = new ArrayAdapter<FileType>(this, android.R.layout.simple_spinner_dropdown_item,FileType.values());
		spTypeFile.setAdapter(adpTypeFile);
		spTypeFile.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adp, View arg1,
					int arg2, long arg3) {
				switch((FileType)adp.getSelectedItem())
				{
				case CSV:
					try {
						adpLoadFile.clear();
						adpLoadFile.addAll(loadFilePackages(new File(ieFolder, "CSV"),FILE_EXTENTION_CSV , FileType.CSV));
					} catch (FileNotFoundException e) {
						Log.e("", e.getMessage());
					} catch (UnsupportedEncodingException e) {
						Log.e("", e.getMessage());
					}
					break;
				case XML: 
					break;
				default: // Do Nothing...
				
				}
				
				adpLoadFile.setNotifyOnChange(true);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		spLoadFile = (Spinner) findViewById(R.id.spfilesource);
		adpLoadFile = new ArrayAdapter<FilePackage>(this, android.R.layout.simple_spinner_dropdown_item);
		spLoadFile.setAdapter(adpLoadFile);
		
		cbCleanDBbeforImport = (CheckBox)findViewById(R.id.cbcleardb);
		dbHelper = new OrmDBHelper(this);
		//init();
	}
	
	
	/*private void init()
	{
			
		//add all Files to Adapter and set Adapter to Spinner
		for(File file : getFileList(ieFolder,FILE_EXTENTION_CSV))
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
	}	*/
	
	public void onClickImport(View view)
	{
		if(cbCleanDBbeforImport.isChecked())
		{
			try {
				dbHelper.clearDB();
			} catch (SQLException e) {
				Log.e(this.getClass().getName(), e.getMessage());
			}
		}

		ThwCsvImporter thwImporter = new ThwCsvImporter(dbHelper,this);
		thwImporter.execute((FilePackage)spLoadFile.getSelectedItem());
		
	}
	
	//--------------------------MENUE-HANDLING---------------------------
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.import_data, menu);
        return true;
    }
	
	private FilePackage folderToFilePackage(File folder, String extention,FileType ft ) throws FileNotFoundException, UnsupportedEncodingException
	{
		FilePackage newFilePackage = new FilePackage();
		
		for(File currentFile : folder.listFiles())
		{
			if(currentFile.getName().equals(IMAGE_FILE_NAME + extention))
			{
				switch(ft){
				case CSV:
					newFilePackage.setImageFile(new CSVFile(currentFile, "\";"));
					break;
				case XML:
					break;
				default: // Do Nothing...
				}
			}
			
			if(currentFile.getName().toUpperCase().equals((folder.getName() + extention).toUpperCase()))
			{
				switch(ft){
				case CSV:
					newFilePackage.setDataFile(new CSVFile(currentFile, ";"));
					break;
				case XML:
					break;
				default: // Do Nothing...
				}
			}
		}
		return newFilePackage;
	}
	
	private ArrayList<FilePackage> loadFilePackages(File parentFolder, String extension, FileType ft ) throws FileNotFoundException, UnsupportedEncodingException
	{
		
		ArrayList<FilePackage> packageList = new ArrayList<FilePackage>();
		for(File currentFile : parentFolder.listFiles())
		{
			if(currentFile.isDirectory())
			{
				packageList.add(folderToFilePackage(currentFile, extension, ft));
			}
		}
		return packageList;
	}
	
	
}
