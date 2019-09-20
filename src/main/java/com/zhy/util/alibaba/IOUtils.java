package com.zhy.util.alibaba;

import com.zhy.util.exception.UtilException;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author zhy
 * @date 2019/9/19 15:53
 */
public class IOUtils {
    public static final Properties DEFAULT_PROPERTIES = new Properties();
    public static final boolean[] firstIdentifierFlags = new boolean[256];
    public static final boolean[] identifierFlags = new boolean[256];
    public static final byte[] specicalFlags_doubleQuotes;
    public static final byte[] specicalFlags_singleQuotes;
    public static final boolean[] specicalFlags_doubleQuotesFlags;
    public static final boolean[] specicalFlags_singleQuotesFlags;
    public static final char[] replaceChars;
    public static final char[] ASCII_CHARS;
    static final char[] digits;
    static final char[] DigitTens;
    static final char[] DigitOnes;
    static final int[] sizeTable;
    public static final char[] CA;
    public static final int[] IA;

    public IOUtils() {
    }

    public static String getStringProperty(String name) {
        String prop = null;

        try {
            prop = System.getProperty(name);
        } catch (SecurityException var3) {
        }

        return prop == null ? DEFAULT_PROPERTIES.getProperty(name) : prop;
    }

    public static void loadPropertiesFromFile() {
        InputStream imputStream = AccessController.doPrivileged(new PrivilegedAction<InputStream>() {
            public InputStream run() {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                return cl != null ? cl.getResourceAsStream("fastjson.properties") : ClassLoader.getSystemResourceAsStream("fastjson.properties");
            }
        });
        if (null != imputStream) {
            try {
                DEFAULT_PROPERTIES.load(imputStream);
                imputStream.close();
            } catch (IOException var2) {
            }
        }

    }

    public static void close(Closeable x) {
        if (x != null) {
            try {
                x.close();
            } catch (Exception var2) {
            }
        }

    }

    public static int stringSize(long x) {
        long p = 10L;

        for(int i = 1; i < 19; ++i) {
            if (x < p) {
                return i;
            }

            p = 10L * p;
        }

        return 19;
    }

    public static void getChars(long i, int index, char[] buf) {
        int charPos = index;
        char sign = 0;
        if (i < 0L) {
            sign = 45;
            i = -i;
        }

        int r;
        while(i > 2147483647L) {
            long q = i / 100L;
            r = (int)(i - ((q << 6) + (q << 5) + (q << 2)));
            i = q;
            --charPos;
            buf[charPos] = DigitOnes[r];
            --charPos;
            buf[charPos] = DigitTens[r];
        }

        int q2;
        int i2;
        for(i2 = (int)i; i2 >= 65536; buf[charPos] = DigitTens[r]) {
            q2 = i2 / 100;
            r = i2 - ((q2 << 6) + (q2 << 5) + (q2 << 2));
            i2 = q2;
            --charPos;
            buf[charPos] = DigitOnes[r];
            --charPos;
        }

        do {
            q2 = i2 * '쳍' >>> 19;
            r = i2 - ((q2 << 3) + (q2 << 1));
            --charPos;
            buf[charPos] = digits[r];
            i2 = q2;
        } while(q2 != 0);

        if (sign != 0) {
            --charPos;
            buf[charPos] = (char)sign;
        }

    }

    public static void getChars(int i, int index, char[] buf) {
        int p = index;
        char sign = 0;
        if (i < 0) {
            sign = 45;
            i = -i;
        }

        int q;
        int r;
        while(i >= 65536) {
            q = i / 100;
            r = i - ((q << 6) + (q << 5) + (q << 2));
            i = q;
            --p;
            buf[p] = DigitOnes[r];
            --p;
            buf[p] = DigitTens[r];
        }

        do {
            q = i * '쳍' >>> 19;
            r = i - ((q << 3) + (q << 1));
            --p;
            buf[p] = digits[r];
            i = q;
        } while(q != 0);

        if (sign != 0) {
            --p;
            buf[p] = (char)sign;
        }

    }

