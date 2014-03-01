package proj.thw.app.ie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * Klasse, die eine CSV repraesentiert
 * @author max / deniz
 *
 */
public class CSVFile extends FileIE{

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
