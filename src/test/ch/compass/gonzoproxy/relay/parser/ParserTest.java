package ch.compass.gonzoproxy.relay.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.compass.gonzoproxy.mvc.model.Apdu;

public class ParserTest {

	@Test
	public void test() {
		ParsingHandler parserHanlder = new ParsingHandler();
		byte[] originalApdu = "ATS 0009: 78 00 80 02 20 63 cb a1 80".getBytes();
		byte[] plainApdu = "78 00 80 02 20 63 cb a1 80".getBytes();
		Apdu apdu = new Apdu(originalApdu);
		apdu.setPlainApdu(plainApdu);
		
		parserHanlder.processApdu(apdu);
		assertEquals("T1", apdu.getFields().get(0).getName());
	}
	

}
