package ch.compass.gonzoproxy.relay.io;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

import org.junit.Test;

import ch.compass.gonzoproxy.mvc.model.Package;
import static org.junit.Assert.*;

public class ApduStreamHandlerTest {

	@Test
	public void readApduTest() throws IOException{
		ApduStreamHandler streamHandler = new ApduStreamHandler(new LibNfcApduExtractor(), new LibNfcApduWrapper());
		
		byte[] inputStream = "#C-APDU 000d: 00 a4 04 00 07 d2 76 00 00 85 01 01 00 \n#C-APDU 000d: 00 a4 04 00 07 d2 76 00 00 85 01 01 00 \n".getBytes();
		byte[] originalApduFake = "#C-APDU 000d: 00 a4 04 00 07 d2 76 00 00 85 01 01 00 \n".getBytes();
		byte[] preamleFake = "#C-APDU 000d: ".getBytes();
		byte[] plainApduFake = "00 a4 04 00 07 d2 76 00 00 85 01 01 00".getBytes();
		byte[] trailerFake = " \n".getBytes();
		
		InputStream in = new ByteArrayInputStream(inputStream);
		
		Queue<Package> queue = streamHandler.readApdu(in);
		Package apdu = queue.poll();
		assertArrayEquals(originalApduFake, apdu.getStreamInput());
		assertArrayEquals(plainApduFake, apdu.getPlainPackage());
		assertArrayEquals(preamleFake, apdu.getPreamble());
		assertArrayEquals(trailerFake, apdu.getTrailer());
		
		
	
	}
}
