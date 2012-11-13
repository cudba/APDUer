package ch.compass.apduer.mvc.model;

import java.util.ArrayList;

public class Apdu {

	private byte[] originalApdu;

	private byte[] modifiedApdu;

	private String description;

	private ArrayList<Field> fields;

	public Apdu(byte[] originalApdu) {
		this.originalApdu = originalApdu;
	}

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

	public ArrayList<Field> getFields() {
		return fields;
	}

	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		return new String(originalApdu);
	}

}
