package qa.qcri.aidr.io;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.commons.lang3.StringUtils;

public class GenericExtFilter implements FilenameFilter {

	private String ext;
	private String excludePattern;
	private String[] includePatterns;
	public GenericExtFilter(String ext, String excludePattern, String [] includePatterns) {
		this.ext = ext;
		this.excludePattern = excludePattern;
		this.includePatterns = includePatterns;
	}

	@Override
	public boolean accept(File dir, String name) {
		boolean hasExtension = name.endsWith(ext);
		boolean hasAllPattern = Boolean.TRUE;
		for(String includePattern : includePatterns) {
			if(!name.contains(includePattern)) {
				hasAllPattern = false;
				break;
			}
		}
		return (hasExtension && !name.contains(excludePattern) && hasAllPattern);
	}
}
