package de.persosim.simulator.tlv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.persosim.simulator.exception.ISO7816Exception;
import de.persosim.simulator.utils.Utils;

public class TlvLengthTest {
	
	/**
	 * Positive test case: Extract 1 byte length from a range out of a larger
	 * byte array. The range is smaller than the length of the byte array and
	 * larger than the minimum range required for the respective length
	 */
	@Test
	public void testConstructor_1ByteLengthLargeRange() {
		/* set arbitrary but valid 1-byte length */
		byte[] lengthExpected = new byte[] { (byte) 0x0A };

		/* dummy data to prepend */
		byte[] byteArrayPre = new byte[] { (byte) 0xFF, (byte) 0xFF };
		/* dummy data to append */
		byte[] byteArrayPost = new byte[] { (byte) 0xFF, (byte) 0xFF,
				(byte) 0xFF, (byte) 0xFF };

		/* build actual byte array */
		byte[] byteArray = Utils.concatByteArrays(byteArrayPre, lengthExpected,
				byteArrayPost);

		TlvLength lengthExtracted = new TlvLength(byteArray,
				byteArrayPre.length, byteArray.length);
		assertArrayEquals("Equals expected byte array representation",
				lengthExtracted.toByteArray(), lengthExpected);
	}
	
	/**
	 * Positive test case: Extract 1 byte length from a range out of a larger
	 * byte array. The range is smaller than the length of the byte array and
	 * matches the minimum range required for the respective length
	 */
	@Test
	public void testConstructor_1ByteLengthMinRange() {
		/* set arbitrary but valid 1-byte length */
		byte[] lengthExpected = new byte[] { (byte) 0x0A };

		/* dummy data to prepend */
		byte[] byteArrayPre = new byte[] { (byte) 0xFF, (byte) 0xFF };
		/* dummy data to append */
		byte[] byteArrayPost = new byte[] { (byte) 0xFF, (byte) 0xFF,
				(byte) 0xFF, (byte) 0xFF };

		/* build actual byte array */
		byte[] byteArray = Utils.concatByteArrays(byteArrayPre, lengthExpected,
				byteArrayPost);

		TlvLength lengthExtracted = new TlvLength(byteArray,
				byteArrayPre.length, byteArrayPre.length
						+ lengthExpected.length);
		assertArrayEquals("Equals expected byte array representation",
				lengthExtracted.toByteArray(), lengthExpected);
	}
	
	/**
	 * Positive test case: Extract 2 byte length from a range out of a larger
	 * byte array. The range is smaller than the length of the byte array and
	 * matches the minimum range required for the respective length
	 */
	@Test
	public void testConstructor_2ByteLength() {
		/* set arbitrary but valid 2-byte length */
		byte[] lengthExpected = new byte[] { (byte) 0x81, (byte) 0x88 };

		/* dummy data to prepend */
		byte[] byteArrayPre = new byte[] { (byte) 0xFF, (byte) 0xFF };
		/* dummy data to append */
		byte[] byteArrayPost = new byte[] { (byte) 0xFF, (byte) 0xFF,
				(byte) 0xFF, (byte) 0xFF };

		/* build actual byte array */
		byte[] byteArray = Utils.concatByteArrays(byteArrayPre, lengthExpected,
				byteArrayPost);

		TlvLength lengthExtracted = new TlvLength(byteArray,
				byteArrayPre.length, byteArrayPre.length
						+ lengthExpected.length);
		assertArrayEquals("Equals expected byte array representation",
				lengthExtracted.toByteArray(), lengthExpected);
	}
	
