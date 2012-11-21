package ch.compass.gonzoproxy.relay.io;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

import org.junit.Test;

import ch.compass.gonzoproxy.mvc.model.Apdu;
import static org.junit.Assert.*;

public class ApduStreamHandlerTest {

	@Test
	public void readApduTest() throws IOException{
		ApduStreamHandler streamHandler = new ApduStreamHandler(new LibNfcApduExtractor(), new LibNfcApduWrapper());
		
		byte[] inputStream = "#C-APDU 000d: 00 a4 04 00 07 d2 76 00 00 85 01 01 00 \n".getBytes();
		byte[] preamle = "#C-APDU 000d: ".getBytes();
		byte[] trailer = " \n".getBytes();
		
		InputStream in = new ByteArrayInputStream(inputStream);
		
		Queue<Apdu> queue = streamHandler.readApdu(in);
		Apdu apdu = queue.poll();
		assertArrayEquals(inputStream, apdu.getOriginalApdu());
		assertArrayEquals(preamle, apdu.getPreamble());
		assertArrayEquals(trailer, apdu.getTrailer());
		
		
	
	}
}
