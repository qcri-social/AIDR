package qa.qcri.aidr.io;

import java.io.File;
import java.io.FilenameFilter;

public class GenericExtFilter implements FilenameFilter {

	private String ext;

	public GenericExtFilter(String ext) {
		this.ext = ext;
	}

	@Override
	public boolean accept(File dir, String name) {
		return (name.endsWith(ext));
	}
}