	/**
	 * Negative test case: Extract 2 byte length from a range out of a larger
	 * byte array. The range is smaller than the length of the byte array and is
	 * 1 byte smaller than the minimum range required for the respective length
	 */
	@Test(expected = ISO7816Exception.class)
	public void testConstructor_2ByteLengthTooShortRange() {
		/* set arbitrary but valid 2-byte length */
		byte[] lengthExpected = new byte[] { (byte) 0x81, (byte) 0x88 };

		/* dummy data to prepend */
		byte[] byteArrayPre = new byte[] { (byte) 0xFF, (byte) 0xFF };
		/* dummy data to append */
		byte[] byteArrayPost = new byte[] { (byte) 0xFF, (byte) 0xFF,
				(byte) 0xFF, (byte) 0xFF };

		/* build actual byte array */
		byte[] byteArray = Utils.concatByteArrays(byteArrayPre, lengthExpected,
				byteArrayPost);

		new TlvLength(byteArray, byteArrayPre.length, byteArrayPre.length
				+ lengthExpected.length - 1);
	}
	
	/**
	 * Positive test case: Extract 5 byte length from a range out of a larger
	 * byte array. The range is smaller than the length of the byte array and
	 * matches the minimum range required for the respective length
	 */
	@Test
	public void testConstructor_5ByteLength() {
		/* set arbitrary but valid 5-byte length */
		byte[] lengthExpected = new byte[] { (byte) 0x84, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00 };

		/* dummy data to prepend */
		byte[] byteArrayPre = new byte[] { (byte) 0xFF, (byte) 0xFF };
		/* dummy data to append */
		byte[] byteArrayPost = new byte[] { (byte) 0xFF, (byte) 0xFF,
				(byte) 0xFF, (byte) 0xFF };

		/* build actual byte array */
		byte[] byteArray = Utils.concatByteArrays(byteArrayPre, lengthExpected,
				byteArrayPost);

		TlvLength lengthExtracted = new TlvLength(byteArray,
				byteArrayPre.length, byteArrayPre.length
						+ lengthExpected.length);
		assertArrayEquals("Equals expected byte array representation",
				lengthExtracted.toByteArray(), lengthExpected);
	}
	
	/**
	 * Negative test case: First byte of Length field must not be 0x80
	 * This value is disallowed by ISO7816
	 */
	@Test(expected = ISO7816Exception.class)
	public void testConstructor_FirstByte80() {
		/* set arbitrary length with invalid length encoding */
		byte[] lengthExpected = new byte[] { (byte) 0x80, (byte) 0x01,
				(byte) 0x00 };

		new TlvLength(lengthExpected);
	}
	
	/**
	 * Negative test case: First byte of Length field must not be 0x85 to 0xFF
	 * These values are disallowed by ISO7816
	 */
	@Test(expected = ISO7816Exception.class)
	public void testConstructor_FirstByte85() {
		byte[] lengthExpected = new byte[] { (byte) 0x85, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };

		new TlvLength(lengthExpected);
	}
	
	/**
	 * Positive test case: Return indicated length
	 */
	@Test
	public void testGetIndicatedLength() {
		byte[] lengthExpected = new byte[] { (byte) 0x81, (byte) 0x01 };

		TlvLength lengthExtracted = new TlvLength(lengthExpected);
		assertEquals("Equals indicated length",
				lengthExtracted.getIndicatedLength(), 1);
	}
	
	/**
	 * Positive test case: Whether encoding is BER compliant
	 */
	@Test
	public void testIsValidBerEncoding_2ByteLengthBerNonDer() {
		/* set arbitrary but valid 5-byte length */
		byte[] lengthExpected = new byte[] { (byte) 0x81, (byte) 0x01 };

		TlvLength lengthExtracted = new TlvLength(lengthExpected);
		assertTrue(lengthExtracted.isValidBerEncoding());
	}
	
	/**
	 * Positive test case: Check a BER 1-byte length for BER validity
	 */
	@Test
	public void testIsValidBerEncoding_1ByteLengthBerNonDer() {
		/* set arbitrary but valid 1-byte length */
		byte[] lengthExpected = new byte[] { (byte) 0x7E };

		TlvLength lengthExtracted = new TlvLength(lengthExpected);
		assertTrue(lengthExtracted.isValidBerEncoding());
	}
	
