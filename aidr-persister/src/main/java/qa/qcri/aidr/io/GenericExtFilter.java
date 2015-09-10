package qa.qcri.aidr.io;

import java.io.File;
import java.io.FilenameFilter;

public class GenericExtFilter implements FilenameFilter {

	private String ext;
	private String excludePattern;

	public GenericExtFilter(String ext, String excludePattern) {
		this.ext = ext;
		this.excludePattern = excludePattern;
	}

	@Override
	public boolean accept(File dir, String name) {
		return (name.endsWith(ext) && !name.contains(excludePattern));
	}
}
