package relay.io;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ApduStreamHandler {

	private static final int BUFFER_SIZE = 1024;
	private char delimiter;

	public ApduStreamHandler(char delimiter) {
		this.delimiter = delimiter;

	}

	public byte[] readApdu(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];

		int length = 0;
		int readBytes = 0;
		while ((readBytes = inputStream.read(buffer, length, buffer.length - length)) != -1) {
			length += readBytes;
			if (buffer[length - 1] == delimiter)
				return trim(buffer, length);

			if (buffer.length == length)
				buffer = enlarge(buffer);
		}

		throw new IOException("Stream closed");
	}

	public void sendApdu(OutputStream outputStream, byte[] apdu) {
		byte[] buffer = apdu;
		try {
			outputStream.write(buffer);
			outputStream.write(delimiter);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private byte[] enlarge(byte[] array) {
		byte[] newArray = new byte[array.length << 1];
		System.arraycopy(array, 0, newArray, 0, array.length);
		return newArray;
	}

	private byte[] trim(byte[] array, int length) {
		byte[] newArray = new byte[length];
		System.arraycopy(array, 0, newArray, 0, length);
		return newArray;
	}
}
