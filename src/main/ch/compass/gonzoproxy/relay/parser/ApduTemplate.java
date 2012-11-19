package ch.compass.gonzoproxy.relay.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.utils.ByteArrays;

public class ApduTemplate {

	private static final int MAX_FIELD_COUNT = 9;

	private static final int BYTES_TO_IDENTIFY = 1;

	ArrayList<Field> fields = new ArrayList<Field>();

	private ArrayList<Field> identificationBytes = new ArrayList<Field>();

	public ApduTemplate(String fileName) {
		loadTemplate(fileName);

	}

	private void loadTemplate(String fileName) {

		try (BufferedReader templateReader = new BufferedReader(new FileReader(
				fileName))) {

			String line = "";
			for (int i = 0; i < MAX_FIELD_COUNT; i++) {

				if ((line = templateReader.readLine()) != null) {

					String[] properties = line.split(",");

					// syntaxt in textfile must be name,value,description for
					// each field
					if (properties.length == 3) {
						Field flag = new Field(properties[0], properties[1],
								properties[2]);
						fields.add(flag);
					} else {
						throw new IOException("syntax error at field " + i);
					}

				}
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		setIdentifierValues();
	}

	private void setIdentifierValues() {
		for (int i = 0; i < BYTES_TO_IDENTIFY; i++) {
			identificationBytes.add(fields.get(i));
		}
	}

	public boolean accept(Parser parser) {
		byte[] plainApdu = parser.getProcessingApdu().getPlainApdu();
		int fieldsize = parser.getDefaultFieldsize();
		int offset = 0;
		for (int i = 0; i < identificationBytes.size(); i++) {
			byte[] idBytes = ByteArrays.trim(plainApdu, offset, offset + fieldsize);
			
			if (!identificationByteMatches(i, idBytes)) {
				return false;
			}
			offset += parser.getEncodingOffset();
		}
		return parser.tryParse(this);
	}

	private boolean identificationByteMatches(int position, byte[] idBytes) {
		return identificationBytes.get(position).getValue()
				.equals(new String(idBytes));
	}

	public ArrayList<Field> getFields() {
		return fields;
	}

}
