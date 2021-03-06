package proj.thw.app.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;

import proj.thw.app.classes.Equipment;

/**
 * Klasse, die hilfsfunktionalitaeten zur Verfuegung stellt
 * @author max / deniz
 *
 */
public class Helper {
	
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
