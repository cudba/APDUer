package ch.compass.gonzoproxy.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class ByteArraysTest {

	@Test
	public void testTrimLengthValid() {
		byte[] testArray = new byte[]{'A','B','C','D'};
		byte[] expectedArray = new byte[]{'A','B'};
		
		int fromIndex = 0;
		int length = 2;
		assertArrayEquals(expectedArray, ByteArrays.trim(testArray, fromIndex, length));
	}
	
	@Test
	public void testTrimLengthTooBig()	{
		byte[] testArray = new byte[]{'A','B','C','D'};
		
		int fromIndex = 0;
		int length = testArray.length + 1;
		assertArrayEquals(testArray, ByteArrays.trim(testArray, fromIndex, length));
		
	}
	
	@Test
	public void testTrimLengthNegative() {
		byte[] testArray = new byte[]{'A','B','C','D'};
		
		int fromIndex = 0;
		int length = -1;
		assertArrayEquals(testArray, ByteArrays.trim(testArray, fromIndex, length));
	}
	
	@Test
	public void testGetDelimiterIndices() {
		ArrayList<Integer> expectedIndices = new ArrayList<Integer>();
		expectedIndices.add(0);
		expectedIndices.add(2);
		expectedIndices.add(4);
		
		char delimiter = '#';
		
		byte[] testArray = new byte[]{'#', 'A', '#', 'B', '#', 'C'};
		ArrayList<Integer> indices = ByteArrays.getDelimiterIndices(testArray, delimiter);
		assertEquals(expectedIndices.get(0), indices.get(0));
	}
}
