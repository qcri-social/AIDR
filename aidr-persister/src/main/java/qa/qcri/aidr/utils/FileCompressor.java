/**
 * Compress a file to zip format.
 * 
 */
package qa.qcri.aidr.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class FileCompressor {

	private static Logger logger = Logger.getLogger(FileCompressor.class.getName());
	
	public static int BUFFER_SIZE = 1 << 16;

	private String fileName;
	private String outFile;
	private String inputFileName;
	private String outputFileName;
	private String outputUnzippedFileName;
	private String outUnzippedFile;

	public FileCompressor() {}

	/**
	 * 
	 * @param inputDir path to input file
	 * @param outputDir path to output file
	 * @param fileName input file name without the directory path
	 */
	public FileCompressor(final String inputDir, final String outputDir, final String fileName) {
		this.fileName = fileName;
		this.setInputFileName(inputDir, fileName);

		String[] nameParts = StringUtils.split(fileName, '.');
		if (nameParts.length > 0) {
			String fileNamePrefix = nameParts[0];		// use only file prefix or full fileName for output file generation? 
			this.setOutputFileName(outputDir, fileName);
			if (nameParts.length > 1) {
				this.setUnzippedOutputFileName(outputDir, fileNamePrefix, nameParts[1]);
			} else {
				this.setUnzippedOutputFileName(outputDir, fileNamePrefix, null);
			}
		}
		//System.out.println("inputFileName = " + this.getInputFileName() + " outputFileName = " + this.getOutputFileName());
	}

	public String zip() {

		byte[] buffer = new byte[BUFFER_SIZE];

		try(OutputStream fos = new BufferedOutputStream(new FileOutputStream(this.getOutputFileName()), BUFFER_SIZE);
				ZipOutputStream zos = new ZipOutputStream(fos);
				InputStream in = new BufferedInputStream(new FileInputStream(this.getInputFileName()), BUFFER_SIZE);) {
			
			ZipEntry ze = new ZipEntry(this.getFileName());
			zos.putNextEntry(ze);
			int len;
			while ((len = in.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}
			in.close();
			zos.closeEntry();
			zos.close();
			//System.out.println("Done zipping file: " + this.inputFileName + ", created file = " + this.outputFileName);
			return this.getOutFile();

		} catch (Exception e) {
			logger.error("IOException while compressing the file: "+fileName);
			return null;
		}
	}

	/**
	 * 
	 * @return unzips an input file that is in zipped format
	 */
	public String unzip() {
		byte[] buffer = new byte[BUFFER_SIZE];
		try(ZipInputStream zis = new ZipInputStream(new FileInputStream(this.getInputFileName()));) {
			// get the zip file content
			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();
			if (ze != null) {
				File newFile = new File(this.getUnzippedOutputFileName());
				FileOutputStream fos = new FileOutputStream(newFile);             

				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();   
				ze = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();

			//System.out.println("Done unzipping file: " + this.getInputFileName() + ", created file = " + this.getUnzippedOutputFileName());
			return this.getOutUnzippedFile();
		} catch(Exception ex) {
			logger.error("IOException while unzipping the file"+ex);
			return null;
		}
	}

	public String getOutFile() {
		return this.outFile;
	}

	public String getOutUnzippedFile() {
		return this.outUnzippedFile;
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

	public String getUnzippedOutputFileName() {
		return this.outputUnzippedFileName;
	}

	public void setUnzippedOutputFileName(String outputDir, String fileNamePrefix, String extension) {
		this.outputUnzippedFileName = outputDir + "/" + fileNamePrefix;
		this.outUnzippedFile = fileNamePrefix;
		if (extension != null) {
			this.outputUnzippedFileName = this.outputUnzippedFileName +  "." + extension;
			this.outUnzippedFile = this.outUnzippedFile + "." + extension;
		}
	}

	public void setBufferSize(int value) {
		BUFFER_SIZE = value;
	}
	
	public int getBufferSize() {
		return BUFFER_SIZE;
	}
}
