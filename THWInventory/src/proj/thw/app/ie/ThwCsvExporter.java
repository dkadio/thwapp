package proj.thw.app.ie;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import proj.thw.app.classes.Equipment;
import proj.thw.app.database.OrmDBHelper;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class ThwCsvExporter extends AsyncTask<String,String, FileIE>{

	private ProgressDialog asyncDialog;
	private Context context;
	private String fileName;
	private OrmDBHelper dbHelper;
	
	public ThwCsvExporter(Context context,OrmDBHelper dbHelper, String fileName)
	{
		this.context = context;
		this.fileName = fileName;
		this.dbHelper = dbHelper;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		asyncDialog = new ProgressDialog(context);
		asyncDialog.setTitle("Please Wait...");
		asyncDialog.show();
		
	}
	
	@Override
	protected void onPostExecute(FileIE result) {
		super.onPostExecute(result);
		asyncDialog.dismiss();
		
		if(result != null)
		{
			
		}
		
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		
		if(values.length > 0)
		{
			asyncDialog.setMessage(values[0]);
		}
		
	}

	//TODO alle this.getclass.gename raus....
	
	@Override
	protected FileIE doInBackground(String... params) {
		int rowcount = 0;
		List<Equipment> saveList;
		FileWriter csvWriter = null;
		
		publishProgress("Read Data from DB...");
		
		
		
		
		try {
			saveList = dbHelper.getDbHelperEquip().selectAllEquipments();
			rowcount = saveList.size();
		} catch (SQLException e) {
			Log.e(this.getClass().getName(), e.getMessage());
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			return null;
		}
		
		publishProgress("Write Data to File....");
		
		try {
			csvWriter = new FileWriter(new File(""));
			//Write Header
			String header = "Ebene;";
			
			//Write first entry...
			
			//Write Datarow
			
			for(Equipment saveItem : saveList)
			{
				
			}
			
			publishProgress("Write Line ( " + "/" +rowcount );
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		finally
		{
			if(csvWriter != null)
			{
				try {
					csvWriter.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
		
		
		
		
		return null;
	}

}
