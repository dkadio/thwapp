package proj.thw.app.activitys;

import java.io.File;

import proj.thw.app.R;
import proj.thw.app.database.OrmDBHelper;
import proj.thw.app.ie.FileIE;
import proj.thw.app.ie.ThwCsvExporter;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Klasse, die Exportfunktionen zur Verfuegung stellt.
 * @author max / deniz
 *
 */
public class ExportDataActivity extends Activity {

	private EditText etFileName;
	private Spinner spExportFormat;
	ArrayAdapter<FileIE.FileType> adpFormatAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_export_data);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		etFileName = (EditText) findViewById(R.id.etfilename);
		spExportFormat = (Spinner) findViewById(R.id.spformat);
		adpFormatAdapter = new ArrayAdapter<FileIE.FileType>(this,
				android.R.layout.simple_spinner_dropdown_item, FileIE.FileType.values());
		spExportFormat.setAdapter(adpFormatAdapter);
		adpFormatAdapter.setNotifyOnChange(true);
	}

	/**
	 * Funktion, die aufgerufen wird wenn auf den Wxportbutton geklickt wird
	 * @param view
	 */
	public void onClickExport(View view)
	{
		switch ((FileIE.FileType) spExportFormat.getSelectedItem()) {
		case CSV:
			callCSVExporter();
			break;
		case XML:
				Toast.makeText(this, getString(R.string.error_not_available), Toast.LENGTH_LONG).show();
		break;
		default:
			break;
		}
	}

	/**
	 * Funktion, um Export als CSV zu starten
	 */
	private void callCSVExporter() {
		String folderPath = Environment.getExternalStorageDirectory()
				+ File.separator + getResources().getString(R.string.app_name)
				+ File.separator + "IE" + File.separator + "CSV";
		ThwCsvExporter exporter = new ThwCsvExporter(this,
				new OrmDBHelper(this), etFileName.getText().toString());
		exporter.execute(folderPath);
	}

}
