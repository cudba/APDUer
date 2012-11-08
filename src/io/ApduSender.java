package io;

public interface ApduSender {
	
	/**
	 * Writes APDU Command to Stream
	 * 
	 * @param apdu APDU command
	 */
	
	public void send(byte[] apdu);

}
