package ch.compass.gonzoproxy.relay.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.compass.gonzoproxy.mvc.model.Packet;
import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.mvc.model.SessionFormat;

public class ParsingHandlerTest {
	ParsingHandler parserHanlder = new ParsingHandler(SessionFormat.LibNFC);

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
	public void testProcessKnownLibNfcApduCustomLengthResponse() {
		
		String fakePlainApdu = "77 07 82 00 07 94 76 00 0a 85";
		String libnfcInput = "#R-APDU 000a: 77 07 82 00 07 94 76 00 0a 85";
		Packet apdu = new Packet(libnfcInput.getBytes());
		apdu.setPlainPacket(fakePlainApdu.getBytes());

		parserHanlder.tryParse(apdu);

		StringBuilder mergedFields = new StringBuilder();

		for (Field field : apdu.getFields()) {
			mergedFields.append(field.getValue());
		}
		
		String atsDescription = "Get Processing Options Response";
		String trimmedApdu = "77078200 079476 00 0a85";
		assertEquals(atsDescription, apdu.getDescription());
		assertArrayEquals(fakePlainApdu.getBytes(), apdu.getPlainPacket());
		assertEquals(trimmedApdu, mergedFields.toString());

	}
	
	@Test
	public void testTest(){
		byte[] bytes = {0x0f, (byte) 0x80, (byte) 0xa8 , 0x00, 0x70 };
	    StringBuilder sb = new StringBuilder();
	    for (byte b : bytes) {
	        sb.append(String.format("%02X ", b));
	    }
	    System.out.println(sb.toString());
	}
	
//	@Test
//	public void testProcessKnownLibNfcApduCustomIdentifierLengthResponse() {
//		ParsingHandler parserHanlder = new ParsingHandler(SessionFormat.LibNFC);
//		
//		String fakePlainApdu = "77 07 84 00 07 94 76 00 0a 85";
//		String libnfcInput = "#R-APDU 000a: 77 07 82 00 07 94 76 00 03 85";
//		Packet apdu = new Packet(libnfcInput.getBytes());
//		apdu.setPlainPacket(fakePlainApdu.getBytes());
//
//		parserHanlder.tryParse(apdu);
//
//		StringBuilder mergedFields = new StringBuilder();
//
//		for (Field field : apdu.getFields()) {
//			mergedFields.append(field.getValue());
//		}
//		
//		String atsDescription = "Get Processing Options Response";
//		String trimmedApdu = "77078200 079476 00 0a85";
//		assertEquals(atsDescription, apdu.getDescription());
//		assertArrayEquals(fakePlainApdu.getBytes(), apdu.getPlainPacket());
//		assertEquals(trimmedApdu, mergedFields.toString());
//
//	}

}
