package proj.thw.app.ie;

import java.io.File;
import java.io.FileNotFoundException;

public class CSVFile extends FileIE{

	//evtl ein TableObject um CSVFile als Table im Speicher zu halten... JAVA besitzt kein TableObjekt!! ToDo
	private String separator; 

	public CSVFile(String filePath, String separator) throws FileNotFoundException {
		super(filePath);
		this.separator = separator;
	}
	
	public CSVFile(File fileToParse, String separator) throws FileNotFoundException {
		super(fileToParse);
		this.separator = separator;
	}
	
	/*
	public CSVFile(InputStream is, String separator) throws FileNotFoundException {
		super(is);
		this.separator = separator;
	}*/
	
	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	
}
