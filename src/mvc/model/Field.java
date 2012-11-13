package mvc.model;

public class Field {
	
	private String name;
	private byte value;
	private String description;

	public Field(String name, byte value, String description) {
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

	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
