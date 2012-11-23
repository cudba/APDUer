package ch.compass.gonzoproxy.utils;

import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.relay.parser.ParsingUnit;

public class ParsingHelper {
	
	
	public static int getEncodedFieldLength(int fieldLength, int encodingOffset) {
		return getMultipleEncodedFieldsLength(fieldLength, encodingOffset, 0);
	}

	public static int getMultipleEncodedFieldsLength(int fieldLength, int encodingOffset, int whitespaceOffset) {
		return fieldLength * encodingOffset - whitespaceOffset;
	}
	
	public static int findNextContentIdentifier(byte[] plainApdu, int offset, Field field) {
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
			int contentLength, int offset, int encodingOffset) {
		return contentLength
				- ((offset - contentStartIndex) / (encodingOffset));
	}
	
	public static boolean isContentLengthField(Field processingField) {
		return processingField.getName() != null
				&& processingField.getName().equals(ParsingUnit.CONTENT_LENGTH_FIELD);
	}
	
	public static int calculateSubContentLength(int offset, int nextIdentifier, int encodingOffset) {
		return (nextIdentifier - offset) / (encodingOffset);
	}
	
	public static boolean isContentIdentifierField(Field processingField) {
		return processingField.getName().equals(ParsingUnit.CONTENT_IDENTIFIER);
	}
	

}
