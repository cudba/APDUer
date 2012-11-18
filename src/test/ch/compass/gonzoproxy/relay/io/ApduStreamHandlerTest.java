package ch.compass.gonzoproxy.relay.io;
//package ch.compass.apduer.relay.io;
//import static org.junit.Assert.*;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//
//import org.junit.Test;
//
//
//public class ApduStreamHandlerTest {
//
//	@Test
//	public void testEnlargeArray() throws NoSuchMethodException,
//			SecurityException, IllegalAccessException,
//			IllegalArgumentException, InvocationTargetException {
//		ApduStreamHandler streamHandler = new ApduStreamHandler('#');
//		Method enlargeMethod = ApduStreamHandler.class.getDeclaredMethod(
//				"enlarge", byte[].class);
//		enlargeMethod.setAccessible(true);
//
//		int initialSize = 10;
//		int expectedSize = initialSize * 2;
//		byte[] testArray = new byte[initialSize];
//		byte[] enlargedArray = (byte[]) enlargeMethod.invoke(streamHandler,
//				testArray);
//		assertEquals(expectedSize, enlargedArray.length);
//	}
//
//	@Test
//	public void testTrimArray() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//		ApduStreamHandler streamHandler = new ApduStreamHandler('#');
//
//		Method trimMethod = ApduStreamHandler.class.getDeclaredMethod("trim",
//				byte[].class, int.class);
//		trimMethod.setAccessible(true);
//
//		int initialSize = 20;
//		int trimToSize = 10;
//		byte[] testArray = new byte[initialSize];
//		byte[] trimmedArray = (byte[]) trimMethod.invoke(streamHandler, testArray, trimToSize);
//		assertEquals(trimToSize, trimmedArray.length);
//	}
//}
