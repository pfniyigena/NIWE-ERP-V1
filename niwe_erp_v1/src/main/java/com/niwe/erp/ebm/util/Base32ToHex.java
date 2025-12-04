package com.niwe.erp.ebm.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Base32ToHex {
	private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

	private static final int[] base32Lookup = new int[] { 255, 255, 26, 27, 28, 29, 30, 31, 255, 255, 255, 255, 255,
			255, 255, 255, 255, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
			25, 255, 255, 255, 255, 255, 255, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
			22, 23, 24, 25, 255, 255, 255, 255, 255 };

	public static String encode(byte[] bytes) {
		int i = 0, index = 0, digit = 0;
		StringBuffer base32 = new StringBuffer((bytes.length + 7) * 8 / 5);
		while (i < bytes.length) {
			int currByte = (bytes[i] >= 0) ? bytes[i] : (bytes[i] + 256);
			if (index > 3) {
				int nextByte;
				if (i + 1 < bytes.length) {
					nextByte = (bytes[i + 1] >= 0) ? bytes[i + 1] : (bytes[i + 1] + 256);
				} else {
					nextByte = 0;
				}
				digit = currByte & 255 >> index;
				index = (index + 5) % 8;
				digit <<= index;
				digit |= nextByte >> 8 - index;
				i++;
			} else {
				digit = currByte >> 8 - index + 5 & 0x1F;
				index = (index + 5) % 8;
				if (index == 0)
					i++;
			}
			base32.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567".charAt(digit));
		}
		return base32.toString();
	}

	public byte[] decode(String base32) {
		byte[] bytes = new byte[base32.length() * 5 / 8];
		for (int i = 0, index = 0, offset = 0; i < base32.length(); i++) {
			int lookup = base32.charAt(i) - 48;
			if (lookup >= 0 && lookup < base32Lookup.length) {
				int digit = base32Lookup[lookup];
				if (digit != 255)
					if (index <= 3) {
						index = (index + 5) % 8;
						if (index == 0) {
							bytes[offset] = (byte) (bytes[offset] | digit);
							offset++;
							if (offset >= bytes.length)
								break;
						} else {
							bytes[offset] = (byte) (bytes[offset] | digit << 8 - index);
						}
					} else {
						index = (index + 5) % 8;
						bytes[offset] = (byte) (bytes[offset] | digit >>> index);
						offset++;
						if (offset >= bytes.length)
							break;
						bytes[offset] = (byte) (bytes[offset] | digit << 8 - index);
					}
			}
		}
		return bytes;
	}

	public String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public String hmacSha1(String value, String key) {
		try {
			byte[] keyBytes = decode(key);
			SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(value.getBytes());
			byte[] encVal = new byte[10];
			System.arraycopy(rawHmac, 0, encVal, 0, 10);
			return encode(encVal);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
