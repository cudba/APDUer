//package ch.compass.gonzoproxy.relay.parser;
//
//import static org.junit.Assert.assertArrayEquals;
//import static org.junit.Assert.assertEquals;
//
//import org.junit.Test;
//
//import ch.compass.gonzoproxy.mvc.model.Apdu;
//import ch.compass.gonzoproxy.mvc.model.Field;
//
//public class ApduAnalyzerTest {
//
//
//	@Test
//	public void testProcessKnownLibNfcApdu() {
//		ApduAnalyzer parserHanlder = new ApduAnalyzer();
//
//		String fakePlainApdu = "00 a4 04 00 07 d2 76 00 00 85 01 01 00";
//		String libnfcInput = "C-APDU 000d: 00 a4 04 00 07 d2 76 00 00 85 01 01 00";
//		Apdu apdu = new Apdu(libnfcInput.getBytes());
//		apdu.setPlainApdu(fakePlainApdu.getBytes());
//
//		parserHanlder.processApdu(apdu);
//
//		StringBuilder mergedFields = new StringBuilder();
//
//		for (Field field : apdu.getFields()) {
//			mergedFields.append(field.getValue());
//		}
//		
//		String atsDescription = "Case 4 Select Command";
//		String trimmedApdu = "00a4040007d2 76 00 00 85 01 0100";
//		assertEquals(atsDescription, apdu.getDescription());
//		assertArrayEquals(fakePlainApdu.getBytes(), apdu.getPlainApdu());
//		assertEquals(trimmedApdu, mergedFields.toString());
//
//	}
//	
//	@Test
//	public void testProcessKnownLibNfcApduCustomLength() {
//		ApduAnalyzer parserHanlder = new ApduAnalyzer();
//
//		String fakePlainApdu = "00 a4 04 00 07 d2 76 00 00 85 01 00";
//		String libnfcInput = "C-APDU 000d: 00 a4 04 00 07 d2 76 00 00 85 01 00";
//		Apdu apdu = new Apdu(libnfcInput.getBytes());
//		apdu.setPlainApdu(fakePlainApdu.getBytes());
//
//		parserHanlder.processApdu(apdu);
//
//		StringBuilder mergedFields = new StringBuilder();
//
//		for (Field field : apdu.getFields()) {
//			mergedFields.append(field.getValue());
//		}
//		
//		String atsDescription = "Case 3 Select Command";
//		String trimmedApdu = "00a4040007d2 76 00 00 85 01 00";
//		assertEquals(atsDescription, apdu.getDescription());
//		assertArrayEquals(fakePlainApdu.getBytes(), apdu.getPlainApdu());
//		assertEquals(trimmedApdu, mergedFields.toString());
//
//	}
//
//	@Test
//	public void testProcessUnknownLibNfcApdu() {
//		ApduAnalyzer parserHanlder = new ApduAnalyzer();
//
//		String fakePlainApdu = "ff ff ff ff";
//		String libnfcInput = "C-APDU 0004: ff ff ff ff";
//		Apdu apdu = new Apdu(libnfcInput.getBytes());
//		apdu.setPlainApdu(fakePlainApdu.getBytes());
//
//		parserHanlder.processApdu(apdu);
//
//		assertEquals(0, apdu.getFields().size());
//		assertArrayEquals(fakePlainApdu.getBytes(), apdu.getPlainApdu());
//
//	}
//
//}
