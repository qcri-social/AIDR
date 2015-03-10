package qa.qcri.aidr.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;

public class FileCompressor {

	private static final int BUFFER_SIZE = 1 << 16;

	private String fileName;
	private String outFile;
	private String inputFileName;
	private String outputFileName;
	
	public FileCompressor() {}

	public FileCompressor(final String inputDir, final String outputDir, final String fileName) {
		this.fileName = fileName;
		this.setInputFileName(inputDir, fileName);
		
		String[] nameParts = StringUtils.split(fileName, '.');
		if (nameParts.length > 0) {
			String fileNamePrefix = nameParts[0];		// use only file prefix or full fileName for output file generation? 
			this.setOutputFileName(outputDir, fileName);
		}
		//System.out.println("inputFileName = " + this.getInputFileName() + " outputFileName = " + this.getOutputFileName());
	}
	
	public String zip() {

		byte[] buffer = new byte[BUFFER_SIZE];
	
		try {
			FileOutputStream fos = new FileOutputStream(this.getOutputFileName());
			ZipOutputStream zos = new ZipOutputStream(fos);
			ZipEntry ze = new ZipEntry(this.getFileName());
			zos.putNextEntry(ze);
			
			FileInputStream in = new FileInputStream(this.getInputFileName());
			int len;
			while ((len = in.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}
			in.close();
			zos.closeEntry();
			zos.close();
			System.out.println("Done zipping file: " + this.inputFileName + ", created file = " + this.outputFileName);
			return this.getOutFile();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getOutFile() {
		return this.outFile;
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
	public String getInputFileName() {
		return this.inputFileName;
	}
	
	public void setInputFileName(String inputDir, String fileName) {
		this.inputFileName = inputDir + "/" + fileName;
	}
	
	public String getOutputFileName() {
		return this.outputFileName;
	}
	
	public void setOutputFileName(String outputDir, String fileNamePrefix) {
		this.outputFileName = outputDir + "/" + fileNamePrefix + ".zip";
		this.outFile = fileNamePrefix + ".zip";
	}
	
 }
