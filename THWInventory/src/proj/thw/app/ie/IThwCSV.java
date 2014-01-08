package proj.thw.app.ie;

import java.util.ArrayList;

import proj.thw.app.classes.Equipment;

public interface IThwCSV {

	public ArrayList<Equipment> CSVToEquipmentList();
	public String EquipmentListToCSV(ArrayList<Equipment> equipList);
}
