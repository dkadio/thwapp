package proj.thw.app.ie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public abstract class FileParser {

	//Objektattribute
	protected String 			filePath; 
	protected File				fileToParse; 	
	protected BufferedReader	fileReader;
	
	
	/**
	 * Constructor
	 * @param filePath Absolute Path to File
	 * @throws FileNotFoundException 
	 */
	public FileParser(String filePath) throws FileNotFoundException
	{
		this.filePath = filePath;
		this.fileToParse = new File(filePath);
		this.fileReader = new BufferedReader(new FileReader(fileToParse));
	}
	
	/**
	 * Constructor
	 * @param fileToParse Fileobject
	 * @throws FileNotFoundException 
	 */
	public FileParser(File fileToParse) throws FileNotFoundException
	{
		this.fileToParse = fileToParse;
		this.filePath = fileToParse.getAbsolutePath();
		this.fileReader = new BufferedReader(new FileReader(fileToParse));
	}

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
}
