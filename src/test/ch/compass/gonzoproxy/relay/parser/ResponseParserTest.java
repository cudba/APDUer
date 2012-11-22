package ch.compass.gonzoproxy.relay.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.compass.gonzoproxy.mvc.model.Apdu;
import ch.compass.gonzoproxy.mvc.model.Field;

public class ResponseParserTest {

	@Test
	public void sinleIdentifierTemplateAcceptedTest() {
		ResponseParser parser = new ResponseParser();
		
		String processingApduFake = "00 a4 04 00 07 d2 76 00 00 85 01 01 00";
		String libnfcInput = "C-APDU 000d: 00 a4 04 00 07 d2 76 00 00 85 01 01 00";
		Apdu apdu = new Apdu(libnfcInput.getBytes());
		apdu.setPlainApdu(processingApduFake.getBytes());
		
		ApduTemplate templateFake = new ApduTemplate();
		templateFake.getFields().add(new Field("testFieldName", "O0", "testDescription"));
		
		assertTrue(parser.templateIsAccepted(templateFake));
	}

}
