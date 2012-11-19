package ch.compass.gonzoproxy.relay.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;

import ch.compass.gonzoproxy.mvc.model.Apdu;

public class ApduStreamHandler {

	private static final int BUFFER_SIZE = 1024;

	public ApduStreamHandler() {

	}

	public Queue<Apdu> readApdu(InputStream inputStream) throws IOException {
		LibNfcApduExtractor extractor = new LibNfcApduExtractor();
		byte[] buffer = new byte[BUFFER_SIZE];
		Queue<Apdu> apduQueue = new LinkedList<Apdu>();

		int length = 0;
		int readBytes = 0;
		if ((readBytes = inputStream.read(buffer, length, buffer.length)) != -1) {
			
			length += readBytes;
			
			int missingBytes = extractor.extractApdusToQueue(buffer, apduQueue, length);
			
			if ((length + missingBytes) > buffer.length) {
				buffer = enlarge(buffer);
			}
			
			if((readBytes = inputStream.read(buffer, length, missingBytes)) != 0) {
				int startIndex = length;
				length += readBytes;
				//TODO: add apdu to queue
			}
			return apduQueue;
		}
		throw new IOException("Stream disconnected...Nein?... Doch!... OOHHHHHCCHH!");
	}

	public void sendApdu(OutputStream outputStream, Apdu apdu) {
		LibNfcApduWrapper wrapper = new LibNfcApduWrapper();
		
		byte[] buffer = wrapper.wrap(apdu);
		try {
			outputStream.write(buffer);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	private byte[] enlarge(byte[] array) {
		byte[] newArray = new byte[array.length << 1];
		System.arraycopy(array, 0, newArray, 0, array.length);
		return newArray;
	}
}