    public static void getChars(byte b, int index, char[] buf) {
        int i = b;
        int charPos = index;
        char sign = 0;
        if (b < 0) {
            sign = 45;
            i = -b;
        }

        int q;
        do {
            q = i * '쳍' >>> 19;
            int r = i - ((q << 3) + (q << 1));
            --charPos;
            buf[charPos] = digits[r];
            i = q;
        } while(q != 0);

        if (sign != 0) {
            --charPos;
            buf[charPos] = (char)sign;
        }

    }

    public static int stringSize(int x) {
        int i;
        for(i = 0; x > sizeTable[i]; ++i) {
        }

        return i + 1;
    }

    public static void decode(CharsetDecoder charsetDecoder, ByteBuffer byteBuf, CharBuffer charByte) {
        try {
            CoderResult cr = charsetDecoder.decode(byteBuf, charByte, true);
            if (!cr.isUnderflow()) {
                cr.throwException();
            }

            cr = charsetDecoder.flush(charByte);
            if (!cr.isUnderflow()) {
                cr.throwException();
            }

        } catch (CharacterCodingException var4) {
            throw new UtilException("utf8 decode error, " + var4.getMessage(), var4);
        }
    }

    public static boolean firstIdentifier(char ch) {
        return ch < firstIdentifierFlags.length && firstIdentifierFlags[ch];
    }

    public static boolean isIdent(char ch) {
        return ch < identifierFlags.length && identifierFlags[ch];
    }

    public static byte[] decodeBase64(char[] chars, int offset, int charsLen) {
        if (charsLen == 0) {
            return new byte[0];
        } else {
            int sIx = offset;

            int eIx;
            for(eIx = offset + charsLen - 1; sIx < eIx && IA[chars[sIx]] < 0; ++sIx) {
            }

            while(eIx > 0 && IA[chars[eIx]] < 0) {
                --eIx;
            }

            int pad = chars[eIx] == '=' ? (chars[eIx - 1] == '=' ? 2 : 1) : 0;
            int cCnt = eIx - sIx + 1;
            int sepCnt = charsLen > 76 ? (chars[76] == '\r' ? cCnt / 78 : 0) << 1 : 0;
            int len = ((cCnt - sepCnt) * 6 >> 3) - pad;
            byte[] bytes = new byte[len];
            int d = 0;
            int i = 0;
            int r = len / 3 * 3;

            while(d < r) {
                int ii = IA[chars[sIx++]] << 18 | IA[chars[sIx++]] << 12 | IA[chars[sIx++]] << 6 | IA[chars[sIx++]];
                bytes[d++] = (byte)(ii >> 16);
                bytes[d++] = (byte)(ii >> 8);
                bytes[d++] = (byte)ii;
                if (sepCnt > 0) {
                    ++i;
                    if (i == 19) {
                        sIx += 2;
                        i = 0;
                    }
                }
            }

            if (d < len) {
                i = 0;

                for(r = 0; sIx <= eIx - pad; ++r) {
                    i |= IA[chars[sIx++]] << 18 - r * 6;
                }

                for(r = 16; d < len; r -= 8) {
                    bytes[d++] = (byte)(i >> r);
                }
            }

            return bytes;
        }
    }

    public static byte[] decodeBase64(String chars, int offset, int charsLen) {
        if (charsLen == 0) {
            return new byte[0];
        } else {
            int sIx = offset;

            int eIx;
            for(eIx = offset + charsLen - 1; sIx < eIx && IA[chars.charAt(sIx)] < 0; ++sIx) {
            }

            while(eIx > 0 && IA[chars.charAt(eIx)] < 0) {
                --eIx;
            }

            int pad = chars.charAt(eIx) == '=' ? (chars.charAt(eIx - 1) == '=' ? 2 : 1) : 0;
            int cCnt = eIx - sIx + 1;
            int sepCnt = charsLen > 76 ? (chars.charAt(76) == '\r' ? cCnt / 78 : 0) << 1 : 0;
            int len = ((cCnt - sepCnt) * 6 >> 3) - pad;
            byte[] bytes = new byte[len];
            int d = 0;
            int i = 0;
            int r = len / 3 * 3;

            while(d < r) {
                int ii = IA[chars.charAt(sIx++)] << 18 | IA[chars.charAt(sIx++)] << 12 | IA[chars.charAt(sIx++)] << 6 | IA[chars.charAt(sIx++)];
                bytes[d++] = (byte)(ii >> 16);
                bytes[d++] = (byte)(ii >> 8);
                bytes[d++] = (byte)ii;
                if (sepCnt > 0) {
                    ++i;
                    if (i == 19) {
                        sIx += 2;
                        i = 0;
                    }
                }
            }

            if (d < len) {
                i = 0;

                for(r = 0; sIx <= eIx - pad; ++r) {
                    i |= IA[chars.charAt(sIx++)] << 18 - r * 6;
                }

                for(r = 16; d < len; r -= 8) {
                    bytes[d++] = (byte)(i >> r);
                }
            }

            return bytes;
        }
    }

