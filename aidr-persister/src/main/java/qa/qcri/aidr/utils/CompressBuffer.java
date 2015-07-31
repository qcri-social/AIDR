/**
 * methods for (un)compressing tweets 
 */
package qa.qcri.aidr.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class CompressBuffer extends FileCompressor {
	
	private static Logger logger = Logger.getLogger(CompressBuffer.class.getName());
	//ByteArrayOutputStream out = null;
	FileOutputStream out = null;
	ZipOutputStream outZip = null;
	ZipEntry ze = null;
	String folderLocation = null;
	
	@Override
	public void setBufferSize(int value) {
		if (value < 32) {
			BUFFER_SIZE = 1 << value;
		}
	}
	
	/**
	 * 
	 * @param folderLocation location where to put the zipped file
	 * @param zippedFile name of the zipped file, without path
	 */
	public CompressBuffer(String folderLocation, String zippedFile) {
		super();
		this.folderLocation = folderLocation;
		this.setBufferSize(20);
		//out = new ByteArrayOutputStream(BUFFER_SIZE);
		try {
			out = new FileOutputStream(folderLocation + File.separator + zippedFile);
			outZip = new ZipOutputStream(out);
			ze = new ZipEntry(zippedFile);
			outZip.putNextEntry(ze);
		} catch (Exception e) {
			logger.error("IOException while compressing the buffer"+e);
		}
	}
	
	public CompressBuffer() {
		super();
	}

	/**
	 * 
	 * @param folderLocation location where to put the zipped file
	 * @param zippedFile name of the zipped file, without path
	 */
	public void init(String folderLocation, String zippedFile) {
		//out = new ByteArrayOutputStream(BUFFER_SIZE);	
		this.folderLocation = folderLocation;
		this.setBufferSize(20);
		try {
			out = new FileOutputStream(folderLocation + File.separator + zippedFile);
			outZip = new ZipOutputStream(out);
			ze = new ZipEntry(zippedFile);
			outZip.putNextEntry(ze);
		} catch (Exception e) {
			logger.error("IOException while initializing the compress buffer"+e);
		}
	}

	public void close() {
		try {
			outZip.closeEntry();
			outZip.close();
		} catch (Exception e) {
			logger.error("IOException while closing the compressed file");
		}
	}

	/**
	 * 
	 * @param data raw byte data
	 * @return returns zipped data as byte array
	 */
	public void zip(final byte[] data) {
		try {
			outZip.write(data, 0, data.length);
		} catch (Exception e) {
			logger.error("IOException while compressing the file ");
		}
	}

	/**
	 * 
	 * @param str String to zip
	 * @return zipped data as byte array
	 */
	public void zip(final String str) {
		try {
			byte[] buffer = str.trim().getBytes();
			zip(buffer); 
		} catch (Exception e) {
			logger.error("IOException while compressing the file ");
		}
	}

	/**
	 * This function will decompress a zipped file to a file on disk and also returns the decompressed data buffer
	 * @param zipFile compressed file to unzip
	 * @param unzippedFile name of decompressed file on disk, with full path, null if not to be written to disk
	 * @return byte array of decompressed data 
	 */
	public byte[] unzip(String zipFile, String unzippedFile) {
		byte[] buffer = new byte[BUFFER_SIZE];
		byte[] outBuffer = new byte[BUFFER_SIZE];
		FileOutputStream fos = null;
		try{
			//get the zip file content
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));

			//get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();
			int lastPos = 0;
			while (ze != null){
				if (unzippedFile != null) {
					File newFile = new File(unzippedFile);
					fos = new FileOutputStream(newFile);             
				}
				int len;
				while ((len = zis.read(buffer, 0, BUFFER_SIZE)) > 0)  {
					if (fos != null) {
						fos.write(buffer, 0, len);
					}
					System.arraycopy(buffer, 0, outBuffer, lastPos, len);	// copy decompressed content to in-memory buffer
					lastPos += len;
				}
				if (fos != null) fos.close();   
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();

			System.out.println("Done unzipping!");
			byte[] returnBuf = new byte[lastPos];
			System.arraycopy(outBuffer, 0, returnBuf, 0, lastPos);		// in-memory buffer to return
			return returnBuf;

		} catch(Exception e) {
			logger.error("IOException while unzipping the directory ");
			return null;
		}
	}

	/**
	 * This function will decompress a zipped file and return the decompressed data buffer
	 * @param zipFile compressed file to unzip
	 * @return byte array of decompressed data 
	 */
	public byte[] unzip(String zipFile) {
		return this.unzip(zipFile, null);
	}
	
	/**
	 * This function will decompress a zipped file to a file on disk and also returns the decompressed data buffer as a String
	 * @param zipFile compressed file to unzip
	 * @param unzippedFile name of decompressed file on disk, with full path, null if not to be written to disk
	 * @return String representation of the decompressed data 
	 */
	public String unzipAsString(String zipFile, String unzippedFile) {
		try {
			byte[] unzippedBuffer = unzip(zipFile, unzippedFile);
			if (unzippedBuffer != null) {
				String string = new String(unzippedBuffer).trim();
				return string;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("Exception in unzipping the directory"+e);
			return null;
		}
	}
	
	/**
	 * This function will decompress a zipped file and return the decompressed data buffer as a String
	 * @param zipFile compressed file to unzip
	 * @return String representation decompressed data 
	 */
	public String unzipAsString(String zipFile) {
		try {
			String unzippedString = unzipAsString(zipFile, null);
			return unzippedString;
		} catch (Exception e) {
			logger.error("Exception in unzipping the directory"+e);
			return null;
		}
	}

	/*public static void main(String[] args) throws Exception {
		CompressBuffer zipper = new CompressBuffer();

		String collectionCode = "20150104-0348-SinhaKoushik-abcdertgh";
		String folderLocation = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH) + collectionCode;

		String unzippedFileName = "testData2.txt";
		BufferedReader br = null;

		// test zipping a text file
		try {
			zipper.init(folderLocation, "testZip.zip");
			br = new BufferedReader(new FileReader(folderLocation+ File.separator + unzippedFileName));
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println("Read line from unzipped file = " + line + ", of length = " + line.length());
				zipper.zip(line);
			}
			zipper.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		br.close();
		//writer.close();
		System.out.println("Done creating ZIP file!");
		
		// test unzipping a zipped file
		byte[] unzippedBuffer = zipper.unzip(folderLocation + File.separator + "testZip.zip", folderLocation + File.separator + "testUnzip.txt");
		System.out.println("Done creating UNZIP file! unzipped size = " + unzippedBuffer.length);
		System.out.println((unzippedBuffer != null ? new String(unzippedBuffer) : "Error in unzip!"));
		
		String unzippedString = zipper.unzipAsString(folderLocation + File.separator + "testZip.zip");
		System.out.println("Done creating UNZIP file! unzipped string size = " + unzippedString.length());
		System.out.println((unzippedBuffer != null ? unzippedString : "Error in unzip!"));		
	}*/
}
