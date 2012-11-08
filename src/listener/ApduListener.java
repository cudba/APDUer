package listener;

public interface ApduListener {

	public void commandReceived();
	
	public void responseReceived();
	
}
