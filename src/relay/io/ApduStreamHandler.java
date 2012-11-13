package relay.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class ApduStreamHandler {

	private char delimiter;

	public ApduStreamHandler(char delimiter) {
		this.delimiter = delimiter;

	}

	public byte[] readStream(InputStream inputStream) {
		byte[] apdu = new byte[0];
		byte[] buffer = new byte[4096];

		BufferedInputStream in = new BufferedInputStream(inputStream);
		try {
			while (in.read(buffer, 0, buffer.length) != -1) {
				int inputSize = getDelimiterIndex(buffer);
				if (inputSize >= 0) {
					apdu = copyBufferToApdu(buffer, apdu, inputSize);
					return apdu;
				} else {
					// TODO: Bufferoverflow oder so :>
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

	public void sendStream(OutputStream outputstream, byte[] apdu) {
		byte[] buffer = apdu;
		BufferedOutputStream writer = new BufferedOutputStream(outputstream);
		try {
			writer.write(buffer);
			writer.write(delimiter);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param buffer
	 *            contains read data
	 * @param apdu
	 *            represents the apdu command / response
	 * @return new array with merged data, value of buffer is added to apdu
	 */
	private byte[] copyBufferToApdu(byte[] buffer, byte[] apdu, int inputSize) {
		byte[] tmp = Arrays.copyOf(apdu, apdu.length + inputSize);
		System.arraycopy(buffer, 0, tmp, apdu.length, inputSize);
		return tmp;
	}

	private int getDelimiterIndex(byte[] buf) {
		for (int i = 0; i < buf.length; i++) {
			if (buf[i] == delimiter) {
				return i;
			}
		}
		return -1;
	}

}
