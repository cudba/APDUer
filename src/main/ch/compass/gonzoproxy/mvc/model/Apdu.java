package ch.compass.gonzoproxy.mvc.model;

import java.util.ArrayList;

public class Apdu {

	private byte[] plainApdu;
	private byte[] modifiedApdu;
	private byte[] preamble;
	private byte[] trailer;
	private String description;
	private ApduType type;
	private int size;
	private ArrayList<Field> fields = new ArrayList<Field>();

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

	public ApduType getType() {
		return type;
	}

	public void setType(ApduType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return new String(plainApdu);
	}

	public String toAscii() {
		StringBuffer sb = new StringBuffer("");
		String ascii = new String(plainApdu);
		String[] strArr = ascii.split(" ");

		for (String a : strArr) {
			int c = Integer.parseInt(a, 16);
			char chr = (char) c;
			sb.append(chr);
		}

		return sb.toString();
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
