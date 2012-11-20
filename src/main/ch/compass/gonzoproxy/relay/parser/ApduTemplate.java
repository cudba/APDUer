package ch.compass.gonzoproxy.relay.parser;

import java.util.ArrayList;

import ch.compass.gonzoproxy.mvc.model.Field;

public class ApduTemplate {

	private String apduDescription = "";
	private ArrayList<Field> fields = new ArrayList<Field>();
	
	
	public String getApduDescription() {
		return apduDescription;
	}
	
	public void setApduDescription(String apduDescription) {
		this.apduDescription = apduDescription;
	}

	public ArrayList<Field> getFields() {
		return fields;
	}

	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}

}
