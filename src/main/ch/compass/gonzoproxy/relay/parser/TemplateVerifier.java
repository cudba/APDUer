package ch.compass.gonzoproxy.relay.parser;

import java.util.ArrayList;

import ch.compass.gonzoproxy.mvc.model.Apdu;
import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.utils.ByteArrays;
import ch.compass.gonzoproxy.utils.ParsingHelper;

public class TemplateVerifier {

	
	
	private int encodingOffset;
	private int whitespaceOffset;

	public TemplateVerifier(int encodingOffset, int whitespaceOffset){
		this.encodingOffset = encodingOffset;
		this.whitespaceOffset = whitespaceOffset;
		
	}

	public boolean accept(ApduTemplate template, Apdu processingApdu) {
		byte[] plainApdu = processingApdu.getPlainApdu();
		ArrayList<Field> templateFields = template.getFields();

		int contentStartIndex = 0;
		int contentLength = ParsingUnit.DEFAULT_FIELDLENGTH;

		int fieldLength = ParsingUnit.DEFAULT_FIELDLENGTH;
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
			offset += ParsingHelper.getEncodedFieldLength(fieldLength,
					encodingOffset);
			if (ParsingHelper.isContentLengthField(processingField)) {
				byte[] length = extractFieldFromBuffer(plainApdu, fieldLength
						* encodingOffset, currentOffset);
				if (ParsingHelper.isContentIdentifierField(templateFields.get(i + 1))) {
					contentLength = hexToInt(length);
					contentStartIndex = offset;
				} else {
					fieldLength = hexToInt(length);
				}
			}

			else if (ParsingHelper.isContentIdentifierField(processingField)) {
				int nextIdentifier = 0;
				if (templateFields.size() > i + ParsingUnit.NEXT_IDENTIFIER_OFFSET) {
					nextIdentifier = ParsingHelper.findNextContentIdentifier(plainApdu,
							currentOffset,
							templateFields.get(i + ParsingUnit.NEXT_IDENTIFIER_OFFSET));
				}

				if (nextIdentifier > 0) {
					fieldLength = ParsingHelper.calculateSubContentLength(offset,
							nextIdentifier, encodingOffset + whitespaceOffset);
				} else {
					fieldLength = ParsingHelper.getRemainingContentSize(contentStartIndex,
							contentLength, offset, encodingOffset);
				}
			} else {
				fieldLength = ParsingUnit.DEFAULT_FIELDLENGTH;
			}
		}
		System.out.println("blubbb");
		return offset - whitespaceOffset == plainApdu.length;
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
		if (fieldLength > ParsingUnit.DEFAULT_FIELDLENGTH) {
			int encodedFieldLength = ParsingHelper.getMultipleEncodedFieldsLength(
					fieldLength, encodingOffset, whitespaceOffset);
			idByte = extractFieldFromBuffer(plainApdu, encodedFieldLength, offset);
		} else {
			idByte = extractFieldFromBuffer(plainApdu, fieldLength * encodingOffset, offset);
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

	protected int hexToInt(byte[] hexValue) {
		return Integer.parseInt(new String(hexValue), 16);
	}

	protected byte[] extractFieldFromBuffer(byte[] plainApdu, int fieldLength,
			int currentOffset) {
		return ByteArrays.trim(plainApdu, currentOffset, fieldLength);
	}

}
