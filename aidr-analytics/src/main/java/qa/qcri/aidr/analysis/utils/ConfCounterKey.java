package qa.qcri.aidr.analysis.utils;

public class ConfCounterKey extends CounterKey {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5215949450260629342L;
	
	private String binNumber;
	
	public ConfCounterKey() {
		super();
	}

	public ConfCounterKey(String crisisCode, String attributeCode, String labelCode, String binNumber) {
		super();
		if (binNumber != null) {
			this.setBinNumber(binNumber);
		}
	}

	public String getBinNumber() {
		return binNumber;
	}

	public void setBinNumber(String binNumber) {
		this.binNumber = binNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((binNumber == null) ? 0 : binNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfCounterKey other = (ConfCounterKey) obj;
		if (binNumber == null) {
			if (other.binNumber != null)
				return false;
		} else if (!binNumber.equals(other.binNumber))
			return false;
		return true;
	}

}