    public static byte[] decodeBase64(String s) {
        int sLen = s.length();
        if (sLen == 0) {
            return new byte[0];
        } else {
            int sIx = 0;

            int eIx;
            for(eIx = sLen - 1; sIx < eIx && IA[s.charAt(sIx) & 255] < 0; ++sIx) {
            }

            while(eIx > 0 && IA[s.charAt(eIx) & 255] < 0) {
                --eIx;
            }

            int pad = s.charAt(eIx) == '=' ? (s.charAt(eIx - 1) == '=' ? 2 : 1) : 0;
            int cCnt = eIx - sIx + 1;
            int sepCnt = sLen > 76 ? (s.charAt(76) == '\r' ? cCnt / 78 : 0) << 1 : 0;
            int len = ((cCnt - sepCnt) * 6 >> 3) - pad;
            byte[] dArr = new byte[len];
            int d = 0;
            int i = 0;
            int r = len / 3 * 3;

            while(d < r) {
                int ii = IA[s.charAt(sIx++)] << 18 | IA[s.charAt(sIx++)] << 12 | IA[s.charAt(sIx++)] << 6 | IA[s.charAt(sIx++)];
                dArr[d++] = (byte)(ii >> 16);
                dArr[d++] = (byte)(ii >> 8);
                dArr[d++] = (byte)ii;
                if (sepCnt > 0) {
                    ++i;
                    if (i == 19) {
                        sIx += 2;
                        i = 0;
                    }
                }
            }

            if (d < len) {
                i = 0;

                for(r = 0; sIx <= eIx - pad; ++r) {
                    i |= IA[s.charAt(sIx++)] << 18 - r * 6;
                }

                for(r = 16; d < len; r -= 8) {
                    dArr[d++] = (byte)(i >> r);
                }
            }

            return dArr;
        }
    }

    public static int encodeUTF8(char[] chars, int offset, int len, byte[] bytes) {
        int sl = offset + len;
        int dp = 0;

        for(int dlASCII = dp + Math.min(len, bytes.length); dp < dlASCII && chars[offset] < 128; bytes[dp++] = (byte)chars[offset++]) {
        }

        while(true) {
            while(offset < sl) {
                char c = chars[offset++];
                if (c < 128) {
                    bytes[dp++] = (byte)c;
                } else if (c < 2048) {
                    bytes[dp++] = (byte)(192 | c >> 6);
                    bytes[dp++] = (byte)(128 | c & 63);
                } else if (c >= '\ud800' && c < '\ue000') {
                    int ip = offset - 1;
                    int uc;
                    if (c >= '\ud800' && c < '\udc00') {
                        if (sl - ip < 2) {
                            uc = -1;
                        } else {
                            char d = chars[ip + 1];
                            if (d < '\udc00' || d >= '\ue000') {
                                bytes[dp++] = 63;
                                continue;
                            }

                            uc = (c << 10) + d + -56613888;
                        }
                    } else {
                        if (c >= '\udc00' && c < '\ue000') {
                            bytes[dp++] = 63;
                            continue;
                        }

                        uc = c;
                    }

                    if (uc < 0) {
                        bytes[dp++] = 63;
                    } else {
                        bytes[dp++] = (byte)(240 | uc >> 18);
                        bytes[dp++] = (byte)(128 | uc >> 12 & 63);
                        bytes[dp++] = (byte)(128 | uc >> 6 & 63);
                        bytes[dp++] = (byte)(128 | uc & 63);
                        ++offset;
                    }
                } else {
                    bytes[dp++] = (byte)(224 | c >> 12);
                    bytes[dp++] = (byte)(128 | c >> 6 & 63);
                    bytes[dp++] = (byte)(128 | c & 63);
                }
            }

            return dp;
        }
    }

