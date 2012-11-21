package ch.compass.gonzoproxy.relay.parser;

import java.util.ArrayList;
import java.util.List;

import ch.compass.gonzoproxy.mvc.model.Apdu;
import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.utils.ByteArrays;

public class AsciiApduParser implements Parser {

	private static final int ENCODING_OFFSET = 2;

	private static final int WHITESPACE_OFFSET = 1;

	private static final int DEFAULT_FIELDLENGTH = 1;

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
			if ((offset + fieldLength * ENCODING_OFFSET) > plainApdu.length) {
				return false;
			}

			Field processingField = template.getFields().get(i);
			if (isIdentifierField(processingField)) {
				byte[] idByte;
				if (hasCustomLenght(fieldLength)) {
					idByte = ByteArrays.trim(plainApdu, offset, fieldLength
							* (ENCODING_OFFSET + WHITESPACE_OFFSET)
							- WHITESPACE_OFFSET);
				} else {
					idByte = ByteArrays.trim(plainApdu, offset, fieldLength
							* ENCODING_OFFSET);
				}
				if (!valueMatches(idByte, templateFields.get(i))) {
					return false;
				}
				System.out.println("Field matched");
			}
			
			//TODO: Refactor
			int currentFieldOffset = offset;
			offset += fieldLength
					* (ENCODING_OFFSET + WHITESPACE_OFFSET);
			if (isLengthField(processingField)) {
				byte[] length = ByteArrays.trim(plainApdu, currentFieldOffset, fieldLength * ENCODING_OFFSET);
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
						* (ENCODING_OFFSET + WHITESPACE_OFFSET)
						- WHITESPACE_OFFSET, processingField);
			} else {
				parseValueToField(plainApdu, offset, fieldLength
						* ENCODING_OFFSET, processingField);
			}

			offset += fieldLength * (ENCODING_OFFSET + WHITESPACE_OFFSET);

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

	@Override
	public Apdu getProcessingApdu() {
		return processingApdu;
	}

}
