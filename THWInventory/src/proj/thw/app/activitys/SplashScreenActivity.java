package proj.thw.app.activitys;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import proj.thw.app.R;
import proj.thw.app.database.OrmDBHelper;
import proj.thw.app.ie.CSVFile;
import proj.thw.app.ie.FileIE;
import proj.thw.app.ie.ThwCsvImporter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class SplashScreenActivity extends Activity {
	
	private TextView tvStatus;
	private OrmDBHelper dbHelper; 
	private Context context;
	private boolean importData = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		context = this;
		//init Views
		
	
		tvStatus = (TextView) findViewById(R.id.tvstatus);
		tvStatus.setVisibility(View.INVISIBLE);
		
		//init Objects
		
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	public void onClick(View v)
	{
		if(!importData)
		{
		v.setVisibility(View.INVISIBLE);
		//spLoadFile.setVisibility(View.INVISIBLE);
		tvStatus.setVisibility(View.VISIBLE);
		
		//ThwCsvImporter importer = new ThwCsvImporter(dbHelper, this, tvStatus);
		//importer.execute((CSVFile)spLoadFile.getSelectedItem());
		}
		else
		{
			Intent i = new Intent (this, EquipmentTreeViewListActivity.class);
			startActivity(i);
		}
	}
	
	 @Override
	    public boolean onCreateOptionsMenu(final Menu menu) {
	        final MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.splash_screen, menu);
	        return true;
	    }

}
