package ch.compass.gonzoproxy.mvc.model;

import java.util.ArrayList;

public class Packet {

	private byte[] plainPacket;
	private byte[] modifiedPacket;
	private byte[] preamble;
	private byte[] trailer;
	private String description;
	private ForwardingType type;
	private int size;
	private ArrayList<Field> fields = new ArrayList<Field>();

	private byte[] streamInput;

	public Packet(byte[] streamInput) {
		this.streamInput = streamInput;
	}

	public byte[] getPlainPacket() {
		return plainPacket;
	}

	public byte[] getModifiedPacket() {
		return modifiedPacket;
	}

	public void setPlainPacket(byte[] packet) {
		this.plainPacket = packet;
	}

	public void setModifiedPacket(byte[] modifiedPacket) {
		this.modifiedPacket = modifiedPacket;
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

	public ForwardingType getType() {
		return type;
	}

	public void setType(ForwardingType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return new String(plainPacket);
	}

	public String toAscii() {
		StringBuffer sb = new StringBuffer("");
		String ascii = new String(plainPacket);
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

	public byte[] getStreamInput() {
		return streamInput;
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
