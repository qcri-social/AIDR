package qa.qcri.aidr.predictui.dto;

import java.math.BigInteger;

public class TaggersForCodes {

    private String code;

    private BigInteger count;

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
