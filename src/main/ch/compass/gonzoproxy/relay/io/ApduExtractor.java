package ch.compass.gonzoproxy.relay.io;

import java.util.Queue;

import ch.compass.gonzoproxy.mvc.model.Apdu;

public interface ApduExtractor {

	public int extractApdusToQueue(byte[] buffer, Queue<Apdu> apduQueue,
			int readBytes);

}
