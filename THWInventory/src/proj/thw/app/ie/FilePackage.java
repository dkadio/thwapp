package proj.thw.app.ie;

import java.util.ArrayList;

public class FilePackage {

	private ArrayList<FileIE> fileList;
	public FilePackage()
	{
		fileList = new ArrayList<FileIE>();
	}
	
	public void addFile(FileIE addFile){
		fileList.add(addFile);
	}
	
	
}
