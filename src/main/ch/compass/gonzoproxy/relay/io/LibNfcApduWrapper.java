package ch.compass.gonzoproxy.relay.io;

import java.util.Arrays;

import ch.compass.gonzoproxy.mvc.model.Apdu;

public class LibNfcApduWrapper {
	
	private byte[] trailer;
	private byte[] plainApdu;
	private byte[] preamble;

	public byte[] wrap(Apdu apdu) {
		this.trailer = apdu.getTrailer();
		this.plainApdu = apdu.getPlainApdu();
		this.preamble = computePreamle(apdu);
		this.preamble = apdu.getPreamble();
		
		int newSize = preamble.length + plainApdu.length + trailer.length;
		
		byte[] wrappedApdu = Arrays.copyOf(preamble, newSize);
		System.arraycopy(plainApdu, 0, wrappedApdu, preamble.length, plainApdu.length);
		System.arraycopy(trailer, 0, wrappedApdu, preamble.length + plainApdu.length, trailer.length);
		
		
		return wrappedApdu;
	}

	private byte[] computePreamle(Apdu apdu) {
		byte[] origPreamble = apdu.getPreamble();
		byte[] newPreamble = new byte[origPreamble.length];
		
		int apduSize = apdu.getSize();
		byte[] sizeBytes = new byte[4];
		
		
		return newPreamble;
	}

}
