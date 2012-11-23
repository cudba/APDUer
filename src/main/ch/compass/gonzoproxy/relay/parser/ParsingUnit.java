package ch.compass.gonzoproxy.relay.parser;

import java.util.ArrayList;
import java.util.List;

import ch.compass.gonzoproxy.mvc.model.Apdu;
import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.utils.ByteArrays;

public class ParsingUnit {

	public static final String CONTENT_LENGTH_FIELD = "Lc";
	public static final String CONTENT_IDENTIFIER = "Ci";
	public static final int NEXT_IDENTIFIER_OFFSET = 2;
	public static final int DEFAULT_FIELDLENGTH = 1;

	private Apdu processingApdu;
	
	private int encodingOffset;
	private int whitespaceOffset;


	public ParsingUnit(int encodingOffset, int whitespaceOffset) {
		this.encodingOffset = encodingOffset;
		this.whitespaceOffset = whitespaceOffset;

	}

	public void setProcessingApdu(Apdu apdu) {
		this.processingApdu = apdu;
	}


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

			int currentOffset = offset;
			offset += encodedSingleFieldLength(fieldLength);
			if (isContentLengthField(processingField)) {
				byte[] length = extractFieldFromBuffer(plainApdu, fieldLength
						* encodingOffset, currentOffset);
				if (isContentIdentifierField(templateFields.get(i + 1))) {
					contentLength = hexToInt(length);
					contentStartIndex = offset;
				} else {
					fieldLength = hexToInt(length);
				}
			}

			else if (isContentIdentifierField(processingField)) {
				int nextIdentifier = 0;
				if (templateFields.size() > i + NEXT_IDENTIFIER_OFFSET) {
					nextIdentifier = findNextIdentifier(plainApdu,
							currentOffset,
							templateFields.get(i + NEXT_IDENTIFIER_OFFSET));
				}

				if (nextIdentifier > 0) {
					fieldLength = calculateSubContentLength(offset,
							nextIdentifier);
				} else {
					fieldLength = getRemainingContentSize(contentStartIndex,
							contentLength, offset);
				}
			} else {
				fieldLength = DEFAULT_FIELDLENGTH;
			}
		}
		return offset - whitespaceOffset == plainApdu.length;
	}

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
				if (isContentIdentifierField(templateFields.get(i + 1))) {
					contentLength = Integer.parseInt(
							processingField.getValue(), 16);
					contentStartIndex = offset;
				} else {
					fieldLength = Integer.parseInt(processingField.getValue(),
							16);
				}

			} else if (isContentIdentifierField(processingField)) {

				int nextIdentifier = 0;
				if (templateFields.size() > i + NEXT_IDENTIFIER_OFFSET) {
					nextIdentifier = findNextIdentifier(plainApdu,
							currentFieldOffset,
							templateFields.get(i + NEXT_IDENTIFIER_OFFSET));
				}

				if (nextIdentifier > 0) {
					fieldLength = calculateSubContentLength(offset,
							nextIdentifier);
				} else {
					fieldLength = getRemainingContentSize(contentStartIndex,
							contentLength, offset);
				}

			} else {
				fieldLength = DEFAULT_FIELDLENGTH;
			}
		}
		return true;
	}

	private boolean isContentIdentifierField(Field processingField) {
		return processingField.getName().equals(CONTENT_IDENTIFIER);
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

	private int calculateSubContentLength(int offset, int nextIdentifier) {
		return (nextIdentifier - offset) / (encodingOffset + whitespaceOffset);
	}

	private int getRemainingContentSize(int contentStartIndex,
			int contentLength, int offset) {
		return contentLength
				- ((offset - contentStartIndex) / (encodingOffset + whitespaceOffset));
	}

	private boolean apduContainsMoreFields(byte[] plainApdu, int fieldLength,
			int offset) {
		return (offset + fieldLength * encodingOffset) <= plainApdu.length;
	}

	private boolean isIdentifierField(Field processingField) {
		return processingField.getValue() != null;
	}

	private boolean fieldIsVerified(byte[] plainApdu, int fieldLength,
			int offset, Field processingField) {
		byte[] idByte;
		if (hasCustomLenght(fieldLength)) {
			idByte = ByteArrays.trim(plainApdu, offset,
					encodedMultipleFieldsLength(fieldLength));
		} else {
			idByte = ByteArrays.trim(plainApdu, offset, fieldLength
					* encodingOffset);
		}
		if (!valueMatches(idByte, processingField)) {
			return false;
		}
		return true;
	}

	private boolean valueMatches(byte[] idByte, Field field) {
		String apduValue = new String(idByte);
		String templateValue = field.getValue();
		return apduValue.equals(templateValue);
	}

	private byte[] extractFieldFromBuffer(byte[] plainApdu, int fieldLength,
			int currentOffset) {
		return ByteArrays.trim(plainApdu, currentOffset, fieldLength);
	}

	private void parseField(byte[] plainApdu, int fieldLength, int offset,
			Field processingField) {
		if (hasCustomLenght(fieldLength)) {
			parseValueToField(plainApdu, offset,
					encodedMultipleFieldsLength(fieldLength), processingField);
		} else {
			parseValueToField(plainApdu, offset, fieldLength * encodingOffset,
					processingField);
		}
	}

	private void parseValueToField(byte[] plainApdu, int offset,
			int fieldLength, Field field) {
		if ((offset + fieldLength) <= plainApdu.length) {
			byte[] value = ByteArrays.trim(plainApdu, offset, fieldLength);
			setFieldValue(field, value);
			processingApdu.addField(field);
		}
	}

	private boolean hasCustomLenght(int fieldLength) {
		return fieldLength > DEFAULT_FIELDLENGTH;
	}

	private void setFieldValue(Field field, byte[] value) {
		field.setValue(new String(value));
	}

	private int hexToInt(byte[] hexValue) {
		return Integer.parseInt(new String(hexValue), 16);
	}

	private Field getCopyOf(Field field) {
		return new Field(field.getName(), field.getValue(),
				field.getDescription());
	}

	private void setApduDescription(ApduTemplate template) {
		processingApdu.setDescription(template.getApduDescription());
	}

	private boolean isContentLengthField(Field processingField) {
		return processingField.getName() != null
				&& processingField.getName().equals(CONTENT_LENGTH_FIELD);
	}

	private int encodedSingleFieldLength(int fieldLength) {
		return fieldLength * (encodingOffset + whitespaceOffset);
	}

	private int encodedMultipleFieldsLength(int fieldLength) {
		return encodedSingleFieldLength(fieldLength) - whitespaceOffset;
	}

}
