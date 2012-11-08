package listener;

import model.Apdu;

public interface ApduListener {

	public void commandReceived(Apdu commandApdu);
	
	public void responseReceived(Apdu responseApdu);
	
}
