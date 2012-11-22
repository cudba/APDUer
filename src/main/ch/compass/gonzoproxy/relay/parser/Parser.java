package ch.compass.gonzoproxy.relay.parser;

import ch.compass.gonzoproxy.mvc.model.Apdu;

public interface Parser {
	public boolean templateIsAccepted(ApduTemplate template);
	public boolean tryParse(ApduTemplate template);
	public void setEncodingSettings(int encodingOffset, int whitespaceOffset);
	public void setProcessingApdu(Apdu apdu);
}
