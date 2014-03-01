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

/**
 * Klasse, die zum Anzeigen von schönen Bildern dient und neben bei noch die Ordnerstruktur prueft ung ggfs. 
 * neu erstellt
 * @author max / deniz
 *
 */
public class SplashScreenActivity extends Activity {

	protected static final String LOG = "SplashScreenActivity";
	protected static final int SPLASH_TIME_OUT = 3000;
	protected static final String IMPORT_EXPORT_FILE_FOLDER = "IE";
	protected static final String FOLDER_TEMP = "Temp";
	protected static final String XML_FOLDER = "XML";
	protected static final String CSV_FOLDER = "CSV";
	protected static final String DEFAULT_FOLDER = "Default";
	protected static final String DEFAULT_FILE = "default";
	protected static final String FILE_EXTENTION_CSV = ".csv";
	protected static final String FILE_EXTENTION_XML = ".xml";

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
				writeMsg(getResources().getString(R.string.init_folders));
				Thread.currentThread();
				try {
					Thread.sleep(SPLASH_TIME_OUT);
				} catch (InterruptedException e) {
					Log.e(LOG, e.getMessage());
				}
				writeMsg(getResources().getString(R.string.init_folders));
				createOrCheckSysFolders();
				writeMsg(getResources().getString(R.string.suc));
				Intent i = new Intent(callContext,
						EquipmentTreeViewListActivity.class);
				callContext.startActivity(i);
				finish();

			}
		}).start();
	}

	/**
	 * Funktion, um eine Nachricht auf den Bildschirm anzuzeigen
	 * @param msg Nachricht die angezeigt werden soll
	 */
	private void writeMsg(final String msg) {
		tvstatus.post(new Runnable() {
			@Override
			public void run() {
				tvstatus.setText(msg);
			}
		});
	}

	/**
	 * Funktion, die die Ordnerhierarchie ueberprueft und ggfs. erstellt
	 */
	private void createOrCheckSysFolders() {
		// pruefe, ob App-Folder existieren fuer die import/exportdateien, sonst
		// leg ihn an
		File sysFolderOnExternalStorage = new File(
				Environment.getExternalStorageDirectory(), getResources()
						.getString(R.string.app_name));

		if (!sysFolderOnExternalStorage.exists())
			sysFolderOnExternalStorage.mkdir();

		File tempFolder = new File(sysFolderOnExternalStorage,
				FOLDER_TEMP);
		if (!tempFolder.exists())
			tempFolder.mkdir();

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

	/**
	 * Funktion, die die Standard-CSV Datei erstellt
	 */
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

	/**
	 * Funktion, die Standard Datein zum Importieren erstellt
	 * @param parentFolder		Uebergeordneter Ordner
	 * @param fileName			DateiName
	 * @param fileExtention		Dateityp
	 * @param resId				
	 */
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

	/**
	 * Funktion, die alle Dateien mit einer bestimmten Dateityp zurueckgibt
	 * @param folder		Ordner in dem gesucht werden soll
	 * @param extention		Dateityp
	 * @return				Array mit allen Datein
	 */
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
