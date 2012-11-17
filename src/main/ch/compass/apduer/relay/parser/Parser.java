package ch.compass.apduer.relay.parser;

public interface Parser {
	public byte[] getPlainApdu();
	public boolean tryParse(ApduTemplate template);
}
