package proj.thw.app.activitys;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;

import proj.thw.app.R;
import proj.thw.app.database.OrmDBHelper;
import proj.thw.app.ie.CSVFile;
import proj.thw.app.ie.FileIE;
import proj.thw.app.ie.FilePackage;
import proj.thw.app.ie.ThwCsvImporter;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Klasse, die Funktionen zum Importieren von Daten als CSV-File in die Datenbank zur verfuegung stellt
 * @author max / deniz
 *
 */
public class ImportDataActivity extends Activity {

	private static final String IMAGE_FILE_NAME = "Image";
	private static final char SEPERATOR = ';';
	private OrmDBHelper dbHelper;
	private Spinner spTypeFile;
	private Spinner spLoadFile;
	private ArrayAdapter<FilePackage> adpLoadFile;
	private ArrayAdapter<FileIE.FileType> adpTypeFile;
	private CheckBox cbCleanDBbeforImport;
	private File ieFolder;
	private Handler returnHandler;

	public Handler getReturnHandler() {
		return returnHandler;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_import_data);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		returnHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				setResult(RESULT_OK);
				finish();
			};
		};

		ieFolder = new File(Environment.getExternalStorageDirectory(),
				getResources().getString(R.string.app_name) + File.separator
						+ "IE");
		spTypeFile = (Spinner) findViewById(R.id.spformat);
		adpTypeFile = new ArrayAdapter<FileIE.FileType>(this,
				android.R.layout.simple_spinner_dropdown_item,
				FileIE.FileType.values());
		spTypeFile.setAdapter(adpTypeFile);
		spTypeFile.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adp, View arg1, int arg2,
					long arg3) {

				//Funktionalitaet die aufgerufen wird, wenn ein eintrag im spinner geaendert wird
				try {
					switch ((FileIE.FileType) adp.getSelectedItem()) {
					case CSV:

						adpLoadFile.clear();
						adpLoadFile
								.addAll(loadFilePackages(new File(ieFolder,
										FileIE.FileType.CSV.toString()),
										FileIE.FILE_EXTENTION_CSV,
										FileIE.FileType.CSV));
						break;
					case XML:

						adpLoadFile.clear();
						adpLoadFile
								.addAll(loadFilePackages(new File(ieFolder,
										FileIE.FileType.XML.toString()),
										FileIE.FILE_EXTENTION_XML,
										FileIE.FileType.XML));

						break;
					default: // Do Nothing...

					}

					adpLoadFile.setNotifyOnChange(true);
				} catch (FileNotFoundException e) {
					Log.e("", e.getMessage());
				} catch (UnsupportedEncodingException e) {
					Log.e("", e.getMessage());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		spLoadFile = (Spinner) findViewById(R.id.spfilesource);
		adpLoadFile = new ArrayAdapter<FilePackage>(this,
				android.R.layout.simple_spinner_dropdown_item);
		spLoadFile.setAdapter(adpLoadFile);

		cbCleanDBbeforImport = (CheckBox) findViewById(R.id.cbcleardb);
		dbHelper = new OrmDBHelper(this);
		// init();
	}

	/**
	 * Funktion, die aufgerufen wird, wenn auf den Import Button geklickt wird
	 * @param view
	 */
	public void onClickImport(View view) {
		if (((FilePackage) spLoadFile.getSelectedItem()).getDataFile() != null) {
			if (cbCleanDBbeforImport.isChecked()) {
				try {
					dbHelper.clearDB();
				} catch (SQLException e) {
					Log.e(this.getClass().getName(), e.getMessage());
				}
			}

			ThwCsvImporter thwImporter = new ThwCsvImporter(dbHelper, this);
			thwImporter.execute((FilePackage) spLoadFile.getSelectedItem());
		} else {
			Log.e(this.getClass().getName(), getString(R.string.error_no_File_selected));
			Toast.makeText(this, getString(R.string.error_no_File_selected), Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Funktion die einen Ordner, in dem sich bestimmte Dateien befinden, zu einem FilePackage-Objekt initialisiert
	 * @param folder		Ordner mit bestimmten Dateien
	 * @param extention		welches Dateiformat
	 * @param ft			Welche Datei-Typen
	 * @return				gibt ein geladenes FilePackage zurueck
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private FilePackage folderToFilePackage(File folder, String extention,
			FileIE.FileType ft) throws FileNotFoundException,
			UnsupportedEncodingException {
		FilePackage newFilePackage = new FilePackage();
		if (folder.listFiles().length > 0) {
			for (File currentFile : folder.listFiles()) {
				if (currentFile.getName().equals(IMAGE_FILE_NAME + extention)) {
					switch (ft) {
					case CSV:
						newFilePackage.setImageFile(new CSVFile(currentFile,
								SEPERATOR));
						break;
					case XML:
						// hier kann XML implementiert werden....
						break;
					default: // Do Nothing...
					}
				}

				if (currentFile.getName().toUpperCase()
						.equals((folder.getName() + extention).toUpperCase())) {
					switch (ft) {
					case CSV:
						newFilePackage.setDataFile(new CSVFile(currentFile,
								SEPERATOR));
						break;
					case XML:
						// hier kann XML implementiert werden....
						break;
					default: // Do Nothing...
					}
				}
			}
		}

		return newFilePackage;
	}

	/**
	 * Klasse, die FilePackages ( daten.csv und image.csv) laed und zur verfuegung stellt
	 * @param parentFolder		uebergeordneter Ordner
	 * @param extension			welches Dateiformat
	 * @param ft 				filetype
	 * @return					Liste mit allenm Packages
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private ArrayList<FilePackage> loadFilePackages(File parentFolder,
			String extension, FileIE.FileType ft) throws FileNotFoundException,
			UnsupportedEncodingException {

		ArrayList<FilePackage> packageList = new ArrayList<FilePackage>();
		for (File currentFile : parentFolder.listFiles()) {
			if (currentFile.isDirectory()) {
				packageList
						.add(folderToFilePackage(currentFile, extension, ft));
			}
		}
		return packageList;
	}
}
