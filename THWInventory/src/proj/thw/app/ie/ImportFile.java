package proj.thw.app.ie;

import java.io.File;
import java.net.URI;

public class ImportFile extends File {

	private static final long serialVersionUID = 1L;
	
	public ImportFile(URI uri) {
		super(uri);
	}

	public ImportFile(String dirPath, String name) {
		super(dirPath, name);
	}

	public ImportFile(String path) {
		super(path);
	}

	public ImportFile(File dir, String name) {
		super(dir, name);
	}
	
	public String toString()
	{
		return getName();
	}

}
