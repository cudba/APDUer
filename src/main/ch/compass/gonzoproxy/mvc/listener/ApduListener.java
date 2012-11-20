package ch.compass.gonzoproxy.mvc.listener;

import ch.compass.gonzoproxy.mvc.model.Apdu;


public interface ApduListener {

	public void apduReceived(Apdu apdu);

}
