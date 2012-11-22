package ch.compass.gonzoproxy.relay.parser;

import java.util.ArrayList;

import ch.compass.gonzoproxy.mvc.model.Apdu;
import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.utils.ByteArrays;

public class ResponseParser extends AbstractParser {

	private static final String IDENTIFIER_LENGTH_FIELD = "LEN";
	Apdu processingApdu;

	public boolean templateIsAccepted(ApduTemplate template) {
		byte[] plainApdu = processingApdu.getPlainApdu();
		ArrayList<Field> templateFields = template.getFields();

		int contentFieldLength = 0;
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
					int encodedFieldLenght = fieldLength * (encodingOffset + whitespaceOffset) - whitespaceOffset ;
					idByte = extractFieldFromBuffer(plainApdu, encodedFieldLenght, offset);
//							ByteArrays.trim(plainApdu, offset, fieldLength
//							* (encodingOffset + whitespaceOffset)
//							- whitespaceOffset);
				} else {
					idByte = extractFieldFromBuffer(plainApdu, fieldLength,
							offset);
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
				byte[] length = extractFieldFromBuffer(plainApdu, fieldLength,
						currentFieldOffset);
				contentFieldLength = hexToInt(length);
			}

			if (isIdentifierField(processingField)) {

			}
			fieldLength = DEFAULT_FIELDLENGTH;
		}
		System.out
				.println("template accepted " + template.getApduDescription());
		return true;
	}


	private boolean isIdentifierLengthField(Field processingField) {
		return processingField.getName().equals(IDENTIFIER_LENGTH_FIELD);
	}

	@Override
	public boolean tryParse(ApduTemplate template) {
		// TODO Auto-generated method stub
		return false;
	}

}
