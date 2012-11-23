package ch.compass.gonzoproxy.relay.parser;

import java.util.ArrayList;

import ch.compass.gonzoproxy.mvc.model.Package;
import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.utils.ParsingHelper;

public class TemplateValidator {


	public boolean accept(ApduTemplate template, Package processingApdu) {
		byte[] plainApdu = processingApdu.getPlainPackage();
		ArrayList<Field> templateFields = template.getFields();

		int contentStartIndex = 0;
		int contentLength = ParsingHelper.DEFAULT_FIELDLENGTH;

		int fieldLength = ParsingHelper.DEFAULT_FIELDLENGTH;
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
			offset += ParsingHelper.getEncodedFieldLength(fieldLength, true);
			if (ParsingHelper.isContentLengthField(processingField)) {
				int encodedFieldLength = ParsingHelper.getEncodedFieldLength(
						fieldLength, false);
				byte[] length = ParsingHelper.extractFieldFromBuffer(plainApdu,
						encodedFieldLength, currentOffset);
				if (ParsingHelper.isContentIdentifierField(templateFields
						.get(i + 1))) {
					contentLength = hexToInt(length);
					contentStartIndex = offset;
				} else {
					fieldLength = hexToInt(length);
				}
			}

			else if (ParsingHelper.isContentIdentifierField(processingField)) {
				int nextIdentifier = 0;
				if (templateFields.size() > i
						+ ParsingHelper.NEXT_IDENTIFIER_OFFSET) {
					nextIdentifier = ParsingHelper.findNextContentIdentifier(
							plainApdu,
							currentOffset,
							templateFields.get(i
									+ ParsingHelper.NEXT_IDENTIFIER_OFFSET));
				}

				if (nextIdentifier > 0) {
					fieldLength = ParsingHelper.calculateSubContentLength(
							offset, nextIdentifier);
				} else {
					fieldLength = ParsingHelper.getRemainingContentSize(
							contentStartIndex, contentLength, offset);
				}
			} else {
				fieldLength = ParsingHelper.DEFAULT_FIELDLENGTH;
			}
		}
		return offset - ParsingHelper.whitespaceOffset == plainApdu.length;
	}

	private boolean apduContainsMoreFields(byte[] plainApdu, int fieldLength,
			int offset) {
		return (offset + fieldLength * ParsingHelper.encodingOffset) <= plainApdu.length;
	}

	private boolean isIdentifierField(Field processingField) {
		return processingField.getValue() != null;
	}

	private boolean fieldIsVerified(byte[] plainApdu, int fieldLength,
			int offset, Field processingField) {
		byte[] idByte;
		int encodedFieldLength = ParsingHelper.getEncodedFieldLength(
				fieldLength, false);
		if (fieldLength > ParsingHelper.DEFAULT_FIELDLENGTH) {
			idByte = ParsingHelper.extractFieldFromBuffer(plainApdu,
					encodedFieldLength, offset);
		} else {
			idByte = ParsingHelper.extractFieldFromBuffer(plainApdu,
					encodedFieldLength, offset);
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

}
