package proj.thw.app.ie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.util.Log;
import proj.thw.app.classes.Equipment;

public class CSV extends FileHandler implements IThwCSV{

	
	private static final int COLUMN_LAYER 			= 0;
	private static final int COLUMN_OE				= 1;
	private static final int COLUMN_TYPE			= 2;
	private static final int COLUMN_FB				= 3;
	private static final int COLUMN_QUANTITY		= 4;
	private static final int COLUMN_STOCK			= 5;
	private static final int COLUMN_ACTUALQUANTITY	= 6;
	private static final int COLUMN_DESCRIPTION		= 7;
	private static final int COLUMN_EQUIP_NO		= 8;
	private static final int COLUMN_INV_NO			= 9;
	private static final int COLUMN_DEVICE_NO		= 10;
	private static final int COLUMN_STATUS			= 11;
	
	
	public CSV(String filePath) throws FileNotFoundException {
		super(filePath);
		
	}
	
	public CSV(File fileToParse) throws FileNotFoundException {
		super(fileToParse);
		
	}
	public CSV(InputStream is) throws FileNotFoundException {
		super(is);
	}

	@Override
	public ArrayList<Equipment> CSVToEquipmentList() {
		
		return read();
	}

	@Override
	public String EquipmentListToCSV(ArrayList<Equipment> equipList) {
		
		return null;
	}
	
	private ArrayList<Equipment> read()
	{
		ArrayList<Equipment> equipList = new ArrayList<Equipment>();
		String line = "";
		try {
			fileReader.readLine();
			fileReader.readLine();
			while(( line = fileReader.readLine()) != null)
			{
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
		
		return null;
	}
}
