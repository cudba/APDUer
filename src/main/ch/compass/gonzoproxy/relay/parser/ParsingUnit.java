package ch.compass.gonzoproxy.relay.parser;

import java.util.List;

import ch.compass.gonzoproxy.mvc.model.Apdu;
import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.utils.ParsingHelper;

public class ParsingUnit {

	public boolean parseBy(ApduTemplate template, Apdu processingApdu) {
		processingApdu.setDescription(template.getApduDescription());
		List<Field> templateFields = template.getFields();
		byte[] plainApdu = processingApdu.getPlainApdu();

		int contentStartIndex = 0;
		int contentLength = ParsingHelper.DEFAULT_FIELDLENGTH;

		int fieldLength = ParsingHelper.DEFAULT_FIELDLENGTH;
		int offset = 0;

		for (int i = 0; i < templateFields.size(); i++) {
			Field processingField = getCopyOf(templateFields.get(i));
			parseField(plainApdu, fieldLength, offset, processingField);
			processingApdu.addField(processingField);

			int currentFieldOffset = offset;
			offset += ParsingHelper.getEncodedFieldLength(fieldLength, true);

			if (ParsingHelper.isContentLengthField(processingField)) {
				if (ParsingHelper.isContentIdentifierField(templateFields
						.get(i + 1))) {
					contentLength = Integer.parseInt(
							processingField.getValue(), 16);
					contentStartIndex = offset;
				} else {
					fieldLength = Integer.parseInt(processingField.getValue(),
							16);
				}

			} else if (ParsingHelper.isContentIdentifierField(processingField)) {

				int nextIdentifier = 0;
				if (templateFields.size() > i
						+ ParsingHelper.NEXT_IDENTIFIER_OFFSET) {
					nextIdentifier = ParsingHelper.findNextContentIdentifier(
							plainApdu,
							currentFieldOffset,
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
		return true;
	}

	private void parseField(byte[] plainApdu, int fieldLength, int offset,
			Field processingField) {
		if (ParsingHelper.hasCustomLenght(fieldLength)) {
			parseValueToField(plainApdu, offset,
					ParsingHelper.getEncodedFieldLength(fieldLength, false),
					processingField);
		} else {
			int encodedFieldLength = ParsingHelper.getEncodedFieldLength(
					fieldLength, false);
			parseValueToField(plainApdu, offset, encodedFieldLength,
					processingField);
		}
	}

	private void parseValueToField(byte[] plainApdu, int offset,
			int fieldLength, Field field) {
		if ((offset + fieldLength) <= plainApdu.length) {
			byte[] value = ParsingHelper.extractFieldFromBuffer(plainApdu,
					fieldLength, offset);
			setFieldValue(field, value);
		}
	}

	private void setFieldValue(Field field, byte[] value) {
		field.setValue(new String(value));
	}

	private Field getCopyOf(Field field) {
		return new Field(field.getName(), field.getValue(),
				field.getDescription());
	}
}