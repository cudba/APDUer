package ch.compass.gonzoproxy.mvc.model;

import java.io.Serializable;

public class Field implements Serializable{


	private static final long serialVersionUID = -6724126085297330455L;

	private String name;
	private String value;
	private String description;

	public Field() {
		
	}
	
	public Field(String name, String value, String description) {
		this.name = name;
		this.value = value;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
