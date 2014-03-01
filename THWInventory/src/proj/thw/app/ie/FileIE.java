package proj.thw.app.ie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 *  Klasse, die eine allgemeine File repraesentiert
 * @author max / deniz 
 *
 */
public abstract class FileIE {

	private static final long serialVersionUID = 1L;
	public static final String FILE_EXTENTION_CSV = ".csv";
	public static final String FILE_EXTENTION_XML = ".xml";
	
	public static enum FileType {
		CSV, XML
	};

	//Objektattribute
	protected String			fileName;
	protected String 			filePath; 
	protected File				fileToParse; 	
	protected BufferedReader	fileReader;
	
	
	/**
	 * Constructor
	 * @param filePath Absolute Path to File
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public FileIE(String filePath) throws FileNotFoundException, UnsupportedEncodingException
	{
		this.filePath = filePath;
		this.fileToParse = new File(filePath);
		//this.fileReader = new BufferedReader(new FileReader(fileToParse));
		this.fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileToParse),"ISO-8859-1"));
		this.fileName = fileToParse.getName();
	}
	
	/**
	 * Constructor
	 * @param fileToParse Fileobject
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public FileIE(File fileToParse) throws FileNotFoundException, UnsupportedEncodingException
	{
		this.fileToParse = fileToParse;
		this.filePath = fileToParse.getAbsolutePath();
		this.fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileToParse),"ISO-8859-1"));
		this.fileName = fileToParse.getName();
	}
	
	/*
	public FileIE(InputStream is, String path)
	{
		this.fileReader = new BufferedReader(new InputStreamReader(is));
		this.fileName = fileToParse.getName();
		
		fileToParse = new File();
		OutputStream outputStream = new FileOutputStream(fileToParse);
		IOUtils.copy(inputStream, outputStream);
		outputStream.close();
	}*/

	// Getter/Setter
	/**
	 * Get the current FilePath
	 * @return FilePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Set the current FilePath
	 * @param filePath Path to File
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Get the current FileObject
	 * @return fileToParse
	 */
	public File getFileToParse() {
		return fileToParse;
	}

	/**
	 * Set the current FileObject
	 * @param fileToParse FileObject to parse
	 */
	public void setFileToParse(File fileToParse) {
		this.fileToParse = fileToParse;
	}
	
	public BufferedReader getFileReader() {
		return fileReader;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String toString()
	{
		return fileName;
	}
	
	public void save()
	{
		
	}
	
}
