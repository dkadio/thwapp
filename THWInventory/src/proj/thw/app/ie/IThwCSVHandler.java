package proj.thw.app.ie;

import java.util.ArrayList;

import proj.thw.app.classes.Equipment;
import proj.thw.app.database.OrmDBHelper;

public interface IThwCSVHandler{

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
	
	public void csvFileToDatabase();
	
}