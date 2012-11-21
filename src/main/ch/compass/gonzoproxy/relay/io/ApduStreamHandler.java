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

	public ApduStreamHandler() {

	}

	public Queue<Apdu> readApdu(InputStream inputStream) throws IOException {
		ApduExtractor extractor = new LibNfcApduExtractor();
		byte[] buffer = new byte[BUFFER_SIZE];
		Queue<Apdu> apduQueue = new LinkedList<Apdu>();

		int length = 0;
		int readBytes = 0;
		if ((readBytes = inputStream.read(buffer, length, buffer.length)) != -1) {
			
			length += readBytes;
			
			int missingBytes = extractor.extractApdusToQueue(buffer, apduQueue, length);
			
			if ((length + missingBytes) > buffer.length) {
				buffer = ByteArrays.enlarge(buffer);
			}
			
			if((readBytes = inputStream.read(buffer, length, missingBytes)) != 0) {
				int startIndex = length;
				length += readBytes;
				//TODO: add apdu to queue
			}
			return apduQueue;
		}
		throw new IOException();
	}

	public void sendApdu(OutputStream outputStream, Apdu apdu) {
		ApduWrapper wrapper = new LibNfcApduWrapper();
		
		byte[] buffer = wrapper.wrap(apdu);
		try {
			outputStream.write(buffer);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
