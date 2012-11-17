package ch.compass.apduer.relay.parser;


import java.util.List;

import ch.compass.apduer.mvc.model.Apdu;
import ch.compass.apduer.mvc.model.Field;

public class ApduParser implements Parser {
	
	private Apdu apdu;
	private byte[] plainApdu;

	public ApduParser(Apdu apdu) {
		this.apdu = apdu;
		this.plainApdu = apdu.getPlainApdu();
		
	}

	@Override
	public byte[] getPlainApdu() {
		return plainApdu;
	}

	@Override
	public boolean tryParse(ApduTemplate template) {
		List<Field> fields = template.getFields();
		
		//TODO: sizecheck fields & plainApdu, variable length of (works just with fieldsw with 2bytes)
		
		int offset = 0;
		for (int i = 0; i < fields.size(); i++) {
			Field fieldToParse = fields.get(i);
			parseValueToField(fieldToParse, plainApdu[offset], plainApdu[offset + 1]);
			apdu.addField(fieldToParse);
			offset += 2;
		}
		return false;
	}

	private void parseValueToField(Field field, byte b, byte c) {
		byte[] value = new byte[] {b, c};
		field.setValue(new String(value));
	}
	

}
