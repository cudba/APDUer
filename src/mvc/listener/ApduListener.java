package mvc.listener;

import java.util.ArrayList;

import mvc.model.Apdu;

public interface ApduListener {

	public void apduReceived(Apdu apdu);

}
