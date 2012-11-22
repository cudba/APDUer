package ch.compass.gonzoproxy.relay.parser;

import java.util.ArrayList;
import java.util.List;

import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.utils.ByteArrays;

public class CommandParser extends AbstractParser {

	public boolean templateIsAccepted(ApduTemplate template) {
		byte[] plainApdu = processingApdu.getPlainApdu();
		ArrayList<Field> templateFields = template.getFields();

		int fieldLength = DEFAULT_FIELDLENGTH;
		int offset = 0;
		for (int i = 0; i < templateFields.size(); i++) {
			if (apduContainsMoreFields(plainApdu, fieldLength, offset)) {
				return false;
			}

			Field processingField = template.getFields().get(i);
			if (isIdentifierField(processingField)) {
				byte[] idByte;
				if (hasCustomLenght(fieldLength)) {
					idByte = ByteArrays.trim(plainApdu, offset, fieldLength
							* (encodingOffset + whitespaceOffset)
							- whitespaceOffset);
				} else {
					idByte = ByteArrays.trim(plainApdu, offset, fieldLength
							* encodingOffset);
				}
				if (!valueMatches(idByte, templateFields.get(i))) {
					return false;
				}
				System.out.println("Field matched");
			}

			// TODO: Refactor
			int currentFieldOffset = offset;
			offset += fieldLength * (encodingOffset + whitespaceOffset);
			if (isContentLengthField(processingField)) {
				byte[] length = ByteArrays.trim(plainApdu, currentFieldOffset,
						fieldLength * encodingOffset);
				fieldLength = hexToInt(length);
			} else {
				fieldLength = DEFAULT_FIELDLENGTH;
			}
		}
		System.out
				.println("template accepted " + template.getApduDescription());
		return true;
	}

	@Override
	public boolean tryParse(ApduTemplate template) {
		setApduDescription(template);
		List<Field> templateFields = template.getFields();
		byte[] plainApdu = processingApdu.getPlainApdu();

		int fieldLength = DEFAULT_FIELDLENGTH;
		int offset = 0;
		for (int i = 0; i < templateFields.size(); i++) {
			Field processingField = getCopyOf(templateFields.get(i));

			if (hasCustomLenght(fieldLength)) {
				parseValueToField(plainApdu, offset, fieldLength
						* (encodingOffset + whitespaceOffset)
						- whitespaceOffset, processingField);
			} else {
				parseValueToField(plainApdu, offset, fieldLength
						* encodingOffset, processingField);
			}

			offset += fieldLength * (encodingOffset + whitespaceOffset);

			if (isContentLengthField(processingField)) {
				fieldLength = Integer.parseInt(processingField.getValue(), 16);
			} else {
				fieldLength = DEFAULT_FIELDLENGTH;
			}
		}
		return true;
	}

}
