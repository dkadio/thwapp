package proj.thw.app.ie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import proj.thw.app.classes.Equipment;
import android.util.Log;

public class CSV extends FileHandler implements IThwCSV{

	
	public CSV(String filePath) throws FileNotFoundException {
		super(filePath);
		
	}
	
	public CSV(File fileToParse) throws FileNotFoundException {
		super(fileToParse);
		
	}
	public CSV(InputStream is) throws FileNotFoundException {
		super(is);
	}
/*
	@Override
	public ArrayList<Equipment> CSVToEquipmentList() {
		
		return read();
	}

	@Override
	public String EquipmentListToCSV(ArrayList<Equipment> equipList) {
		
		return null;
	}
	*/
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
