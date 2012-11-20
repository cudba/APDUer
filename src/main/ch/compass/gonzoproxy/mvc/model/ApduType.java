package ch.compass.gonzoproxy.mvc.model;

public enum ApduType {
	COMMAND("COM"), 
	RESPONSE("RES");
	
	private String id;
	private ApduType(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
}
