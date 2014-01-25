package proj.thw.app.ie;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import proj.thw.app.R;
import proj.thw.app.activitys.ImportDataActivity;
import proj.thw.app.classes.Equipment;
import proj.thw.app.classes.EquipmentImage;
import proj.thw.app.database.OrmDBHelper;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ThwCsvImporter extends AsyncTask<FilePackage, String, Boolean>{

	static final String COLUMN_LAYER 			= "Ebene";
	static final String COLUMN_OE				= "OE";
	static final String COLUMN_TYPE				= "Art";
	static final String COLUMN_FB				= "FB";
	static final String COLUMN_QUANTITY			= "Menge";
	static final String COLUMN_STOCK			= "Verfügbar";
	static final String COLUMN_ACTUALQUANTITY	= "Menge Ist";
	static final String COLUMN_DESCRIPTION		= "Ausstattung | Hersteller | Typ";
	static final String COLUMN_EQUIP_NO			= "Sachnummer";
	static final String COLUMN_INV_NO			= "Inventar Nr";
	static final String COLUMN_DEVICE_NO		= "Gerätenr.";
	static final String COLUMN_STATUS			= "Status";
	static final String COLUMN_IMAGE			= "Image";
	
	private ProgressDialog asyncDialog;
	
	private OrmDBHelper dbHelper;
	private ImportDataActivity 	callContext;
	private TextView 	tvStatus;
	private HashMap<String, Integer> columnHeaders;
	
	EquipmentImage defaultEquipImg;
	
	public ThwCsvImporter(OrmDBHelper dbHelper, ImportDataActivity callContext, TextView tvStatus)
	{
		this.dbHelper 		= dbHelper;
		this.callContext 	= callContext;
		this.tvStatus 		= tvStatus;
	}
	
	public ThwCsvImporter(OrmDBHelper dbHelper, ImportDataActivity callContext)
	{
		this.dbHelper 		= dbHelper;
		this.callContext 	= callContext;
		this.tvStatus 		= null;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		if(tvStatus != null)
		{
			tvStatus.setVisibility(View.VISIBLE);
		}
		else
		{
			asyncDialog = new ProgressDialog(callContext);
			asyncDialog.setCanceledOnTouchOutside(false);
			asyncDialog.setTitle("Please Wait...");
			asyncDialog.setMessage("importiere Daten...");
			asyncDialog.setIcon(callContext.getResources().getDrawable(R.drawable.db_icon));
			asyncDialog.show();
		}
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		
		if(!result)
		{
			Toast.makeText(callContext, "Import fehlgeschlagen...", Toast.LENGTH_LONG).show();
		}
		if(tvStatus != null)
		{
			tvStatus.setText("");
			tvStatus.setVisibility(View.INVISIBLE);
		}
		else
		{
			asyncDialog.dismiss();
			callContext.getReturnHandler().handleMessage(new Message());
		}
		//Intent i = new Intent(callContext, EquipmentTreeViewListActivity.class);
        //callContext.startActivity(i);
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		if(tvStatus != null)
		{
			tvStatus.setText(values[0].toString());
		}
		else
		{
			asyncDialog.setMessage(values[0].toString());
		}
	}
	
	@Override
	protected Boolean doInBackground(FilePackage... params) {
		
		if(params.length == 0)
		{
			return false;
		}
		
		FilePackage fileToImport = params[0];
		
		//Bitmap als DefaultImage laden...
		Bitmap bmp = BitmapFactory.decodeStream(callContext.getResources().openRawResource(R.drawable.error));
		defaultEquipImg = new EquipmentImage(bmp);
		
		int rowCount = 0;
		String line = "";
		try {
			if((line = fileToImport.getDataFile().getFileReader().readLine()) != null)
			{
				//lese header aus...
				//Vector<String> columnHeaders = new Vector<String>();
				publishProgress("initialisiere Header...");
				columnHeaders = new HashMap<String, Integer>();
	
				String[] spHeader = line.split(((CSVFile)fileToImport.getDataFile()).getSeparator());
				for(int i = 0; i <  spHeader.length; i++){
					String col = spHeader[i].replace('"',' ').trim();
					columnHeaders.put(col.trim().toUpperCase(),i);
				}
				
				//lese Standort aus
				String headerline = fileToImport.getDataFile().getFileReader().readLine();
				String[] spStandort = headerline.split(((CSVFile)fileToImport.getDataFile()).getSeparator());
				
				String location = "";
				if(columnHeaders.containsKey(COLUMN_OE.toUpperCase()))
					location = spStandort[columnHeaders.get(COLUMN_OE.toUpperCase())];
				
				while(( line = fileToImport.getDataFile().getFileReader().readLine()) != null)
				{	
					publishProgress("load RowNr:  " + rowCount++);
					String[] spRow = line.split(((CSVFile)fileToImport.getDataFile()).getSeparator());
					
					Equipment newEquip = new Equipment(); 
					
					newEquip.setLocation(location);
					
					//parse Layer from row
					int layer = 0;
					if(columnHeaders.containsKey(COLUMN_LAYER.toUpperCase()))
						layer = getIntValue(spRow[columnHeaders.get(COLUMN_LAYER.toUpperCase())]);
					
					newEquip.setLayer(layer);
					
					//parse DeviceNo
					String deviceNo = "";
					if(columnHeaders.containsKey(COLUMN_DEVICE_NO.toUpperCase()))
						deviceNo = getStringValue(spRow[columnHeaders.get(COLUMN_DEVICE_NO.toUpperCase())]);
					
					newEquip.setDeviceNo(deviceNo);
					
					//parse EquipNo
					String equipNo = "";
					if(columnHeaders.containsKey(COLUMN_EQUIP_NO.toUpperCase()))
						equipNo = getStringValue(spRow[columnHeaders.get(COLUMN_EQUIP_NO.toUpperCase())]);
					
					newEquip.setEquipNo(equipNo);
					
					//parse InvNo
					String invNo = "";
					if(columnHeaders.containsKey(COLUMN_INV_NO.toUpperCase()))
						invNo = getStringValue(spRow[columnHeaders.get(COLUMN_INV_NO.toUpperCase())]);
					
					newEquip.setInvNo(invNo);
					
					//parse Description
					String desc = "";
					if(columnHeaders.containsKey(COLUMN_DESCRIPTION.toUpperCase()))
						desc = getStringValue(spRow[columnHeaders.get(COLUMN_DESCRIPTION.toUpperCase())]);
					
					newEquip.setDescription(desc);
					
					//parse actualquantity
					int quantity = 0;
					if(columnHeaders.containsKey(COLUMN_QUANTITY.toUpperCase()))
						quantity = getIntValue(spRow[columnHeaders.get(COLUMN_QUANTITY.toUpperCase())]);
					
					newEquip.setActualQuantity(quantity);
					
					//parse targetquantity
					int targetquantity = 0;
					if(columnHeaders.containsKey(COLUMN_ACTUALQUANTITY.toUpperCase()))
						targetquantity = getIntValue(spRow[columnHeaders.get(COLUMN_ACTUALQUANTITY.toUpperCase())]);
					
					newEquip.setTargetQuantity(targetquantity);
					
					//parse stock
					int stock = 0;
					if(columnHeaders.containsKey(COLUMN_STOCK.toUpperCase()))
						stock = getIntValue(spRow[columnHeaders.get(COLUMN_STOCK.toUpperCase())]);
					
					newEquip.setStock(stock);
					
					
					//Status split again..
					String[] spStatus = spRow[columnHeaders.get(COLUMN_STATUS.toUpperCase())].split(",");
					for(String status : spStatus)
					{
						status = getStringValue(status);
						if(Equipment.Status.V.toString().equals(status))
						{
							newEquip.getStatus().add(Equipment.Status.V);
						}
						else if(Equipment.Status.F.toString().equals(status))
						{
							newEquip.getStatus().add(Equipment.Status.F);
						}
						else if(Equipment.Status.BA.toString().equals(status))
						{
							newEquip.getStatus().add(Equipment.Status.BA);
						}
						else if (Equipment.Status.A.toString().equals(status))
						{
							newEquip.getStatus().add(Equipment.Status.A);
						}
						
					}
					
					String type = getStringValue(getStringValue(spRow[columnHeaders.get(COLUMN_TYPE.toUpperCase())]));
					if(Equipment.Type.POS.toString().equals(type.toUpperCase()))
					{
						newEquip.setType(Equipment.Type.POS);
					}else if(Equipment.Type.GWM.toString().equals(type.toUpperCase()))
					{
						newEquip.setType(Equipment.Type.GWM);
					}
					else if(Equipment.Type.SATZ.toString().equals(type.toUpperCase()))
					{
						newEquip.setType(Equipment.Type.SATZ);
					}
					else if(Equipment.Type.TEIL.toString().equals(type.toUpperCase()))
					{
						newEquip.setType(Equipment.Type.TEIL);
					}
					else
					{
						newEquip.setType(Equipment.Type.NOTYPE);
					}
					
					
					//pruefe ob Image vohanden ist
					if(columnHeaders.containsKey(COLUMN_IMAGE.toUpperCase()))
					{
						
					}
					else
					{
						//Wenn nicht, dann nimm default
						//newEquip.setEquipImg(defaultEquipImg);
					}
						
					
					//speichere Equipmentobjekt in DB
					dbHelper.getDbHelperEquip().insertEquipment(newEquip);
				}
				
			}
		} catch (IOException e) {
			Log.e(this.getClass().getName(), e.getMessage());
			return false;
		} catch (SQLException e) {
			Log.e(this.getClass().getName(), e.getMessage());
			return false;
		} catch (Exception e) {
			Log.e(this.getClass().getName(), e.getMessage());
			return false;
		}
	
		return true;
	}
	
	private int getIntValue(String rowValue)
	{
		try {
			String intAsString = rowValue.replace('"', ' ').trim();
			int rslt = Integer.parseInt(intAsString);
			return rslt;
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	private String getStringValue(String rowValue)
	{
		return rowValue.replace('"', ' ').trim();
	}
}
