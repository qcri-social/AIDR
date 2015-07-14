/**
 * Generates a MD5 digest for generating unique downloadable filenames. 
 */
package qa.qcri.aidr.utils;


import java.security.MessageDigest;

public class MD5HashGenerator {

	public MD5HashGenerator() {}
	
	public String getMD5Hash(String data) throws Exception {
		if (data != null) {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(data.getBytes());

			byte byteData[] = md.digest();

			//convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}

			//System.out.println("Digest(in hex format):: " + sb.toString());

			//convert the byte to hex format method 2
			StringBuffer hexString = new StringBuffer();
			for (int i=0;i<byteData.length;i++) {
				String hex=Integer.toHexString(0xff & byteData[i]);
				if(hex.length()==1) hexString.append('0');
				hexString.append(hex);
			}
			//System.out.println("Digest(in hex format):: " + hexString.toString());
			return hexString.toString();
		} else {
			throw new Exception("Data can't be null");
		}
	}
}

