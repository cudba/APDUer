package ch.compass.gonzoproxy.relay.io;

import java.util.ArrayList;
import java.util.Queue;

import ch.compass.gonzoproxy.mvc.model.Apdu;
import ch.compass.gonzoproxy.utils.ByteArrays;

public class LibNfcApduExtractor implements ApduExtractor {

	private static final char DELIMITER = '#';

	private byte[] tmpApdu;

	/**
	 * 
	 * @return Number of missing Bytes from last Apdu in Buffer
	 */

	public int extractApdusToQueue(byte[] buffer, Queue<Apdu> apduQueue,
			int readBytes) {
		ArrayList<Integer> indices = ByteArrays.getDelimiterIndices(buffer, DELIMITER);

		int startIndex = 0;
		int endIndex = 0;

		for (int i = 0; i < indices.size() - 1; i++) {
			startIndex = indices.get(i);
			endIndex = indices.get(i + 1);
			int size = endIndex - startIndex;
			byte[] rawApdu = ByteArrays.trim(buffer, startIndex, size);
			Apdu apdu = splitApdu(rawApdu);
			apduQueue.add(apdu);
		}

		byte[] finalApdu = ByteArrays.trim(buffer, endIndex, readBytes);

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
				return ByteArrays.trim(rawApdu, endOfPlainApdu, rawApdu.length - endOfPlainApdu);
			}
		}
		return null;
	}

	private byte[] getPlainApdu(byte[] rawApdu, int size) {
		for (int i = 0; i < rawApdu.length; i++) {
			if(rawApdu[i] == ':'){
				return ByteArrays.trim(rawApdu, i+2, size*3-1);		}
		}
		return rawApdu;
	}

	private byte[] getApduPreamble(byte[] rawApdu, int size) {
		for (int i = 0; i < rawApdu.length; i++) {
			if(rawApdu[i] == ':'){
				return ByteArrays.trim(rawApdu, 0, i+2);
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
		return ByteArrays.merge(tmpApdu, missingBytes);
	}

}