	/**
	 * Negative test case: Check a BER but non-DER encoded 5-byte length for DER
	 * validity
	 */
	@Test
	public void testIsValidDerEncoding_5ByteLengthBerNonDer() {
		/* set arbitrary but valid 5-byte BER but non-DER length */
		byte[] lengthExpected = new byte[] { (byte) 0x84, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x01 };

		TlvLength lengthExtracted = new TlvLength(lengthExpected);

		assertFalse(lengthExtracted.isValidDerEncoding());
	}
	
	/**
	 * Positive test case: Check DER encoded 1-byte length for DER validity
	 */
	@Test
	public void testIsValidDerEncoding_1ByteLengthDer() {
		/* set arbitrary but valid 1-byte DER encoded length */
		byte[] lengthExpected = new byte[] { (byte) 0x7E };

		TlvLength lengthExtracted = new TlvLength(lengthExpected);
		assertTrue(lengthExtracted.isValidDerEncoding());
	}
	
	/**
	 * Positive test case: Check DER encoded 2-byte length for DER validity
	 */
	@Test
	public void testIsValidDerEncoding_2ByteLengthDer() {
		/* set arbitrary but valid 2-byte length */
		byte[] lengthExpected = new byte[] { (byte) 0x81, (byte) 0x88 };

		TlvLength lengthExtracted = new TlvLength(lengthExpected);
		assertTrue(lengthExtracted.isValidDerEncoding());
	}
	
	/**
	 * Positive test case: Check non-DER but BER encoded 2-byte length for DER validity
	 */
	@Test
	public void testIsValidDerEncoding_2ByteLengthBerNonDer() {
		/* set arbitrary but valid 2-byte length */
		byte[] lengthExpected = new byte[] { (byte) 0x81, (byte) 0x01 };

		TlvLength lengthExtracted = new TlvLength(lengthExpected);
		assertFalse(lengthExtracted.isValidDerEncoding());
	}
	
	/**
	 * Positive test case: check equals for same object and same constructor
	 * arguments
	 */
	@Test
	public void testEquals() {
		/* set arbitrary but valid 1-byte length */
		byte[] lengthExpected = new byte[] { (byte) 0x7E };

		TlvLength lengthFieldExpected1 = new TlvLength(lengthExpected);
		TlvLength lengthFieldExpected2 = new TlvLength(lengthExpected);

		assertEquals("Equals self", lengthFieldExpected1, lengthFieldExpected1);
		assertEquals("Equals same constructor arguments", lengthFieldExpected1,
				lengthFieldExpected2);
	}
	
	/**
	 * Negative test case: check equals for different constructor arguments
	 */
	@Test
	public void testEquals_DifferentValue() {
		/* set arbitrary but valid 1-byte length */
		byte[] lengthExpected1 = new byte[] { (byte) 0x7E };
		/* set arbitrary but valid 1-byte length */
		byte[] lengthExpected2 = new byte[] { (byte) 0x7D };

		TlvLength lengthFieldExpected1 = new TlvLength(lengthExpected1);
		TlvLength lengthFieldExpected2 = new TlvLength(lengthExpected2);

		assertNotEquals(lengthFieldExpected1, lengthFieldExpected2);
	}
	
	/**
	 * Negative test case: check equals for different BER encodings of the same
	 * length
	 */
	@Test
	public void testEquals_DifferentEncoding() {
		/* set arbitrary but valid 1-byte length */
		byte[] lengthExpected1 = new byte[] { (byte) 0x01 };
		/* set arbitrary but valid 2-byte length with same BER encoded length */
		byte[] lengthExpected2 = new byte[] { (byte) 0x81, (byte) 0x01 };

		TlvLength lengthFieldExpected1 = new TlvLength(lengthExpected1);
		TlvLength lengthFieldExpected2 = new TlvLength(lengthExpected2);

		assertFalse(lengthFieldExpected1.equals(lengthFieldExpected2));
	}
	
}
