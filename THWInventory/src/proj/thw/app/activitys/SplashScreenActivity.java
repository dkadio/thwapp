package proj.thw.app.activitys;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;

import proj.thw.app.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashScreenActivity extends Activity {

	private static final String LOG 						= "SplashScreenActivity";
	private static final int SPLASH_TIME_OUT 				= 3000;
	private static final String IMPORT_EXPORT_FILE_FOLDER 	= "IE";
	private static final String XML_FOLDER 					= "XML";
	private static final String CSV_FOLDER 					= "CSV";
	private static final String DEFAULT_FOLDER 				= "Default";
	private static final String DEFAULT_FILE 				= "default";
	private static final String FILE_EXTENTION_CSV 			= ".csv";
	private static final String FILE_EXTENTION_XML 			= ".xml";

	private File csvFolder;
	private File xmlFolder;
	private File defaultFolder;
	
	private static Context callContext;
	
	private ProgressBar pbload;
	private TextView tvstatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		callContext = this;
		
		pbload = (ProgressBar) findViewById(R.id.pbloaddb);
		pbload.setVisibility(View.VISIBLE);
		
		tvstatus = (TextView) findViewById(R.id.tvstatus);
		tvstatus.setText("Init Folders");
		tvstatus.setVisibility(View.VISIBLE);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				writeMsg("Init Folders...");
				Thread.currentThread();
				try {
					Thread.sleep(SPLASH_TIME_OUT);
				} catch (InterruptedException e) {
					Log.e(LOG, e.getMessage());
				}
				writeMsg("Init Folders...");
				createOrCheckSysFolders();
				writeMsg("successful!");
				Intent i = new Intent(callContext,
						EquipmentTreeViewListActivity.class);
				callContext.startActivity(i);
				finish();
				
			}
		}).start();
	}
	
	private void writeMsg(final String msg)
	{
		tvstatus.post(new Runnable() {
			@Override
			public void run() {
				tvstatus.setText(msg);
			}
		});
	}

	private void createOrCheckSysFolders() {
		// pruefe, ob App-Folder existieren fuer die import/exportdateien, sonst
		// leg ihn an
		File sysFolderOnExternalStorage = new File(
				Environment.getExternalStorageDirectory(), getResources()
						.getString(R.string.app_name));

		if (!sysFolderOnExternalStorage.exists())
			sysFolderOnExternalStorage.mkdir();

		File ieFolder = new File(sysFolderOnExternalStorage,
				IMPORT_EXPORT_FILE_FOLDER);
		if (!ieFolder.exists())
			ieFolder.mkdir();

		csvFolder = new File(ieFolder, CSV_FOLDER);
		if (!csvFolder.exists())
			csvFolder.mkdir();
		
		createDefaultCSV();
		
		xmlFolder = new File(ieFolder, XML_FOLDER);
		if (!xmlFolder.exists())
			xmlFolder.mkdir();

		createDefaultXML();
	}

	private void createDefaultCSV() {

		defaultFolder = new File(csvFolder, DEFAULT_FOLDER);
		if (!defaultFolder.exists()) {
			defaultFolder.mkdir();
		}

		createDefaultFiles(defaultFolder, DEFAULT_FILE, FILE_EXTENTION_CSV,
				R.raw.thwdefault);
	}

	private void createDefaultXML() {

		defaultFolder = new File(xmlFolder, DEFAULT_FOLDER);
		if (!defaultFolder.exists()) {
			defaultFolder.mkdir();
		}

		createDefaultFiles(defaultFolder, DEFAULT_FILE, FILE_EXTENTION_XML, -1);
	}

	private void createDefaultFiles(File parentFolder, String fileName,
			String fileExtention, int resId) {

		if (resId >= 0) {
			if (getFileList(parentFolder, fileExtention).length == 0) {
				int size;
				try {
					InputStream is = getResources().openRawResource(resId);
					size = is.available();
					byte[] buffer = new byte[size];
					is.read(buffer);
					is.close();

					FileOutputStream fos = new FileOutputStream(new File(
							parentFolder, fileName + fileExtention));
					fos.write(buffer);
					fos.flush();
					fos.close();

				} catch (IOException e) {
					Log.e(this.getClass().getName(), e.getMessage());
				}
			}
		}
	}

	private File[] getFileList(File folder, String extention) {
		final String ext = extention;
		return folder.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				if (filename.toLowerCase().endsWith(ext)) {
					return true;
				} else {
					return false;
				}
			}
		});
	}

}
