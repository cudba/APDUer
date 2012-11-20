package ch.compass.gonzoproxy.relay.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.compass.gonzoproxy.mvc.model.Apdu;
import ch.compass.gonzoproxy.mvc.model.Field;

public class ApduAnalyzerTest {


	@Test
	public void testProcessKnownLibNfcApdu() {
		ApduAnalyzer parserHanlder = new ApduAnalyzer();

		String fakePlainApdu = "78 00 80 02 20 63 cb a1 80";
		String libnfcInput = "ATS 0009: 78 00 80 02 20 63 cb a1 80";
		Apdu apdu = new Apdu(libnfcInput.getBytes());
		apdu.setPlainApdu(fakePlainApdu.getBytes());

		parserHanlder.processApdu(apdu);

		StringBuilder mergedFields = new StringBuilder();

		for (Field field : apdu.getFields()) {
			mergedFields.append(field.getValue());
		}
		
		String atsDescription = "Answer To Select";
		String trimmedApdu = "780080022063cba180";
		assertEquals(atsDescription, apdu.getDescription());
		assertArrayEquals(fakePlainApdu.getBytes(), apdu.getPlainApdu());
		assertEquals(trimmedApdu, mergedFields.toString());

	}

	@Test
	public void testProcessUnknownLibNfcApdu() {
		ApduAnalyzer parserHanlder = new ApduAnalyzer();

		String fakePlainApdu = "ff ff ff ff";
		String libnfcInput = "C-APDU 0004: ff ff ff ff";
		Apdu apdu = new Apdu(libnfcInput.getBytes());
		apdu.setPlainApdu(fakePlainApdu.getBytes());

		parserHanlder.processApdu(apdu);

		assertEquals(0, apdu.getFields().size());
		assertArrayEquals(fakePlainApdu.getBytes(), apdu.getPlainApdu());

	}

}
