package proj.thw.app.ie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import proj.thw.app.activitys.EquipmentTreeViewListActivity;
import proj.thw.app.classes.Equipment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class CSVHandler extends AsyncTask<CSVFile, String, ArrayList<Equipment>> {

	@Override
	protected ArrayList<Equipment> doInBackground(CSVFile... params) {
		// TODO Auto-generated method stub
		return null;
	}
/*
	private TextView tvStatus;
	private Context callContext;
	
	public CSVHandler(TextView tvStatus, Context callContext)
	{
		this.tvStatus = tvStatus;
		this.callContext = callContext;
	}
	
	@Override
	protected ArrayList<Equipment> doInBackground(CSVFile... params) {
		
		CSVFile fileToLoad = params[0];
		return null; //read(fileToLoad);
	}

	@Override
	public ArrayList<Equipment> importFile(CSVFile fileToRead) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exportFile(ArrayList<Equipment> equipList) {
		// TODO Auto-generated method stub
		
	}

	/*
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		tvStatus.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected void onPostExecute(ArrayList<Equipment> result) {
		super.onPostExecute(result);
		
		if(result != null)
		{
			Intent i = new Intent(callContext, EquipmentTreeViewListActivity.class);
			i.putExtra(KEY_EQUIPMENTLIST, result);
	        callContext.startActivity(i);
		}
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		tvStatus.setText(values[0].toString());
	}

	@Override
	public ArrayList<Equipment> read(CSVFile fileToRead) {
		
		BufferedReader fileReader;
		try {
			fileReader = new BufferedReader(new FileReader(params[0]));
		} catch (FileNotFoundException e) {
			Log.e(this.getClass().getName(), e.getMessage());
			return null;
		}
		
		
		ArrayList<Equipment> equipList = new ArrayList<Equipment>();
		String line = "";
		int rowCount = 0;
		try {
			
			if((line = fileReader.readLine()) != null)
			{
				//Rowsno init
				HashMap<String,Integer> columnNo = new HashMap<String, Integer>();
			
				String[] sp1 = line.split(SEPERATOR);
				for(int i = 0; i <  sp1.length; i++){
					columnNo.put(sp1[i], i);
				}
				
				fileReader.readLine();
				while(( line = fileReader.readLine()) != null)
				{
					publishProgress("Load RowNo: " + rowCount++);
					
					Equipment insertEquip = new Equipment();
					
					String[] splitArray1 = line.split(SEPERATOR);
					
					int no = columnNo.get(KEY_LAYER);
					String s = splitArray1[no];
					int l = Integer.parseInt(s);
					insertEquip.setLayer(l);
					insertEquip.setActualQuantity(Integer.parseInt(splitArray1[columnNo.get(KEY_QUANTITY)]));
					insertEquip.setStock(Integer.parseInt(splitArray1[columnNo.get(KEY_STOCK)]));
					insertEquip.setTargetQuantity(Integer.parseInt(splitArray1[columnNo.get(KEY_ACTUALQUANTITY)]));
					insertEquip.setDescription(splitArray1[columnNo.get(KEY_DESCRIPTION)]);
					insertEquip.setEquipNo(splitArray1[columnNo.get(KEY_EQUIP_NO)]);
					insertEquip.setInvNo(splitArray1[columnNo.get(KEY_INV_NO)]);
					insertEquip.setDeviceNo(splitArray1[columnNo.get(KEY_DEVICE_NO)]);
					
					//Status split again..
					String[] spStatus = splitArray1[columnNo.get(KEY_STATUS)].split(SEPERATOR_STATUS);
					for(String status : spStatus)
					{
						if(Equipment.Status.V.toString().equals(status))
						{
							insertEquip.getStatus().add(Equipment.Status.V);
						}else if(Equipment.Status.F.toString().equals(status))
						{
							insertEquip.getStatus().add(Equipment.Status.F);
						}else if(Equipment.Status.BA.toString().equals(status))
						{
							insertEquip.getStatus().add(Equipment.Status.BA);
						}else
							insertEquip.getStatus().add(Equipment.Status.NOSTATUS);
						{
					}
						
					if(Equipment.Type.POS.toString().equals(splitArray1[columnNo.get(KEY_TYPE)]))
					{
						insertEquip.setType(Equipment.Type.POS);
					}else if(Equipment.Type.GWM.toString().equals(splitArray1[columnNo.get(KEY_TYPE)]))
					{
						insertEquip.setType(Equipment.Type.GWM);
					}else if(Equipment.Type.SATZ.toString().equals(splitArray1[columnNo.get(KEY_TYPE)]))
					{
						insertEquip.setType(Equipment.Type.SATZ);
					}else if(Equipment.Type.TEIL.toString().equals(splitArray1[columnNo.get(KEY_TYPE)]))
					{
						insertEquip.setType(Equipment.Type.TEIL);
					}else
						insertEquip.setType(Equipment.Type.NOTYPE);
					}
					
					equipList.add(insertEquip);
				}
			}
			
			
			return equipList;
		} catch (IOException e) {
			Log.e(this.getClass().getName(), e.getMessage());
		}
		finally
		{
			if(fileReader != null)
			{
				try {
					fileReader.close();
				} catch (IOException e) {
					Log.e(this.getClass().getName(), e.getMessage());
				}
			}
				
		}
		
		return equipList;
	}

	@Override
	public ArrayList<Equipment> importFile(CSVFile fileToRead) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exportFile(ArrayList<Equipment> equipList) {
		// TODO Auto-generated method stub
		
	}
	*/
}
