package ch.compass.apduer.relay.io;

import java.util.ArrayList;
import java.util.Queue;

import ch.compass.apduer.mvc.model.Apdu;

public class LibNfcApduExtractor {
	
	private static final char DELIMITER = '#';
	private static final int BUFFER_SIZE = 1024;
	
	private byte[] tmpApdu;
	
	/**
	 * 
	 * @return	Number of missing Bytes from last Apdu in Buffer
	 */

	public int extractApdusToQueue(byte[] buffer, Queue<Apdu> apduQueue, int readBytes) {
		ArrayList<Integer> indices = getDelimiterIndices(buffer);
		
		int startIndex = 0;
		int endIndex = 0;
		
		for (int i = 0; i < indices.size() - 1; i++) {
			startIndex = indices.get(i);
			endIndex = indices.get(i+1);
			int size = endIndex - startIndex;
			byte[] apdu = trim(buffer, startIndex, size);
			apduQueue.add(new Apdu(apdu));
		}
		
		byte [] finalApdu = trim(buffer,endIndex, readBytes);
		
		int missingBytes;
		if ((missingBytes = getMissingBytes(finalApdu)) == 0) {
			apduQueue.add(new Apdu(finalApdu));
			return 0;
		}
		tmpApdu = finalApdu;
		return missingBytes;
	}
	
	private int getMissingBytes(byte[] trailerApdu) {
		return 0;
	}
	
	public byte[] buildFinalApdu(byte[] missingBytes) {
		//TODO: trim missingBytes
		return merge(tmpApdu, missingBytes);
	}

	private byte[] merge(byte[] tmpFinalApdu, byte[] missingBytes) {
		return null;
	}

	private byte[] trim(byte[] array,int fromIndex, int length) {
		byte[] newArray = new byte[length];
		System.arraycopy(array, fromIndex, newArray, 0, length);
		return newArray;
	}
	
	private ArrayList<Integer> getDelimiterIndices(byte[] buffer) {
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for (int i = 0; i < buffer.length; i++) {
			if(buffer[i] == DELIMITER)
				indices.add(i);
				
		}
		return indices;
	}
	
	
}