    /** @deprecated */
    public static int decodeUTF8(byte[] sa, int sp, int len, char[] da) {
        int sl = sp + len;
        int dp = 0;

        for(int dlASCII = Math.min(len, da.length); dp < dlASCII && sa[sp] >= 0; da[dp++] = (char)sa[sp++]) {
        }

        while(true) {
            while(true) {
                while(sp < sl) {
                    int b1 = sa[sp++];
                    if (b1 < 0) {
                        byte b2;
                        if (b1 >> 5 != -2 || (b1 & 30) == 0) {
                            byte b3;
                            if (b1 >> 4 == -2) {
                                if (sp + 1 >= sl) {
                                    return -1;
                                }

                                b2 = sa[sp++];
                                b3 = sa[sp++];
                                if (b1 == -32 && (b2 & 224) == 128 || (b2 & 192) != 128 || (b3 & 192) != 128) {
                                    return -1;
                                }

                                char c = (char)(b1 << 12 ^ b2 << 6 ^ b3 ^ -123008);
                                boolean isSurrogate = c >= '\ud800' && c < '\ue000';
                                if (isSurrogate) {
                                    return -1;
                                }

                                da[dp++] = c;
                            } else {
                                if (b1 >> 3 != -2) {
                                    return -1;
                                }

                                if (sp + 2 >= sl) {
                                    return -1;
                                }

                                b2 = sa[sp++];
                                b3 = sa[sp++];
                                int b4 = sa[sp++];
                                int uc = b1 << 18 ^ b2 << 12 ^ b3 << 6 ^ b4 ^ 3678080;
                                if ((b2 & 192) != 128 || (b3 & 192) != 128 || (b4 & 192) != 128 || uc < 65536 || uc >= 1114112) {
                                    return -1;
                                }

                                da[dp++] = (char)((uc >>> 10) + 'ퟀ');
                                da[dp++] = (char)((uc & 1023) + '\udc00');
                            }
                        } else {
                            if (sp >= sl) {
                                return -1;
                            }

                            b2 = sa[sp++];
                            if ((b2 & 192) != 128) {
                                return -1;
                            }

                            da[dp++] = (char)(b1 << 6 ^ b2 ^ 3968);
                        }
                    } else {
                        da[dp++] = (char)b1;
                    }
                }

                return dp;
            }
        }
    }

    /** @deprecated */
    public static String readAll(Reader reader) {
        StringBuilder buf = new StringBuilder();

        try {
            char[] chars = new char[2048];

            while(true) {
                int len = reader.read(chars, 0, chars.length);
                if (len < 0) {
                    return buf.toString();
                }

                buf.append(chars, 0, len);
            }
        } catch (Exception var4) {
            throw new UtilException("read string from reader error", var4);
        }
    }

