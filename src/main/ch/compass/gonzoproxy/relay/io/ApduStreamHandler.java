package ch.compass.gonzoproxy.relay.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;

import ch.compass.gonzoproxy.mvc.model.Apdu;
import ch.compass.gonzoproxy.utils.ByteArrays;

public class ApduStreamHandler {

	private static final int BUFFER_SIZE = 1024;
	private ApduExtractor extractor;
	private ApduWrapper wrapper;

	public ApduStreamHandler(ApduExtractor extractor, ApduWrapper wrapper) {
		this.extractor = extractor;
		this.wrapper = wrapper;

	}

	public Queue<Apdu> readApdu(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		Queue<Apdu> apduQueue = new LinkedList<Apdu>();

		int length = 0;
		int readBytes = 0;

		boolean readCompleted = false;

		while (!readCompleted) {

			if (length == buffer.length) {
				ByteArrays.enlarge(buffer);
			}

			if ((readBytes = inputStream.read(buffer, length, buffer.length)) != -1) {
				length += readBytes;
				buffer = extractor.extractApdusToQueue(buffer, apduQueue,
						readBytes);
			} else {
				throw new IOException();
			}

			readCompleted = buffer.length == 0;

			if (!readCompleted) {
				length = buffer.length;
				buffer = ByteArrays.enlarge(buffer, BUFFER_SIZE);
			}
		}
		return apduQueue;
	}

	public void sendApdu(OutputStream outputStream, Apdu apdu)
			throws IOException {

		byte[] buffer = wrapper.wrap(apdu);
		outputStream.write(buffer);
		outputStream.flush();
	}

}
