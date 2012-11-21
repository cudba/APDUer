package ch.compass.gonzoproxy.mvc.model;

public enum SessionFormat {
	LibNFC(2,1);
	
	private int encodingOffset;
	private int whitespaceOffset;
	
	SessionFormat(int encodingOffset, int whitespaceOffset){
		this.encodingOffset = encodingOffset;
		this.whitespaceOffset = whitespaceOffset;
		
	}
	
	public int getEncodingOffset() {
		return encodingOffset;
	}
	
	public int getWhitespaceOffset() {
		return whitespaceOffset;
	}
}
