package ch.compass.gonzoproxy.relay.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.compass.gonzoproxy.mvc.model.Package;
import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.mvc.model.SessionFormat;

public class ParsingHandlerTest {

	@Test
	public void testProcessKnownLibNfcApdu() {
		ParsingHandler parserHanlder = new ParsingHandler(SessionFormat.LibNFC);

		String fakePlainApdu = "00 a4 04 00 07 d2 76 00 00 85 01 01 00";
		String libnfcInput = "C-APDU 000d: 00 a4 04 00 07 d2 76 00 00 85 01 01 00";
		Package apdu = new Package(libnfcInput.getBytes());
		apdu.setPlainPackage(fakePlainApdu.getBytes());

		parserHanlder.tryParse(apdu);

		StringBuilder mergedFields = new StringBuilder();

		for (Field field : apdu.getFields()) {
			mergedFields.append(field.getValue());
		}
		
		String atsDescription = "Case 4 Select Command";
		String trimmedApdu = "00a4040007d2 76 00 00 85 01 0100";
		assertEquals(atsDescription, apdu.getDescription());
		assertArrayEquals(fakePlainApdu.getBytes(), apdu.getPlainPackage());
		assertEquals(trimmedApdu, mergedFields.toString());

	}
	
	@Test
	public void testProcessKnownLibNfcApduCustomLength() {
		ParsingHandler parserHanlder = new ParsingHandler(SessionFormat.LibNFC);
		
		String fakePlainApdu = "00 a4 04 00 07 d2 76 00 00 85 01 00";
		String libnfcInput = "C-APDU 000b: 00 a4 04 00 07 d2 76 00 00 85 01 00";
		Package apdu = new Package(libnfcInput.getBytes());
		apdu.setPlainPackage(fakePlainApdu.getBytes());

		parserHanlder.tryParse(apdu);

		StringBuilder mergedFields = new StringBuilder();

		for (Field field : apdu.getFields()) {
			mergedFields.append(field.getValue());
		}
		
		String atsDescription = "Case 3 Select Command";
		String trimmedApdu = "00a4040007d2 76 00 00 85 01 00";
		assertEquals(atsDescription, apdu.getDescription());
		assertArrayEquals(fakePlainApdu.getBytes(), apdu.getPlainPackage());
		assertEquals(trimmedApdu, mergedFields.toString());

	}

	@Test
	public void testProcessUnknownLibNfcApdu() {
		ParsingHandler parserHanlder = new ParsingHandler(SessionFormat.LibNFC);
		
		String fakePlainApdu = "ff ff ff ff";
		String libnfcInput = "C-APDU 0004: ff ff ff ff";
		Package apdu = new Package(libnfcInput.getBytes());
		apdu.setPlainPackage(fakePlainApdu.getBytes());

		parserHanlder.tryParse(apdu);

		assertEquals(0, apdu.getFields().size());
		assertArrayEquals(fakePlainApdu.getBytes(), apdu.getPlainPackage());

	}
	
	@Test
	public void testProcessKnownLibNfcApduCustomLengthResponse() {
		ParsingHandler parserHanlder = new ParsingHandler(SessionFormat.LibNFC);
		
		String fakePlainApdu = "77 07 82 00 07 94 76 00 0a 85";
		String libnfcInput = "#R-APDU 000a: 77 07 82 00 07 94 76 00 03 85";
		Package apdu = new Package(libnfcInput.getBytes());
		apdu.setPlainPackage(fakePlainApdu.getBytes());

		parserHanlder.tryParse(apdu);

		StringBuilder mergedFields = new StringBuilder();

		for (Field field : apdu.getFields()) {
			mergedFields.append(field.getValue());
		}
		
		String atsDescription = "Get Processing Options Response";
		String trimmedApdu = "77078200 079476 00 0a85";
		assertEquals(atsDescription, apdu.getDescription());
		assertArrayEquals(fakePlainApdu.getBytes(), apdu.getPlainPackage());
		assertEquals(trimmedApdu, mergedFields.toString());

	}

}
