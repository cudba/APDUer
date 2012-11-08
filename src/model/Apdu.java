package model;

public class Apdu {

	
	private byte[] originalApdu;
	
	
	private byte[] modifiedApdu;
	
	private String description;

	public byte[] getOriginalApdu() {
		return originalApdu;
	}

	public void setOriginalApdu(byte[] originalApdu) {
		this.originalApdu = originalApdu;
	}

	public byte[] getModifiedApdu() {
		return modifiedApdu;
	}

	public void setModifiedApdu(byte[] modifiedApdu) {
		this.modifiedApdu = modifiedApdu;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

}
