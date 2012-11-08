package mvc.listener;

import mvc.model.Apdu;

public interface ApduListener {

	public void commandReceived(Apdu commandApdu);
	
	public void responseReceived(Apdu responseApdu);
	
}
