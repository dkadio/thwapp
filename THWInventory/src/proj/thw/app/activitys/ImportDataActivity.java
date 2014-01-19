package proj.thw.app.activitys;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import proj.thw.app.R;
import proj.thw.app.database.OrmDBHelper;
import proj.thw.app.ie.CSVFile;
import proj.thw.app.ie.FileIE;
import proj.thw.app.ie.ThwCsvImporter;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.TabActivity;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;

public class ImportDataActivity extends FragmentActivity {

	//private static final String appFolder = "//storage//"
	private static final String DEFAULT_FILE_NAME 			= "default";
	private static final String FILE_EXTENTION_CSV 			= ".csv";
	private static final String FILE_EXTENTION_XML 			= ".xml";
	
	private enum FileType{CSV,XML};
	private OrmDBHelper dbHelper; 
	
	private Spinner spTypeFile;
	private Spinner spLoadFile;
	private ArrayAdapter<FileIE> adpLoadFile;
	private ArrayAdapter<FileType> adpTypeFile;

	private CheckBox cbCleanDBbeforImport;
	private TabHost tabImport;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_import_data);
		
	//	tabImport = (TabHost) findViewById();
		//tabImport.setup();
		
		spTypeFile = (Spinner) findViewById(R.id.spformat);
		adpTypeFile = new ArrayAdapter<FileType>(this, android.R.layout.simple_spinner_dropdown_item,FileType.values());
		spTypeFile.setAdapter(adpTypeFile);
		
		spLoadFile = (Spinner) findViewById(R.id.spfilesource);
		adpLoadFile = new ArrayAdapter<FileIE>(this, android.R.layout.simple_spinner_dropdown_item);
		spLoadFile.setAdapter(adpLoadFile);
		
		cbCleanDBbeforImport = (CheckBox)findViewById(R.id.cbcleardb);
		dbHelper = new OrmDBHelper(this);
		//init();
	}
	

	/*
	private void init()
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
	}	
	*/
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
		thwImporter.execute((CSVFile)spLoadFile.getSelectedItem());
		
	}
	
	//--------------------------MENUE-HANDLING---------------------------
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.import_data, menu);
        return true;
    }
	
	
}
