package ch.compass.gonzoproxy.relay.parser;

import java.util.List;

import ch.compass.gonzoproxy.mvc.model.Apdu;
import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.utils.ByteArrays;

public class AsciiApduParser implements Parser {

	private static final int BYTES_TO_IDENTIFY = 1;

	private static final int ENCODING_OFFSET = 2;
	
	private static final int WHITESPACE_OFFSET = 1;

	private static final int DEFAULT_FIELDLENGTH = 1;

	private Apdu processingApdu;

	public boolean templateIsAccepted(ApduTemplate template) {
		byte[] plainApdu = processingApdu.getPlainApdu();
		if (!containsIdBytes(plainApdu)) {
			return false;
		}
		int offset = 0;
		for (int i = 0; i < BYTES_TO_IDENTIFY; i++) {
			byte[] idByte = new byte[] { plainApdu[offset],
					plainApdu[offset + DEFAULT_FIELDLENGTH] };
			if (!valueMatches(idByte, template.getFields().get(i))) {
				return false;
			}
			offset += DEFAULT_FIELDLENGTH * (ENCODING_OFFSET + WHITESPACE_OFFSET);
		}
		return true;
	}

	private boolean valueMatches(byte[] idByte, Field field) {
		String apduValue = new String(idByte);
		String templateValue = field.getValue();
		return apduValue.equals(templateValue);
	}

	private boolean containsIdBytes(byte[] plainApdu) {

		return plainApdu.length >= BYTES_TO_IDENTIFY * (ENCODING_OFFSET + WHITESPACE_OFFSET);
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
			parseField(plainApdu, offset, fieldLength * ENCODING_OFFSET, processingField);
			if (isLengthField(processingField)) {
				fieldLength = Integer.parseInt(processingField.getValue(), 16);
			} else {
				fieldLength = DEFAULT_FIELDLENGTH;
			}
			offset += fieldLength * (ENCODING_OFFSET + WHITESPACE_OFFSET);
		}
		return true;
	}

	private Field getCopyOf(Field field) {
		return new Field(field.getName(), field.getValue(), field.getDescription());
	}

	private void setApduDescription(ApduTemplate template) {
		processingApdu.setDescription(template.getApduDescription());
	}

	private boolean isLengthField(Field processingField) {
		return processingField.getName().equals("LEN");
	}

	private void parseField(byte[] plainApdu, int offset, int fieldsize,
			Field field) {
		if ((offset + fieldsize) <= plainApdu.length) {
			byte[] value = ByteArrays.trim(plainApdu, offset, fieldsize);
			setFieldValue(field, value);
			processingApdu.addField(field);
		}
	}

	private void setFieldValue(Field field, byte[] value) {
		field.setValue(new String(value));
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
