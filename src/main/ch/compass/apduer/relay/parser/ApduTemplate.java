package ch.compass.apduer.relay.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import ch.compass.apduer.mvc.model.Field;

public class ApduTemplate {

	private static final int MAY_FIELD_COUNT = 6;

	private static final int BYTES_TO_IDENTIFY = 2;

	ArrayList<Field> fields = new ArrayList<Field>();

	private ArrayList<Field> identificationBytes = new ArrayList<Field>();

	public void loadTemplate(String fileName) {

		try (BufferedReader templateReader = new BufferedReader(new FileReader(
				fileName))) {

			String line = "";
			for (int i = 0; i < MAY_FIELD_COUNT; i++) {

				if ((line = templateReader.readLine()) != null) {

					String[] properties = line.split(",");
					
					// syntaxt in textfile must be name,value,description for each field
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
		byte[] plainApdu = parser.getPlainApdu();
		int offset = 0;
		for (int i = 0; i < identificationBytes.size(); i++) {
			byte[] idBytes = new byte[]{plainApdu[offset],plainApdu[offset + 1]};
			if(!identificationByteMatches(i, idBytes)) {
				return false;
			}
			offset +=2;
		}
		return parser.tryParse(this);
	}

	private boolean identificationByteMatches(int position, byte[] idBytes) {
		return identificationBytes.get(position).getValue().equals(new String(idBytes));
	}
	
	public ArrayList<Field> getFields() {
		return fields;
	}

}
