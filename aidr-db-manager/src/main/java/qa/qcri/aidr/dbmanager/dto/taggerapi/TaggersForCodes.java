package qa.qcri.aidr.dbmanager.dto.taggerapi;

import java.io.Serializable;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TaggersForCodes implements Serializable {

	@XmlElement private String code;

	@XmlElement private BigInteger count;

	public TaggersForCodes() {}
	
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigInteger getCount() {
        return count;
    }

    public void setCount(BigInteger count) {
        this.count = count;
    }
}
