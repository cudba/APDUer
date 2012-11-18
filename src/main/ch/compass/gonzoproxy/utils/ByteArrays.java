package ch.compass.gonzoproxy.utils;

import java.util.ArrayList;

public class ByteArrays {
	
	public static byte[] trim(byte[] array, int fromIndex, int length) {
		byte[] newArray = new byte[length];
		System.arraycopy(array, fromIndex, newArray, 0, length);
		return newArray;
	}
	
	public static ArrayList<Integer> getDelimiterIndices(byte[] buffer, char delimiter) {
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
}
