package ch.compass.gonzoproxy.relay.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

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

		String fakePlainApdu = "00 a4 04 00 07 d2 76 00 00 85 01 01 00";
		String libnfcInput = "C-APDU 000d: 00 a4 04 00 07 d2 76 00 00 85 01 01 00";
		Apdu apdu = new Apdu(libnfcInput.getBytes());
		apdu.setPlainApdu(fakePlainApdu.getBytes());

		parserHanlder.processApdu(apdu);

		assertEquals(0, apdu.getFields().size());
		assertArrayEquals(fakePlainApdu.getBytes(), apdu.getPlainApdu());

	}

}
