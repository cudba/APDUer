package ch.compass.gonzoproxy.mvc.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Packet implements Serializable, Cloneable {

	private static final long serialVersionUID = -4766720932383072042L;

	private boolean isModified = false;

	private byte[] originalPacket;
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

	public byte[] getOriginalPacket() {
		return originalPacket;
	}

	public String getPacketFromFields() {

		if (this.fields.size() == 0) {
			return new String(originalPacket);
		} else {
			StringBuilder mergedFields = new StringBuilder();

			for (Field field : this.getFields()) {
				mergedFields.append(field.getValue() + " ");
			}
			return mergedFields.substring(0, mergedFields.length() - 1);
		}
	}

	public void setPlainPacket(byte[] packet) {
		this.originalPacket = packet;
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
		return new String(originalPacket);
	}

	public String toAscii() {
		StringBuffer sb = new StringBuffer("");
		String ascii = getPacketFromFields().replaceAll("\\s", "");
		String[] strArr = ascii.split("(?<=\\G..)");

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

	@Override
	public Packet clone() {
		Packet clonedPacket = new Packet(streamInput);
		clonedPacket.setDescription(description);
		clonedPacket.setPreamble(preamble);
		clonedPacket.setPlainPacket(originalPacket);
		clonedPacket.setTrailer(trailer);
		clonedPacket.setSize(size);
		clonedPacket.setType(type);
		for (Field field : fields) {
			clonedPacket.addField(field.clone());
		}
		return clonedPacket;
	}

	public boolean isModified() {
		return isModified;
	}

	public void isModified(boolean isModified) {
		this.isModified = isModified;
	}
}
