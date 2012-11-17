package ch.compass.apduer.mvc.model;

import java.util.ArrayList;

public class Apdu {

	private byte[] plainApdu;
	private byte[] modifiedApdu;
	private byte[] preamble;
	private byte[] trailer;
	private String description;
	private String type;
	private int size;
	private ArrayList<Field> fields;
	
	private byte[] originalApdu;

	public Apdu(byte[] originalApdu) {
		this.originalApdu = originalApdu;
	}

	public byte[] getPlainApdu() {
		return plainApdu;
	}

	public byte[] getModifiedApdu() {
		return modifiedApdu;
	}
	
	public void setPlainApdu(byte[] plainApdu) {
		this.plainApdu = plainApdu;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return new String(plainApdu);
	}

	public String toAscii() {
		// TODO Auto-generated method stub
		return "";
	}

	public void setPreamble(byte[] preamble) {
		this.preamble = preamble;
	}
	
	public byte[] getPreamble() {
		return preamble;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}

	public byte[] getOriginalApdu() {
		return originalApdu;
	}

	public void setTrailer(byte[] trailer) {
		this.trailer = trailer;
	}
	
	public byte[] getTrailer() {
		return trailer;
	}

	public void addField(Field field) {
		fields.add(field);
	}

}
