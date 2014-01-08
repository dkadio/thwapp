package proj.thw.app.ie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import proj.thw.app.activitys.EquipmentTreeViewListActivity;
import proj.thw.app.classes.Equipment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

public class THWCSVLoader extends AsyncTask<File, String, ArrayList<Equipment>> implements IThwCSV{

	private Spinner spLoadFile;
	private TextView tvStatus;
	private ProgressBar pbLoad;
	private Context callContext;
	
	private String[][] table;
	
	public THWCSVLoader(Spinner spLoadFile, TextView tvStatus, ProgressBar pbLoad, Context callContext)
	{
		this.spLoadFile = spLoadFile;
		this.tvStatus = tvStatus;
		this.pbLoad = pbLoad;
		this.callContext = callContext;
	}
	
	@Override
	protected ArrayList<Equipment> doInBackground(File... params) {
		
		BufferedReader fileReader;
		try {
			fileReader = new BufferedReader(new FileReader(params[0]));
		} catch (FileNotFoundException e) {
			Log.e(this.getClass().getName(), e.getMessage());
			return null;
		}
		
		
		ArrayList<Equipment> equipList = new ArrayList<Equipment>();
		String line = "";
		try {
			fileReader.readLine();
			fileReader.readLine();
			int currentLine = 0;
			while(( line = fileReader.readLine()) != null)
			{
				publishProgress("Load: " + currentLine++);
				try {
					Thread.currentThread().sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/*
				Equipment insertEquip = new Equipment();
				
				String[] splitArray1 = line.split(";");
				
				insertEquip.setLayer(Integer.parseInt(splitArray1[COLUMN_LAYER]));
				insertEquip.setType(splitArray1[COLUMN_TYPE]);
				insertEquip.setActualQuantity(0);
				insertEquip.setStock(0);
				insertEquip.setTargetQuantity(0);
				insertEquip.setDescription(splitArray1[COLUMN_DESCRIPTION]);
				insertEquip.setEquipNo(splitArray1[COLUMN_EQUIP_NO]);
				insertEquip.setInvNo(splitArray1[COLUMN_INV_NO]);
				insertEquip.setDeviceNo(splitArray1[COLUMN_DEVICE_NO]);
				insertEquip.setStatus(splitArray1[COLUMN_STATUS]);
				
				equipList.add(insertEquip);
				*/
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
	protected void onPreExecute() {
		super.onPreExecute();
		
		pbLoad.setVisibility(View.VISIBLE);
		tvStatus.setVisibility(View.VISIBLE);
		spLoadFile.setVisibility(View.INVISIBLE);
	}
	
	@Override
	protected void onPostExecute(ArrayList<Equipment> result) {
		super.onPostExecute(result);
		
		if(result != null)
		{
			Intent i = new Intent(callContext, EquipmentTreeViewListActivity.class);
	        callContext.startActivity(i);
		}
		
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		tvStatus.setText(values[0].toString());
	}
	
	

}
