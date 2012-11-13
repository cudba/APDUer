package ch.compass.apduer.mvc.listener;

import ch.compass.apduer.mvc.model.Apdu;


public interface ApduListener {

	public void apduReceived(Apdu apdu);

}
