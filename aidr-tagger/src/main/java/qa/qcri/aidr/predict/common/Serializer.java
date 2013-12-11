package qa.qcri.aidr.predict.common;

import java.io.*;

/**
 * Helper class for object serialization and deserialization.
 * 
 * @author jrogstadius
 */
public class Serializer {

    public static byte[] serialize(Serializable o) throws IOException {
        byte[] bytes;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(o);
            bytes = bos.toByteArray();
        } finally {
            out.close();
            bos.close();
        }
        return bytes;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deserialize(byte[] objBytes)
            throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(objBytes);
        ObjectInput in = null;
        T object = null;
        try {
            in = new ObjectInputStream(bis);
            object = (T) in.readObject();
        } finally {
            bis.close();
            in.close();
        }
        return object;
    }
}
