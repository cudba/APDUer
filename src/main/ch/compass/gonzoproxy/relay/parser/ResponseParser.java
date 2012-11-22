package ch.compass.gonzoproxy.relay.parser;

import java.util.ArrayList;
import java.util.List;

import ch.compass.gonzoproxy.mvc.model.Field;

public class ResponseParser extends AbstractParser {

	private static final String IDENTIFIER_LENGTH_FIELD = "Ci";
	private static final int NEXT_IDENTIFIER_OFFSET = 2;
	

	public boolean templateIsAccepted(ApduTemplate template) {
		byte[] plainApdu = processingApdu.getPlainApdu();
		ArrayList<Field> templateFields = template.getFields();

		int contentStartIndex = 0;
		int contentLength = DEFAULT_FIELDLENGTH;

		int fieldLength = DEFAULT_FIELDLENGTH;
		int offset = 0;
		
		for (int i = 0; i < templateFields.size(); i++) {
			if (!apduContainsMoreFields(plainApdu, fieldLength, offset)) {
				return false;
			}

			Field processingField = templateFields.get(i);
			if (isIdentifierField(processingField)) {
				if (!fieldIsVerified(plainApdu, fieldLength, offset,
						processingField)) {
					return false;
				}
			}

			int currentFieldOffset = offset;
			offset += encodedSingleFieldLength(fieldLength);
			if (isContentLengthField(processingField)) {
				byte[] length = extractFieldFromBuffer(plainApdu, fieldLength
						* encodingOffset, currentFieldOffset);
				contentLength = hexToInt(length);
				contentStartIndex = offset;
			}

			else if (isContentIdentifierField(processingField)) {
				int nextIdentifier = 0;
				if (templateFields.size() > i + NEXT_IDENTIFIER_OFFSET) {
					nextIdentifier = findNextIdentifier(plainApdu,
							currentFieldOffset,
							templateFields.get(i + NEXT_IDENTIFIER_OFFSET));
				}

				if (nextIdentifier > 0) {
					fieldLength = nextIdentifier;
				} else {
					fieldLength = getRemainingContentSize(contentStartIndex,
							contentLength, offset);
				}
			} else {
				fieldLength = DEFAULT_FIELDLENGTH;
			}
		}
		System.out.println(template.getApduDescription() + " accepted");
		return true;
	}

	@Override
	public boolean tryParse(ApduTemplate template) {
		setApduDescription(template);
		List<Field> templateFields = template.getFields();
		byte[] plainApdu = processingApdu.getPlainApdu();
		
		int contentStartIndex = 0;
		int contentLength = DEFAULT_FIELDLENGTH;

		int fieldLength = DEFAULT_FIELDLENGTH;
		int offset = 0;
		
		for (int i = 0; i < templateFields.size(); i++) {
			Field processingField = getCopyOf(templateFields.get(i));
			parseField(plainApdu, fieldLength, offset, processingField);

			int currentFieldOffset = offset;
			offset += encodedSingleFieldLength(fieldLength);

			if (isContentLengthField(processingField)) {
				contentLength = Integer.parseInt(processingField.getValue(), 16);
				contentStartIndex = offset;
			} else if (isContentIdentifierField(processingField)) {

				int nextIdentifier = 0;
				if (templateFields.size() > i + NEXT_IDENTIFIER_OFFSET) {
					nextIdentifier = findNextIdentifier(plainApdu,
							currentFieldOffset,
							templateFields.get(i + NEXT_IDENTIFIER_OFFSET));
				}

				if (nextIdentifier > 0) {
					fieldLength = nextIdentifier;
				}else {
					fieldLength = getRemainingContentSize(contentStartIndex,
							contentLength, offset);
				}
				
			} else {
				fieldLength = DEFAULT_FIELDLENGTH;
			}
		}
		return true;
	}

	private int getRemainingContentSize(int contentStartIndex,
			int contentLength, int offset) {
		return contentLength - (offset - contentStartIndex)
				/ (encodingOffset + whitespaceOffset);
	}

	private int findNextIdentifier(byte[] plainApdu, int offset, Field field) {
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

	private boolean isContentIdentifierField(Field processingField) {
		return processingField.getName().equals(IDENTIFIER_LENGTH_FIELD);
	}

}
