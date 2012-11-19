package ch.compass.gonzoproxy.relay.parser;

import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

import ch.compass.gonzoproxy.mvc.model.Apdu;
import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.utils.ByteArrays;

public class AsciiApduParser implements Parser {

	private static final int ENCODING_OFFSET = 3;

	private static final int DEFAULT_FIELDSIZE = 1;

	private Apdu processingApdu;

	@Override
	public boolean tryParse(ApduTemplate template) {
		List<Field> fields = template.getFields();
		byte[] plainApdu = processingApdu.getPlainApdu();
		// TODO: sizecheck fields & plainApdu, variable length of fields (works
		// just with fields with 1 byte)
		int fieldsize = DEFAULT_FIELDSIZE;
		int offset = 0;
		for (int i = 0; i < fields.size(); i++) {
			Field fieldToParse = fields.get(i);
			parseField(plainApdu, offset, fieldsize, fieldToParse);
			offset += DEFAULT_FIELDSIZE * ENCODING_OFFSET;
		}
		return true;
	}

	private void parseField(byte[] plainApdu, int offset, int fieldsize,
			Field fieldToParse) {
		if ((offset + fieldsize) < plainApdu.length) {
			byte[] value = ByteArrays.trim(plainApdu, offset, fieldsize);
			setFieldValue(fieldToParse, value);
			processingApdu.addField(fieldToParse);
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

	@Override
	public int getEncodingOffset() {
		return ENCODING_OFFSET;
	}

	@Override
	public int getDefaultFieldsize() {
		return DEFAULT_FIELDSIZE;
	}
	
	

}
