package ch.compass.gonzoproxy.relay.parser;

import ch.compass.gonzoproxy.mvc.model.Apdu;

public interface Parser {
	public void setProcessingApdu(Apdu apdu);
	public Apdu getProcessingApdu();
	public boolean tryParse(ApduTemplate template);
	public boolean templateIsAccepted(ApduTemplate template);
}
