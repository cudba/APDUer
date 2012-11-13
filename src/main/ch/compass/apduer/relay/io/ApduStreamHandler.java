package ch.compass.apduer.relay.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import ch.compass.apduer.mvc.model.Apdu;

public class ApduStreamHandler {

	private static final int BUFFER_SIZE = 1024;
	private char delimiter;

	public ApduStreamHandler(char delimiter) {
		this.delimiter = delimiter;

	}

	public Queue<Apdu> readApdu(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		Queue<Apdu> apduQueue = new LinkedList<Apdu>();
		ArrayList<Integer> delimiterIndices;

		int length = 0;
		int readBytes = 0;
		while ((readBytes = inputStream.read(buffer, length, buffer.length - length)) > 0) {
			length += readBytes;
			System.out.println(length + " Bytes read...");
			delimiterIndices = getDelimiterIndices(buffer);
			int lastDelimiterIndex = extractApdusToQueue(delimiterIndices,buffer, apduQueue);
			eraseExtractedApdus(buffer, lastDelimiterIndex);
			length -= lastDelimiterIndex;
			return apduQueue;
		}
		
		if(length > 0) {
			byte[] lastApdu = trim(buffer, 0, length);
			apduQueue.add(new Apdu(lastApdu));
		}
		return null;
		

	}

	private int extractApdusToQueue(ArrayList<Integer> delimiterIndices, byte[] buffer, Queue<Apdu> apduQueue) {
		ArrayList<Integer> indices = delimiterIndices;
		
		int startIndex = 0;
		int endIndex = 0;
		
		for (int i = 0; i < indices.size() - 1; i++) {
			startIndex = indices.get(i);
			endIndex = indices.get(i+1);
			int size = endIndex - startIndex;
			byte[] apdu = trim(buffer, startIndex, size);
			apduQueue.add(new Apdu(apdu));
		}
		return endIndex;
	}

	private byte[] eraseExtractedApdus(byte[] buffer, int trailerIndex) {
		byte[] cleanBuffer = new byte[BUFFER_SIZE];
		System.arraycopy(buffer, trailerIndex, cleanBuffer, 0, buffer.length - trailerIndex);
		return cleanBuffer;
	}

	private ArrayList<Integer> getDelimiterIndices(byte[] buffer) {
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for (int i = 0; i < buffer.length; i++) {
			if(buffer[i] == delimiter)
				indices.add(i);
				
		}
		return indices;
	}

	public void sendApdu(OutputStream outputStream, byte[] apdu) {
		byte[] buffer = apdu;
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

	private byte[] trim(byte[] array,int fromIndex, int length) {
		byte[] newArray = new byte[length];
		System.arraycopy(array, fromIndex, newArray, 0, length);
		return newArray;
	}
}
