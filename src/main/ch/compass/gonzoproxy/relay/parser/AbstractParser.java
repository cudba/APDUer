package ch.compass.gonzoproxy.relay.parser;

import ch.compass.gonzoproxy.mvc.model.Apdu;
import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.utils.ByteArrays;

public abstract class AbstractParser {

	private static final String CONTENT_LENGTH_FIELD = "Lc";

	protected static final int DEFAULT_FIELDLENGTH = 1;

	protected int encodingOffset = 2;
	protected int whitespaceOffset = 1;

	protected Apdu processingApdu;

	protected boolean apduContainsMoreFields(byte[] plainApdu, int fieldLength,
			int offset) {
		return (offset + fieldLength * encodingOffset) <= plainApdu.length;
	}

	protected boolean isIdentifierField(Field processingField) {
		return processingField.getValue() != null;
	}

	protected boolean fieldIsVerified(byte[] plainApdu, int fieldLength,
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

	protected boolean valueMatches(byte[] idByte, Field field) {
		String apduValue = new String(idByte);
		String templateValue = field.getValue();
		return apduValue.equals(templateValue);
	}

	protected byte[] extractFieldFromBuffer(byte[] plainApdu, int fieldLength,
			int currentOffset) {
		return ByteArrays.trim(plainApdu, currentOffset, fieldLength);
	}

	protected void parseField(byte[] plainApdu, int fieldLength, int offset,
			Field processingField) {
		if (hasCustomLenght(fieldLength)) {
			parseValueToField(plainApdu, offset,
					encodedMultipleFieldsLength(fieldLength), processingField);
		} else {
			parseValueToField(plainApdu, offset, fieldLength * encodingOffset,
					processingField);
		}
	}

	protected void parseValueToField(byte[] plainApdu, int offset,
			int fieldLength, Field field) {
		if ((offset + fieldLength) <= plainApdu.length) {
			byte[] value = ByteArrays.trim(plainApdu, offset, fieldLength);
			setFieldValue(field, value);
			processingApdu.addField(field);
		}
	}

	protected boolean hasCustomLenght(int fieldLength) {
		return fieldLength > DEFAULT_FIELDLENGTH;
	}

	protected void setFieldValue(Field field, byte[] value) {
		field.setValue(new String(value));
	}

	protected int hexToInt(byte[] hexValue) {
		return Integer.parseInt(new String(hexValue), 16);
	}

	protected Field getCopyOf(Field field) {
		return new Field(field.getName(), field.getValue(),
				field.getDescription());
	}

	protected void setApduDescription(ApduTemplate template) {
		processingApdu.setDescription(template.getApduDescription());
	}

	protected boolean isContentLengthField(Field processingField) {
		return processingField.getName().equals(CONTENT_LENGTH_FIELD);
	}

	public void setProcessingApdu(Apdu apdu) {
		this.processingApdu = apdu;
	}

	public void setEncodingSettings(int encodingOffset, int whitespaceOffset) {
		this.encodingOffset = encodingOffset;
		this.whitespaceOffset = whitespaceOffset;
	}

	protected int encodedSingleFieldLength(int fieldLength) {
		return fieldLength * (encodingOffset + whitespaceOffset);
	}

	protected int encodedMultipleFieldsLength(int fieldLength) {
		return encodedSingleFieldLength(fieldLength) - whitespaceOffset;
	}

	abstract boolean templateIsAccepted(ApduTemplate template);

	abstract boolean tryParse(ApduTemplate template);

}