    public static boolean isValidJsonpQueryParam(String value) {
        if (value != null && value.length() != 0) {
            int i = 0;

            for(int len = value.length(); i < len; ++i) {
                char ch = value.charAt(i);
                if (ch != '.' && !isIdent(ch)) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    static {
        char c;
        for(c = 0; c < firstIdentifierFlags.length; ++c) {
            if (c >= 'A' && c <= 'Z') {
                firstIdentifierFlags[c] = true;
            } else if (c >= 'a' && c <= 'z') {
                firstIdentifierFlags[c] = true;
            } else if (c == '_' || c == '$') {
                firstIdentifierFlags[c] = true;
            }
        }

        for(c = 0; c < identifierFlags.length; ++c) {
            if (c >= 'A' && c <= 'Z') {
                identifierFlags[c] = true;
            } else if (c >= 'a' && c <= 'z') {
                identifierFlags[c] = true;
            } else if (c == '_') {
                identifierFlags[c] = true;
            } else if (c >= '0' && c <= '9') {
                identifierFlags[c] = true;
            }
        }

        try {
            loadPropertiesFromFile();
        } catch (Throwable var2) {
        }

        specicalFlags_doubleQuotes = new byte[161];
        specicalFlags_singleQuotes = new byte[161];
        specicalFlags_doubleQuotesFlags = new boolean[161];
        specicalFlags_singleQuotesFlags = new boolean[161];
        replaceChars = new char[93];
        specicalFlags_doubleQuotes[0] = 4;
        specicalFlags_doubleQuotes[1] = 4;
        specicalFlags_doubleQuotes[2] = 4;
        specicalFlags_doubleQuotes[3] = 4;
        specicalFlags_doubleQuotes[4] = 4;
        specicalFlags_doubleQuotes[5] = 4;
        specicalFlags_doubleQuotes[6] = 4;
        specicalFlags_doubleQuotes[7] = 4;
        specicalFlags_doubleQuotes[8] = 1;
        specicalFlags_doubleQuotes[9] = 1;
        specicalFlags_doubleQuotes[10] = 1;
        specicalFlags_doubleQuotes[11] = 4;
        specicalFlags_doubleQuotes[12] = 1;
        specicalFlags_doubleQuotes[13] = 1;
        specicalFlags_doubleQuotes[34] = 1;
        specicalFlags_doubleQuotes[92] = 1;
        specicalFlags_singleQuotes[0] = 4;
        specicalFlags_singleQuotes[1] = 4;
        specicalFlags_singleQuotes[2] = 4;
        specicalFlags_singleQuotes[3] = 4;
        specicalFlags_singleQuotes[4] = 4;
        specicalFlags_singleQuotes[5] = 4;
        specicalFlags_singleQuotes[6] = 4;
        specicalFlags_singleQuotes[7] = 4;
        specicalFlags_singleQuotes[8] = 1;
        specicalFlags_singleQuotes[9] = 1;
        specicalFlags_singleQuotes[10] = 1;
        specicalFlags_singleQuotes[11] = 4;
        specicalFlags_singleQuotes[12] = 1;
        specicalFlags_singleQuotes[13] = 1;
        specicalFlags_singleQuotes[92] = 1;
        specicalFlags_singleQuotes[39] = 1;

        int i;
        for(i = 14; i <= 31; ++i) {
            specicalFlags_doubleQuotes[i] = 4;
            specicalFlags_singleQuotes[i] = 4;
        }

        for(i = 127; i < 160; ++i) {
            specicalFlags_doubleQuotes[i] = 4;
            specicalFlags_singleQuotes[i] = 4;
        }

        for(i = 0; i < 161; ++i) {
            specicalFlags_doubleQuotesFlags[i] = specicalFlags_doubleQuotes[i] != 0;
            specicalFlags_singleQuotesFlags[i] = specicalFlags_singleQuotes[i] != 0;
        }

        replaceChars[0] = '0';
        replaceChars[1] = '1';
        replaceChars[2] = '2';
        replaceChars[3] = '3';
        replaceChars[4] = '4';
        replaceChars[5] = '5';
        replaceChars[6] = '6';
        replaceChars[7] = '7';
        replaceChars[8] = 'b';
        replaceChars[9] = 't';
        replaceChars[10] = 'n';
        replaceChars[11] = 'v';
        replaceChars[12] = 'f';
        replaceChars[13] = 'r';
        replaceChars[34] = '"';
        replaceChars[39] = '\'';
        replaceChars[47] = '/';
        replaceChars[92] = '\\';
        ASCII_CHARS = new char[]{'0', '0', '0', '1', '0', '2', '0', '3', '0', '4', '0', '5', '0', '6', '0', '7', '0', '8', '0', '9', '0', 'A', '0', 'B', '0', 'C', '0', 'D', '0', 'E', '0', 'F', '1', '0', '1', '1', '1', '2', '1', '3', '1', '4', '1', '5', '1', '6', '1', '7', '1', '8', '1', '9', '1', 'A', '1', 'B', '1', 'C', '1', 'D', '1', 'E', '1', 'F', '2', '0', '2', '1', '2', '2', '2', '3', '2', '4', '2', '5', '2', '6', '2', '7', '2', '8', '2', '9', '2', 'A', '2', 'B', '2', 'C', '2', 'D', '2', 'E', '2', 'F'};
        digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        DigitTens = new char[]{'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        DigitOnes = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        sizeTable = new int[]{9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, 2147483647};
        CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
        IA = new int[256];
        Arrays.fill(IA, -1);
        i = 0;

        for(int iS = CA.length; i < iS; IA[CA[i]] = i++) {
        }

        IA[61] = 0;
    }
}
