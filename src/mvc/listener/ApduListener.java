package mvc.listener;

import mvc.model.Apdu;

public interface ApduListener {

	public void apduReceived(Apdu apdu);
	
}
