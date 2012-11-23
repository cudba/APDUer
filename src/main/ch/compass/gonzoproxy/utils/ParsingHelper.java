package ch.compass.gonzoproxy.utils;

import ch.compass.gonzoproxy.mvc.model.Field;

public class ParsingHelper {

	public static final int NEXT_IDENTIFIER_OFFSET = 2;
	public static final int DEFAULT_FIELDLENGTH = 1;
	public static final String CONTENT_LENGTH_FIELD = "Lc";
	public static final String CONTENT_IDENTIFIER = "Ci";
	
	public static int encodingOffset = 2;
	public static int whitespaceOffset = 1;

	public static int getEncodedFieldLength(int fieldLength, boolean whitespace) {
		if(whitespace) {
			return fieldLength * (encodingOffset + whitespaceOffset);
		}
		else {
			return fieldLength * (encodingOffset + whitespaceOffset)
					- whitespaceOffset;
		}
	}


	public static int findNextContentIdentifier(byte[] plainApdu, int offset,
			Field field) {
		String nextIdentifier = field.getValue();
		byte[] byteField = new byte[2];
		for (int i = offset; i < plainApdu.length - 1; i++) {
			byteField[0] = plainApdu[i];
			byteField[1] = plainApdu[i + 1];
			if (new String(byteField).equals(nextIdentifier)) {
				return i;
			}
		}
		return 0;
	}

	public static int getRemainingContentSize(int contentStartIndex,
			int contentLength, int offset) {
		return contentLength
				- ((offset - contentStartIndex) / (encodingOffset + whitespaceOffset));
	}

	public static boolean isContentLengthField(Field processingField) {
		return processingField.getName() != null
				&& processingField.getName().equals(CONTENT_LENGTH_FIELD);
	}

	public static int calculateSubContentLength(int offset, int nextIdentifier) {
		return (nextIdentifier - offset) / (encodingOffset + whitespaceOffset);
	}

	public static boolean isContentIdentifierField(Field processingField) {
		return processingField.getName().equals(CONTENT_IDENTIFIER);
	}

	public static boolean hasCustomLenght(int fieldLength) {
		return fieldLength > DEFAULT_FIELDLENGTH;
	}

	public static byte[] extractFieldFromBuffer(byte[] plainApdu,
			int fieldLength, int currentOffset) {
		return ByteArraysUtils.trim(plainApdu, currentOffset, fieldLength);
	}

}
