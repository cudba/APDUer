package ch.compass.gonzoproxy.relay.parser;

import java.util.List;

import ch.compass.gonzoproxy.mvc.model.Apdu;
import ch.compass.gonzoproxy.mvc.model.Field;

public class AsciiApduParser implements Parser {

	private static final int ENCODING_OFFSET = 2;
	private static final int WHITESPACE_OFFSET = 1;

	private Apdu processingApdu;

	@Override
	public boolean tryParse(ApduTemplate template) {
		List<Field> fields = template.getFields();
		byte[] plainApdu = processingApdu.getPlainApdu();
		// TODO: sizecheck fields & plainApdu, variable length of fields (works
		// just with fields with 1 byte)

		int offset = 0;
		for (int i = 0; i < fields.size(); i++) {
			Field fieldToParse = fields.get(i);
			parseValueToField(fieldToParse, plainApdu[offset],
					plainApdu[offset + 1]);
			processingApdu.addField(fieldToParse);
			offset += ENCODING_OFFSET + WHITESPACE_OFFSET;
		}
		return true;
	}

	private void parseValueToField(Field field, byte prefix, byte postfix) {
		byte[] value = new byte[] { prefix, postfix };
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
	public int getFormattingOffset() {
		return ENCODING_OFFSET + WHITESPACE_OFFSET;
	}

}
