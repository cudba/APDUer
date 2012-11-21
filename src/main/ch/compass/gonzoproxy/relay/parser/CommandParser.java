package ch.compass.gonzoproxy.relay.parser;

import java.util.ArrayList;
import java.util.List;

import ch.compass.gonzoproxy.mvc.model.Apdu;
import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.utils.ByteArrays;

public class CommandParser implements Parser {

	private static final int DEFAULT_FIELDLENGTH = 1;

	private int encodingOffset = 1;
	private int whitespaceOffset = 0;

	private Apdu processingApdu;
	

	public boolean templateIsAccepted(ApduTemplate template) {
		byte[] plainApdu = processingApdu.getPlainApdu();
		ArrayList<Field> templateFields = template.getFields();
//		 if (!fieldCountMatches(plainApdu, template.getFields().size())) {
//		 return false;
//		 }
		int fieldLength = DEFAULT_FIELDLENGTH;
		int offset = 0;
		for (int i = 0; i < templateFields.size(); i++) {
			if ((offset + fieldLength * encodingOffset) > plainApdu.length) {
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
			
			//TODO: Refactor
			int currentFieldOffset = offset;
			offset += fieldLength
					* (encodingOffset + whitespaceOffset);
			if (isLengthField(processingField)) {
				byte[] length = ByteArrays.trim(plainApdu, currentFieldOffset, fieldLength * encodingOffset);
				fieldLength = Integer.parseInt(new String(length), 16);
			} else {
				fieldLength = DEFAULT_FIELDLENGTH;
			}
		}
		System.out.println("template accepted " + template.getApduDescription());
		return true;
	}


	private boolean isIdentifierField(Field processingField) {
		return processingField.getValue() != null;
	}

	private boolean valueMatches(byte[] idByte, Field field) {
		String apduValue = new String(idByte);
		String templateValue = field.getValue();
		return apduValue.equals(templateValue);
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

			if (isLengthField(processingField)) {
				fieldLength = hexToInt(processingField.getValue());
			} else {
				fieldLength = DEFAULT_FIELDLENGTH;
			}
		}
		return true;
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

	private int hexToInt(String hexValue) {
		return Integer.parseInt(hexValue, 16);
	}

	private Field getCopyOf(Field field) {
		return new Field(field.getName(), field.getValue(),
				field.getDescription());
	}

	private void setApduDescription(ApduTemplate template) {
		processingApdu.setDescription(template.getApduDescription());
	}

	private boolean isLengthField(Field processingField) {
		return processingField.getName().equals("Lc");
	}

	@Override
	public void setProcessingApdu(Apdu apdu) {
		this.processingApdu = apdu;
	}


	public void setEncodingSettings(int encodingOffset, int whitespaceOffset) {
		this.encodingOffset = encodingOffset;
		this.whitespaceOffset = whitespaceOffset;
	}

}
