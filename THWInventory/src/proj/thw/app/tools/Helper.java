package proj.thw.app.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.util.Log;
import proj.thw.app.classes.Equipment;

/**
 * Klasse nur zum uebergeben von EquipmentListen an ein anderes Activity, da ubergabe nur 1MB zulaesst
 * @author max
 *
 */
public class Helper {
public static List<Equipment> eqlist;
	
	public static List<Equipment> equipmentToList(Equipment equip){
		Log.d("mytag", "start equiptolist");

		eqlist = new ArrayList<Equipment>();
		loadNextChild(equip);
		return eqlist;
	}
	
	private static void loadNextChild(Equipment parent){
		Iterator<Equipment> iterator = parent.getChilds().iterator();
		Log.d("mytag", String.valueOf(parent.getId()));
		eqlist.add(parent);
		while (iterator.hasNext()) {
		 Equipment element = iterator.next();
		 loadNextChild(element);
		 }
	}
	
	public static File ListToFileStream(List<Equipment> list, String tempFolderPath) throws IOException
	{
		File tempFile = new File(tempFolderPath + File.separator + "FileStream_" +new Date().getTime());
		if(!tempFile.exists())
			tempFile.createNewFile();
		
		FileOutputStream fos = new FileOutputStream(tempFile);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(list);
		oos.flush();
		oos.close();
		return tempFile;
	}
	
	public static List<Equipment> FileStreamToList(String tempfilePath) throws IOException, ClassNotFoundException
	{
		File tempFile= new File(tempfilePath);
		FileInputStream fis = new FileInputStream(tempFile);
		ObjectInputStream ois = new ObjectInputStream(fis);
		List<Equipment> rsltList =  (List<Equipment>) ois.readObject();
		ois.close();
		tempFile.delete();
		return rsltList;
	}
}
