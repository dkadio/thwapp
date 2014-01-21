package proj.thw.app.ie;


public class FilePackage {

	private FileIE dataFile;
	private FileIE imageFile;
	
	public FilePackage()
	{
		dataFile = null;
		imageFile = null;
	}
	
	public FilePackage(FileIE dataFile,FileIE imageFile)
	{
		this.dataFile = dataFile;
		this.imageFile = imageFile;
		
	}
	
	public FileIE getDataFile() {
		return dataFile;
	}

	public void setDataFile(FileIE dataFile) {
		this.dataFile = dataFile;
	}

	public FileIE getImageFile() {
		return imageFile;
	}

	public void setImageFile(FileIE imageFile) {
		this.imageFile = imageFile;
	}
	
	public String toString()
	{
		return dataFile.getFileName();
	}
}
