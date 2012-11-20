package ch.compass.gonzoproxy.utils;

import java.util.ArrayList;

public class ByteArrays {

	public static byte[] trim(byte[] array, int fromIndex, int length) {
		if (validInput(array, fromIndex, length)) {
			byte[] newArray = new byte[length];
			System.arraycopy(array, fromIndex, newArray, 0, length);
			return newArray;
		}
		return array;
	}

	public static ArrayList<Integer> getDelimiterIndices(byte[] buffer,
			char delimiter) {
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for (int i = 0; i < buffer.length; i++) {
			if (buffer[i] == delimiter)
				indices.add(i);

		}
		return indices;
	}

	public static byte[] merge(byte[] tmpFinalApdu, byte[] missingBytes) {
		return null;
	}

	private static boolean validInput(byte[] array, int fromIndex, int length) {
		return array.length >= fromIndex + length && length >= 0;
	}
}
