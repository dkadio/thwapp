package proj.thw.app.ie;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.csvreader.CsvWriter;

import proj.thw.app.classes.Equipment;
import proj.thw.app.database.OrmDBHelper;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class ThwCsvExporter extends AsyncTask<String,String, FileIE>{

	static final String COLUMN_LAYER = "Ebene";
	static final String COLUMN_OE = "OE";
	static final String COLUMN_TYPE = "Art";
	static final String COLUMN_FB = "FB";
	static final String COLUMN_QUANTITY = "Menge";
	static final String COLUMN_STOCK = "Verfügbar";
	static final String COLUMN_ACTUALQUANTITY = "Menge Ist";
	static final String COLUMN_DESCRIPTION = "Ausstattung | Hersteller | Typ";
	static final String COLUMN_EQUIP_NO = "Sachnummer";
	static final String COLUMN_INV_NO = "Inventar Nr";
	static final String COLUMN_DEVICE_NO = "Gerätenr.";
	static final String COLUMN_STATUS = "Status";
	static final String COLUMN_IMAGE = "Image";
	
	static final String COLUMN_ID = "Id";
	static final String COLUMN_STREAM = "Stream";
	
	private static final String FILE_TYPE = ".csv";
	
	private ProgressDialog asyncDialog;
	private Context context;
	private String fileName;
	private OrmDBHelper dbHelper;
	private CsvWriter csvWriterData;
	private CsvWriter csvWriterImage;
	
	private static final char SEPERATOR = ';'; 
	
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
		
		try { 
			if(params.length == 0)
				return null;
			
			String folderPath = params[0];
			publishProgress("erstelle Ordner...");
			File newFolder = new File(folderPath + File.separator + fileName);
			if(newFolder.exists())
			{
				SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMddHHmmss "); 
				fileName += formatter.format(new Date());
				newFolder = new File(folderPath + File.separator + fileName);
			}
			newFolder.mkdir();
			
			csvWriterData = new CsvWriter(newFolder.getAbsolutePath() + File.separator + fileName + FILE_TYPE);
			csvWriterData.setDelimiter(SEPERATOR);
			
			csvWriterImage = new CsvWriter(newFolder.getAbsolutePath() + File.separator + COLUMN_IMAGE + FILE_TYPE);
			csvWriterImage.setDelimiter(SEPERATOR);
			
			publishProgress("Read Data from DB...");
			Equipment rootItem = dbHelper.getDbHelperEquip().selectEquipment("layer", 0);
			
			publishProgress("Write Data to File....");
			writeHeader();
			writeNextLine(rootItem);
			
		} catch (SQLException e) {
			Log.e(this.getClass().getName(), e.getMessage());
		} catch (IOException e) {
			Log.e(this.getClass().getName(), e.getMessage());
		}
		finally
		{
			if(csvWriterData != null)
				csvWriterData.close();
			
			if(csvWriterImage != null)
				csvWriterImage.close();
		}
		return null;
	}
	
	
	private void writeNextLine(Equipment parent) throws IOException
	{
		writeLine(parent);
		Iterator<Equipment> iterator = parent.getChilds().iterator();
		while (iterator.hasNext()) {
			Equipment element = iterator.next();
			writeNextLine(element);
		}
		 
	}
	
	private void writeLine(Equipment saveItem) throws IOException
	{
		publishProgress("write Item: " + saveItem.getEquipNo());
		String strLayer 			= String.valueOf(saveItem.getLayer());
		String strLoc 				= saveItem.getLocation();
		String strType			 	= saveItem.getType().toString();
		String strFb 				= String.valueOf(saveItem.isForeignPart());
		String strQuantity 			= String.valueOf(saveItem.getTargetQuantity());
		String strStock				= String.valueOf(saveItem.getStock());
		String strActQuantity		= String.valueOf(saveItem.getActualQuantity());
		String strDesc				= getDesciptionString(saveItem);
		String strEquipNo			= saveItem.getEquipNo();
		String strInvNo				= saveItem.getInvNo();
		String strDeviceNo			= saveItem.getDeviceNo();
		String strStatus			= saveItem.getStatus().toString();
		
		
		String[] lineData = {strLayer, strLoc , strType, strFb, strQuantity,
				strStock, strActQuantity,strDesc, strEquipNo,
				strInvNo, strDeviceNo, strStatus}; 
		csvWriterData.writeRecord(lineData);
		
		if(saveItem.getEquipImg().getImg() != null)
		{
			String strId 				= String.valueOf(saveItem.getId());
			String strStream 			= saveItem.getEquipImg().getImgBytes().toString();
			
			String[] lineImage = {strId, strStream}; 
			csvWriterImage.writeRecord(lineImage);
		}
		
	}
	
	private void writeHeader() throws IOException
	{
		String[] headerData = {COLUMN_LAYER, COLUMN_OE , COLUMN_TYPE, COLUMN_FB, COLUMN_QUANTITY,
							COLUMN_STOCK, COLUMN_ACTUALQUANTITY,COLUMN_DESCRIPTION, COLUMN_EQUIP_NO,
							COLUMN_INV_NO, COLUMN_DEVICE_NO, COLUMN_STATUS, COLUMN_IMAGE};
		
		String[] headerImage = {COLUMN_ID, COLUMN_STREAM};
		
		csvWriterData.writeRecord(headerData);
		csvWriterImage.writeRecord(headerImage);
						
	}
	
	private String getDesciptionString(Equipment equip)
	{
		String desc = "";
		for(int i = 0; i < equip.getLayer(); i++)
		{
			desc += " ";
		}
		
		return desc += equip.getDescription();
	}
}
