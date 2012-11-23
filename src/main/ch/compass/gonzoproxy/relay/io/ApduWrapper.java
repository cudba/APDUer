package ch.compass.gonzoproxy.relay.io;

import ch.compass.gonzoproxy.mvc.model.Package;

public interface ApduWrapper {

	public byte[] wrap(Package apdu);

}
