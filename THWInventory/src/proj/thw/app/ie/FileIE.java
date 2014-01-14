package proj.thw.app.ie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public abstract class FileIE {

	//Objektattribute
	protected String			fileName;
	protected String 			filePath; 
	protected File				fileToParse; 	
	protected BufferedReader	fileReader;
	
	
	/**
	 * Constructor
	 * @param filePath Absolute Path to File
	 * @throws FileNotFoundException 
	 */
	public FileIE(String filePath) throws FileNotFoundException
	{
		this.filePath = filePath;
		this.fileToParse = new File(filePath);
		this.fileReader = new BufferedReader(new FileReader(fileToParse));
		this.fileName = fileToParse.getName();
	}
	
	/**
	 * Constructor
	 * @param fileToParse Fileobject
	 * @throws FileNotFoundException 
	 */
	public FileIE(File fileToParse) throws FileNotFoundException
	{
		this.fileToParse = fileToParse;
		this.filePath = fileToParse.getAbsolutePath();
		this.fileReader = new BufferedReader(new FileReader(fileToParse));
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
	
	public String toString()
	{
		return fileName;
	}
	
}
