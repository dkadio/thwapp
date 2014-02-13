package proj.thw.app.activitys;

import java.io.File;

import proj.thw.app.R;
import proj.thw.app.R.layout;
import proj.thw.app.R.menu;
import proj.thw.app.database.OrmDBHelper;
import proj.thw.app.ie.ThwCsvExporter;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ExportDataActivity extends Activity {

	private EditText etFileName;
	private Button btnExport;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_export_data);
		
		etFileName = (EditText) findViewById(R.id.etfilename);
		btnExport = (Button) findViewById(R.id.btnexport);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.export_data, menu);
		return true;
	}
	
	
	public void onClickExport(View view)
	{
		String folderPath = Environment.getExternalStorageDirectory()+ File.separator 
							+ getResources().getString(R.string.app_name) + File.separator
							+ "IE" + File.separator
							+ "CSV";
		ThwCsvExporter exporter = new ThwCsvExporter(this, new OrmDBHelper(this), etFileName.getText().toString());
		exporter.execute(folderPath);
	}

}
