package proj.thw.app.ie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class CSVFile extends FileIE{

	//evtl ein TableObject um CSVFile als Table im Speicher zu halten... JAVA besitzt kein TableObjekt!! ToDo
	private char separator; 

	public CSVFile(String filePath, char separator) throws FileNotFoundException, UnsupportedEncodingException {
		super(filePath);
		this.separator = separator;
	}
	
	public CSVFile(File fileToParse, char separator) throws FileNotFoundException, UnsupportedEncodingException {
		super(fileToParse);
		this.separator = separator;
	}
	
	/*
	public CSVFile(InputStream is, String separator) throws FileNotFoundException {
		super(is);
		this.separator = separator;
	}*/
	
	public char getSeparator() {
		return separator;
	}

	public void setSeparator(char separator) {
		this.separator = separator;
	}

	
}
