package ch.compass.apduer.relay.io;

import java.util.ArrayList;
import java.util.Queue;

import ch.compass.apduer.mvc.model.Apdu;

public class LibNfcApduExtractor {

	private static final char DELIMITER = '#';

	private byte[] tmpApdu;

	/**
	 * 
	 * @return Number of missing Bytes from last Apdu in Buffer
	 */

	public int extractApdusToQueue(byte[] buffer, Queue<Apdu> apduQueue,
			int readBytes) {
		ArrayList<Integer> indices = getDelimiterIndices(buffer);

		int startIndex = 0;
		int endIndex = 0;

		for (int i = 0; i < indices.size() - 1; i++) {
			startIndex = indices.get(i);
			endIndex = indices.get(i + 1);
			int size = endIndex - startIndex;
			byte[] rawApdu = trim(buffer, startIndex, size);
			Apdu apdu = splitApdu(rawApdu);
			apduQueue.add(apdu);
		}

		byte[] finalApdu = trim(buffer, endIndex, readBytes);

		int missingBytes;
		if ((missingBytes = getMissingBytes(finalApdu)) == 0) {
			Apdu apdu = splitApdu(finalApdu);
			apduQueue.add(apdu);
			return 0;
		}
		tmpApdu = finalApdu;
		return missingBytes;
	}

	private Apdu splitApdu(byte[] rawApdu) {
		// TODO Auto-generated method stub
		int size = getApduSize(rawApdu);
		byte[] preamble = getApduPreamble(rawApdu, size);
		byte[] plainApdu = getPlainApdu(rawApdu, size);
		byte[] trailer = getApduTrailer(rawApdu, size);
		Apdu newApdu = new Apdu(rawApdu);
		newApdu.setPreamble(preamble);
		newApdu.setPlainApdu(plainApdu);
		newApdu.setTrailer(trailer);
		newApdu.setSize(size);
		return newApdu;
	}


	private byte[] getApduTrailer(byte[] rawApdu, int size) {
		for (int i = 0; i < rawApdu.length; i++) {
			if(rawApdu[i] == ':'){
				int endOfPlainApdu = i+3*size+1;
				return trim(rawApdu, endOfPlainApdu, rawApdu.length - endOfPlainApdu);
			}
		}
		return null;
	}

	private byte[] getPlainApdu(byte[] rawApdu, int size) {
		for (int i = 0; i < rawApdu.length; i++) {
			if(rawApdu[i] == ':'){
				return trim(rawApdu, i+2, size*3-1);		}
		}
		return rawApdu;
	}

	private byte[] getApduPreamble(byte[] rawApdu, int size) {
		for (int i = 0; i < rawApdu.length; i++) {
			if(rawApdu[i] == ':'){
				return trim(rawApdu, 0, i+2);
			}
		}
		return rawApdu;
	}

	private int getApduSize(byte[] rawApdu) {
		int value = 0;
		byte[] size = new byte[4];
		for (int i = 0; i < rawApdu.length; i++) {
			if(rawApdu[i] == ' '){
				size[0] = rawApdu[i+1];
				size[1] = rawApdu[i+2];
				size[2] = rawApdu[i+3];
				size[3] = rawApdu[i+4];
				value = Integer.parseInt(new String(size),16);
				System.out.println("Size: " + value);
				return value;
			}
		}
		return value;
	}

	private int getMissingBytes(byte[] trailerApdu) {
		return 0;
	}

	public byte[] buildFinalApdu(byte[] missingBytes) {
		// TODO: trim missingBytes
		return merge(tmpApdu, missingBytes);
	}

	private byte[] merge(byte[] tmpFinalApdu, byte[] missingBytes) {
		return null;
	}

	private byte[] trim(byte[] array, int fromIndex, int length) {
		byte[] newArray = new byte[length];
		System.arraycopy(array, fromIndex, newArray, 0, length);
		return newArray;
	}

	private ArrayList<Integer> getDelimiterIndices(byte[] buffer) {
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for (int i = 0; i < buffer.length; i++) {
			if (buffer[i] == DELIMITER)
				indices.add(i);

		}
		return indices;
	}

}
