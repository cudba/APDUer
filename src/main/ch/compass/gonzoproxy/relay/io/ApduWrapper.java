package ch.compass.gonzoproxy.relay.io;

import ch.compass.gonzoproxy.mvc.model.Packet;

public interface ApduWrapper {

	public byte[] wrap(Packet apdu);

}
