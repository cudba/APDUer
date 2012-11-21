package ch.compass.gonzoproxy.relay.io;

import ch.compass.gonzoproxy.mvc.model.Apdu;

public interface ApduWrapper {

	public byte[] wrap(Apdu apdu);

}
