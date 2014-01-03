package proj.thw.app.ie;

import java.io.File;
import java.io.FileNotFoundException;

public class XMLParser extends FileParser implements IThwXMLParser{

	
	public XMLParser(String filePath) throws FileNotFoundException {
		super(filePath);
		
	}
	
	public XMLParser(File fileToParse) throws FileNotFoundException {
		super(fileToParse);
		
	}


}
