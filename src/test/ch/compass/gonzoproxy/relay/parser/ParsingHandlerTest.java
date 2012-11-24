package ch.compass.gonzoproxy.relay.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.compass.gonzoproxy.mvc.model.Packet;
import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.mvc.model.ParserSettings;

public class ParsingHandlerTest {
	ParsingHandler parserHanlder = new ParsingHandler(ParserSettings.LibNFC);

	@Test
	public void testProcessKnownLibNfcApdu() {

		String fakePlainApdu = "00 a4 04 00 07 d2 76 00 00 85 01 01 00";
		String libnfcInput = "C-APDU 000d: 00 a4 04 00 07 d2 76 00 00 85 01 01 00";
		Packet apdu = new Packet(libnfcInput.getBytes());
		apdu.setPlainPacket(fakePlainApdu.getBytes());

		parserHanlder.tryParse(apdu);

		StringBuilder mergedFields = new StringBuilder();

		for (Field field : apdu.getFields()) {
			mergedFields.append(field.getValue());
		}

		String atsDescription = "Case 4 Select Command";
		String trimmedApdu = "00a4040007d2 76 00 00 85 01 0100";
		assertEquals(atsDescription, apdu.getDescription());
		assertArrayEquals(fakePlainApdu.getBytes(), apdu.getPlainPacket());
		assertEquals(trimmedApdu, mergedFields.toString());

	}

	@Test
	public void testProcessKnownLibNfcApduCustomLength() {

		String fakePlainApdu = "00 a4 04 00 07 d2 76 00 00 85 01 00";
		String libnfcInput = "C-APDU 000c: 00 a4 04 00 07 d2 76 00 00 85 01 00";
		Packet apdu = new Packet(libnfcInput.getBytes());
		apdu.setPlainPacket(fakePlainApdu.getBytes());

		parserHanlder.tryParse(apdu);

		StringBuilder mergedFields = new StringBuilder();

		for (Field field : apdu.getFields()) {
			mergedFields.append(field.getValue());
		}

		String atsDescription = "Case 3 Select Command";
		String trimmedApdu = "00a4040007d2 76 00 00 85 01 00";
		assertEquals(atsDescription, apdu.getDescription());
		assertArrayEquals(fakePlainApdu.getBytes(), apdu.getPlainPacket());
		assertEquals(trimmedApdu, mergedFields.toString());

	}

	@Test
	public void testProcessUnknownLibNfcApdu() {

		String fakePlainApdu = "ff ff ff ff";
		String libnfcInput = "C-APDU 0004: ff ff ff ff";
		Packet apdu = new Packet(libnfcInput.getBytes());
		apdu.setPlainPacket(fakePlainApdu.getBytes());

		parserHanlder.tryParse(apdu);

		assertEquals(0, apdu.getFields().size());
		assertArrayEquals(fakePlainApdu.getBytes(), apdu.getPlainPacket());

	}

	@Test
	public void testProcessFourBytesContentIdentifier() {

		String fakePlainApdu = "77 07 82 00 07 94 76 00 0a 85";
		String libnfcInput = "#R-APDU 000a: 77 07 8200 07 94 76 00 0a 85";
		Packet apdu = new Packet(libnfcInput.getBytes());
		apdu.setPlainPacket(fakePlainApdu.getBytes());

		parserHanlder.tryParse(apdu);

		StringBuilder mergedFields = new StringBuilder();

		for (Field field : apdu.getFields()) {
			mergedFields.append(field.getValue());
		}

		String atsDescription = "Get Processing Options Response";
		String trimmedApdu = "77078200079476 00 0a85";
		assertEquals(atsDescription, apdu.getDescription());
		assertArrayEquals(fakePlainApdu.getBytes(), apdu.getPlainPacket());
		assertEquals(trimmedApdu, mergedFields.toString());

	}

	@Test
	public void testProcessSelectResponse() {

		String fakePlainApdu = "6f 2f 84 0e 32 50 41 59 2e 53 59 53 2e 44 44 46 30 31 a5 1d bf 0c 1a 61 18 4f 07 a0 00 00 00 04 10 10 87 01 01 50 0a 4d 61 73 74 65 72 43 61 72 64 90 00";
		String libnfcInput = "#R-APDU 0033: 6f 2f 84 0e 32 50 41 59 2e 53 59 53 2e 44 44 46 30 31 a5 1d bf 0c 1a 61 18 4f 07 a0 00 00 00 04 10 10 87 01 01 50 0a 4d 61 73 74 65 72 43 61 72 64 90 00";

		Packet apdu = new Packet(libnfcInput.getBytes());
		apdu.setPlainPacket(fakePlainApdu.getBytes());

		parserHanlder.tryParse(apdu);

		StringBuilder mergedFields = new StringBuilder();

		for (Field field : apdu.getFields()) {
			mergedFields.append(field.getValue());
		}

		String atsDescription = "Select Response";
		String trimmedApdu = "6f2f840e 32 50 41 59 2e 53 59 53 2e 44 44 46 30 31a51d bf 0c 1a 61 18 4f 07 a0 00 00 00 04 10 10 87 01 01 50 0a 4d 61 73 74 65 72 43 61 72 649000";
		assertEquals(atsDescription, apdu.getDescription());
		assertArrayEquals(fakePlainApdu.getBytes(), apdu.getPlainPacket());
		assertEquals(trimmedApdu, mergedFields.toString());

	}
	

//	@Test
//	public void testTest() {
//		byte[] bytes = { 0x0f, (byte) 0x80, (byte) 0xa8, 0x00, 0x70 };
//		StringBuilder sb = new StringBuilder();
//		for (byte b : bytes) {
//			sb.append(String.format("%02X ", b));
//		}
//	}

	
}
