package relay.io;

public interface ApduReader {
	
	/**
	 * reads APDU command from stream
	 * 
	 * @return byte Array which contains plain APDU
	 */

	public byte[] readApduCommand(); 
}
