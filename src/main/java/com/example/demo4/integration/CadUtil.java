package com.example.demo4.integration;

import com.sun.jna.Platform;
import com.sun.jna.Pointer;

import java.io.UnsupportedEncodingException;

// this code copy from cad lib source
public final class CadUtil {

    private CadUtil() {
    }

    /**
     * Convert {@link Pointer} to {@link String}.
     *
     * @param ptr {@link Pointer}.
     * @return {@link String}
     */
    public static String toString(Pointer ptr) {
        if (ptr == null) {
            return "";
        }
        int offset = 0;
        String s = "";
        char c = ptr.getChar(offset);
        while (c != 0) {
            s = s + c;
            offset = offset + 2;
            c = ptr.getChar(offset);
        }
        return s;

    }

    /**
     * Convert {@link String} to byte[].
     *
     * @param str {@link String}
     * @return byte[]
     */
    public static byte[] toByte(String str) {
        return toByte(str, Platform.isWindows() ? "UTF-16LE" : "UTF-8");
    }

    /**
     * Convert {@link String} to byte[] by charsetName.
     *
     * @param str         {@link String}
     * @param charsetName {@link String}
     * @return byte[]
     */
    public static byte[] toByte(String str, String charsetName) {
        try {
            String str0 = str + '\0';
            return str0.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported encoding: " + e.getMessage());
            return new byte[]{0, 0};
        }
    }

    /**
     * Convert byte[] to {@link String}.
     *
     * @param buf byte[]
     * @return {@link String}
     */
    public static String toStringByByte(byte[] buf) {
        return toStringByByte(buf, Platform.isWindows() ? "UTF-16LE" : "UTF-8");
    }

    /**
     * Convert byte[] to {@link String} by charset.
     *
     * @param buf     byte[]
     * @param charset {@link String}
     * @return {@link String}
     */
    public static String toStringByByte(byte[] buf, String charset) {
        int len = Platform.isWindows() ? buf.length / 2 : buf.length;
        for (int index = 0; index < len; index++) {
            if (Platform.isWindows()) {
                if (buf[index * 2] == 0 && buf[index * 2 + 1] == 0) {
                    len = (index + 1) * 2;
                    if (len == 2) {
                        len = 0;
                    }
                    break;
                }
            } else {
                if (buf[index] == 0) {
                    len = index;
                    break;
                }
            }
        }

        String result = "";
        if (len != 0) {
            try {
                result = new String(buf, 0, len, charset);
            } catch (UnsupportedEncodingException e) {
                System.out.println("Unsupported encoding: " + e.getMessage());
            }
        }
        return result;
    }
}
