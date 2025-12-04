package com.niwe.erp.ebm.util;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class AesEnc {
    private static final String ALGO = "AES";

    Base32ToHex base32 = new Base32ToHex();

    public static final String longToHex(long lval, int nbByte) {
        String hexString = null;
        hexString = Long.toHexString(lval);
        int nbChar = hexString.length();
        if (nbChar < nbByte * 2)
            for (int i = nbChar + 1; i <= nbByte * 2; i++)
                hexString = "0" + hexString;
        return hexString;
    }

    private final byte[] HexToByteArray(String hex, int n) {
        int NumberChars = hex.length();
        if (NumberChars % 2 != 0)
            hex = "0" + hex;
        byte[] bytes = new byte[n];
        int j = 0;
        for (int i = 0; i <= NumberChars - 1; i += 2) {
            bytes[j] = (byte)Integer.parseInt(hex.substring(i, i + 2), 16);
            j++;
        }
        return bytes;
    }

    public String encrypt(String data, String key1) throws Exception {
        Key key = generateKey(key1);
        Cipher c = Cipher.getInstance("AES/ECB/NoPadding");
        c.init(1, key);
        byte[] encVal = c.doFinal(HexToByteArray(data, 16));
        return encode(encVal);
    }

    private Key generateKey(String key ) {
        return new SecretKeySpec(HexToByteArray(key, 32), ALGO);
    }

    public static String encode(byte[] bytes) {
        int i = 0;
        int index = 0;
        int digit = 0;
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
}
