package com.zhy.util.alibaba;

import com.zhy.util.exception.UtilException;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.*;

/**
 * @author zhy
 * @date 2019/9/19 15:36
 */
public class TypeUtils {
    public TypeUtils() {
    }

    public static String castToString(Object value) {
        return value == null ? null : value.toString();
    }

    public static Byte castToByte(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof BigDecimal) {
            return byteValue((BigDecimal)value);
        } else if (value instanceof Number) {
            return ((Number)value).byteValue();
        } else if (value instanceof String) {
            String strVal = (String)value;
            return strVal.length() != 0 && !"null".equals(strVal) && !"NULL".equals(strVal) ? Byte.parseByte(strVal) : null;
        } else {
            throw new UtilException("can not cast to byte, value : " + value);
        }
    }

    public static Character castToChar(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Character) {
            return (Character)value;
        } else if (value instanceof String) {
            String strVal = (String)value;
            if (strVal.length() == 0) {
                return null;
            } else if (strVal.length() != 1) {
                throw new UtilException("can not cast to char, value : " + value);
            } else {
                return strVal.charAt(0);
            }
        } else {
            throw new UtilException("can not cast to char, value : " + value);
        }
    }

    public static Short castToShort(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof BigDecimal) {
            return shortValue((BigDecimal)value);
        } else if (value instanceof Number) {
            return ((Number)value).shortValue();
        } else if (value instanceof String) {
            String strVal = (String)value;
            return strVal.length() != 0 && !"null".equals(strVal) && !"NULL".equals(strVal) ? Short.parseShort(strVal) : null;
        } else {
            throw new UtilException("can not cast to short, value : " + value);
        }
    }

    public static BigDecimal castToBigDecimal(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof BigDecimal) {
            return (BigDecimal)value;
        } else if (value instanceof BigInteger) {
            return new BigDecimal((BigInteger)value);
        } else {
            String strVal = value.toString();
            if (strVal.length() == 0) {
                return null;
            } else {
                return value instanceof Map && ((Map)value).size() == 0 ? null : new BigDecimal(strVal);
            }
        }
    }

    public static BigInteger castToBigInteger(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof BigInteger) {
            return (BigInteger)value;
        } else if (!(value instanceof Float) && !(value instanceof Double)) {
            if (value instanceof BigDecimal) {
                BigDecimal decimal = (BigDecimal)value;
                int scale = decimal.scale();
                if (scale > -1000 && scale < 1000) {
                    return ((BigDecimal)value).toBigInteger();
                }
            }

            String strVal = value.toString();
            return strVal.length() != 0 && !"null".equals(strVal) && !"NULL".equals(strVal) ? new BigInteger(strVal) : null;
        } else {
            return BigInteger.valueOf(((Number)value).longValue());
        }
    }

    public static Float castToFloat(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Number) {
            return ((Number)value).floatValue();
        } else if (value instanceof String) {
            String strVal = value.toString();
            if (strVal.length() != 0 && !"null".equals(strVal) && !"NULL".equals(strVal)) {
                if (strVal.indexOf(44) != 0) {
                    strVal = strVal.replaceAll(",", "");
                }

                return Float.parseFloat(strVal);
            } else {
                return null;
            }
        } else {
            throw new UtilException("can not cast to float, value : " + value);
        }
    }

    public static Double castToDouble(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Number) {
            return ((Number)value).doubleValue();
        } else if (value instanceof String) {
            String strVal = value.toString();
            if (strVal.length() != 0 && !"null".equals(strVal) && !"NULL".equals(strVal)) {
                if (strVal.indexOf(44) != 0) {
                    strVal = strVal.replaceAll(",", "");
                }

                return Double.parseDouble(strVal);
            } else {
                return null;
            }
        } else {
            throw new UtilException("can not cast to double, value : " + value);
        }
    }

//    public static Date castToDate(Object value) {
//        return castToDate(value, (String)null);
//    }
//
//    public static Date castToDate(Object value, String format) {
//        if (value == null) {
//            return null;
//        } else if (value instanceof Date) {
//            return (Date)value;
//        } else if (value instanceof Calendar) {
//            return ((Calendar)value).getTime();
//        } else {
//            long longValue = -1L;
//            if (value instanceof BigDecimal) {
//                longValue = longValue((BigDecimal)value);
//                return new Date(longValue);
//            } else if (value instanceof Number) {
//                longValue = ((Number)value).longValue();
//                if ("unixtime".equals(format)) {
//                    longValue *= 1000L;
//                }
//
//                return new Date(longValue);
//            } else {
//                if (value instanceof String) {
//                    String strVal = (String)value;
//                    JSONScanner dateLexer = new JSONScanner(strVal);
//
//                    try {
//                        if (dateLexer.scanISO8601DateIfMatch(false)) {
//                            Calendar calendar = dateLexer.getCalendar();
//                            Date var7 = calendar.getTime();
//                            return var7;
//                        }
//                    } finally {
//                        dateLexer.close();
//                    }
//
//                    if (strVal.startsWith("/Date(") && strVal.endsWith(")/")) {
//                        strVal = strVal.substring(6, strVal.length() - 2);
//                    }
//
//                    if (strVal.indexOf(45) > 0 || strVal.indexOf(43) > 0) {
//                        if (format == null) {
//                            if (strVal.length() != JSON.DEFFAULT_DATE_FORMAT.length() && (strVal.length() != 22 || !JSON.DEFFAULT_DATE_FORMAT.equals("yyyyMMddHHmmssSSSZ"))) {
//                                if (strVal.length() == 10) {
//                                    format = "yyyy-MM-dd";
//                                } else if (strVal.length() == "yyyy-MM-dd HH:mm:ss".length()) {
//                                    format = "yyyy-MM-dd HH:mm:ss";
//                                } else if (strVal.length() == 29 && strVal.charAt(26) == ':' && strVal.charAt(28) == '0') {
//                                    format = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
//                                } else if (strVal.length() == 23 && strVal.charAt(19) == ',') {
//                                    format = "yyyy-MM-dd HH:mm:ss,SSS";
//                                } else {
//                                    format = "yyyy-MM-dd HH:mm:ss.SSS";
//                                }
//                            } else {
//                                format = JSON.DEFFAULT_DATE_FORMAT;
//                            }
//                        }
//
//                        SimpleDateFormat dateFormat = new SimpleDateFormat(format, JSON.defaultLocale);
//                        dateFormat.setTimeZone(JSON.defaultTimeZone);
//
//                        try {
//                            return dateFormat.parse(strVal);
//                        } catch (ParseException var35) {
//                            throw new UtilException("can not cast to Date, value : " + strVal);
//                        }
//                    }
//
//                    if (strVal.length() == 0) {
//                        return null;
//                    }
//
//                    longValue = Long.parseLong(strVal);
//                }
//
//                if (longValue == -1L) {
//                    Class<?> clazz = value.getClass();
//                    Object result;
//                    if ("oracle.sql.TIMESTAMP".equals(clazz.getName())) {
//                        if (oracleTimestampMethod == null && !oracleTimestampMethodInited) {
//                            try {
//                                oracleTimestampMethod = clazz.getMethod("toJdbc");
//                            } catch (NoSuchMethodException var37) {
//                            } finally {
//                                oracleTimestampMethodInited = true;
//                            }
//                        }
//
//                        try {
//                            result = oracleTimestampMethod.invoke(value);
//                        } catch (Exception var36) {
//                            throw new UtilException("can not cast oracle.sql.TIMESTAMP to Date", var36);
//                        }
//
//                        return (Date)result;
//                    } else if ("oracle.sql.DATE".equals(clazz.getName())) {
//                        if (oracleDateMethod == null && !oracleDateMethodInited) {
//                            try {
//                                oracleDateMethod = clazz.getMethod("toJdbc");
//                            } catch (NoSuchMethodException var40) {
//                            } finally {
//                                oracleDateMethodInited = true;
//                            }
//                        }
//
//                        try {
//                            result = oracleDateMethod.invoke(value);
//                        } catch (Exception var39) {
//                            throw new UtilException("can not cast oracle.sql.DATE to Date", var39);
//                        }
//
//                        return (Date)result;
//                    } else {
//                        throw new UtilException("can not cast to Date, value : " + value);
//                    }
//                } else {
//                    return new Date(longValue);
//                }
//            }
//        }
//    }

    public static boolean isNumber(String str) {
        for(int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            if (ch != '+' && ch != '-') {
                if (ch < '0' || ch > '9') {
                    return false;
                }
            } else if (i != 0) {
                return false;
            }
        }

        return true;
    }

    public static Long castToLong(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof BigDecimal) {
            return longValue((BigDecimal)value);
        } else if (value instanceof Number) {
            return ((Number)value).longValue();
        } else {
            if (value instanceof String) {
                String strVal = (String)value;
                if (strVal.length() == 0 || "null".equals(strVal) || "NULL".equals(strVal)) {
                    return null;
                }

                if (strVal.indexOf(44) != 0) {
                    strVal = strVal.replaceAll(",", "");
                }

                try {
                    return Long.parseLong(strVal);
                } catch (NumberFormatException var4) {
//                    JSONScanner dateParser = new JSONScanner(strVal);
//                    Calendar calendar = null;
//                    if (dateParser.scanISO8601DateIfMatch(false)) {
//                        calendar = dateParser.getCalendar();
//                    }
//
//                    dateParser.close();
//                    if (calendar != null) {
//                        return calendar.getTimeInMillis();
//                    }
                }
            }

            if (value instanceof Map) {
                Map map = (Map)value;
                if (map.size() == 2 && map.containsKey("andIncrement") && map.containsKey("andDecrement")) {
                    Iterator iter = map.values().iterator();
                    iter.next();
                    Object value2 = iter.next();
                    return castToLong(value2);
                }
            }

            throw new UtilException("can not cast to long, value : " + value);
        }
    }

    public static byte byteValue(BigDecimal decimal) {
        if (decimal == null) {
            return 0;
        } else {
            int scale = decimal.scale();
            return scale >= -100 && scale <= 100 ? decimal.byteValue() : decimal.byteValueExact();
        }
    }

    public static short shortValue(BigDecimal decimal) {
        if (decimal == null) {
            return 0;
        } else {
            int scale = decimal.scale();
            return scale >= -100 && scale <= 100 ? decimal.shortValue() : decimal.shortValueExact();
        }
    }

    public static int intValue(BigDecimal decimal) {
        if (decimal == null) {
            return 0;
        } else {
            int scale = decimal.scale();
            return scale >= -100 && scale <= 100 ? decimal.intValue() : decimal.intValueExact();
        }
    }

    public static long longValue(BigDecimal decimal) {
        if (decimal == null) {
            return 0L;
        } else {
            int scale = decimal.scale();
            return scale >= -100 && scale <= 100 ? decimal.longValue() : decimal.longValueExact();
        }
    }

    public static Integer castToInt(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Integer) {
            return (Integer)value;
        } else if (value instanceof BigDecimal) {
            return intValue((BigDecimal)value);
        } else if (value instanceof Number) {
            return ((Number)value).intValue();
        } else if (value instanceof String) {
            String strVal = (String)value;
            if (strVal.length() != 0 && !"null".equals(strVal) && !"NULL".equals(strVal)) {
                if (strVal.indexOf(44) != 0) {
                    strVal = strVal.replaceAll(",", "");
                }

                return Integer.parseInt(strVal);
            } else {
                return null;
            }
        } else if (value instanceof Boolean) {
            return (Boolean)value ? 1 : 0;
        } else {
            if (value instanceof Map) {
                Map map = (Map)value;
                if (map.size() == 2 && map.containsKey("andIncrement") && map.containsKey("andDecrement")) {
                    Iterator iter = map.values().iterator();
                    iter.next();
                    Object value2 = iter.next();
                    return castToInt(value2);
                }
            }

            throw new UtilException("can not cast to int, value : " + value);
        }
    }

    public static byte[] castToBytes(Object value) {
        if (value instanceof byte[]) {
            return (byte[])((byte[])value);
        } else if (value instanceof String) {
            return IOUtils.decodeBase64((String)value);
        } else {
            throw new UtilException("can not cast to int, value : " + value);
        }
    }

    public static Boolean castToBoolean(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Boolean) {
            return (Boolean)value;
        } else if (value instanceof BigDecimal) {
            return intValue((BigDecimal)value) == 1;
        } else if (value instanceof Number) {
            return ((Number)value).intValue() == 1;
        } else if (value instanceof String) {
            String strVal = (String)value;
            if (strVal.length() != 0 && !"null".equals(strVal) && !"NULL".equals(strVal)) {
                if (!"true".equalsIgnoreCase(strVal) && !"1".equals(strVal)) {
                    if (!"false".equalsIgnoreCase(strVal) && !"0".equals(strVal)) {
                        if (!"Y".equalsIgnoreCase(strVal) && !"T".equals(strVal)) {
                            if (!"F".equalsIgnoreCase(strVal) && !"N".equals(strVal)) {
                                throw new UtilException("can not cast to boolean, value : " + value);
                            } else {
                                return Boolean.FALSE;
                            }
                        } else {
                            return Boolean.TRUE;
                        }
                    } else {
                        return Boolean.FALSE;
                    }
                } else {
                    return Boolean.TRUE;
                }
            } else {
                return null;
            }
        } else {
            throw new UtilException("can not cast to boolean, value : " + value);
        }
    }

    public static Locale toLocale(String strVal) {
        String[] items = strVal.split("_");
        if (items.length == 1) {
            return new Locale(items[0]);
        } else {
            return items.length == 2 ? new Locale(items[0], items[1]) : new Locale(items[0], items[1], items[2]);
        }
    }

    public static double parseDouble(String str) {
        int len = str.length();
        if (len > 10) {
            return Double.parseDouble(str);
        } else {
            boolean negative = false;
            long longValue = 0L;
            int scale = 0;

            for(int i = 0; i < len; ++i) {
                char ch = str.charAt(i);
                if (ch == '-' && i == 0) {
                    negative = true;
                } else if (ch == '.') {
                    if (scale != 0) {
                        return Double.parseDouble(str);
                    }

                    scale = len - i - 1;
                } else {
                    if (ch < '0' || ch > '9') {
                        return Double.parseDouble(str);
                    }

                    int digit = ch - 48;
                    longValue = longValue * 10L + (long)digit;
                }
            }

            if (negative) {
                longValue = -longValue;
            }

            switch(scale) {
                case 0:
                    return (double)longValue;
                case 1:
                    return (double)longValue / 10.0D;
                case 2:
                    return (double)longValue / 100.0D;
                case 3:
                    return (double)longValue / 1000.0D;
                case 4:
                    return (double)longValue / 10000.0D;
                case 5:
                    return (double)longValue / 100000.0D;
                case 6:
                    return (double)longValue / 1000000.0D;
                case 7:
                    return (double)longValue / 1.0E7D;
                case 8:
                    return (double)longValue / 1.0E8D;
                case 9:
                    return (double)longValue / 1.0E9D;
                default:
                    return Double.parseDouble(str);
            }
        }
    }

    public static float parseFloat(String str) {
        int len = str.length();
        if (len >= 10) {
            return Float.parseFloat(str);
        } else {
            boolean negative = false;
            long longValue = 0L;
            int scale = 0;

            for(int i = 0; i < len; ++i) {
                char ch = str.charAt(i);
                if (ch == '-' && i == 0) {
                    negative = true;
                } else if (ch == '.') {
                    if (scale != 0) {
                        return Float.parseFloat(str);
                    }

                    scale = len - i - 1;
                } else {
                    if (ch < '0' || ch > '9') {
                        return Float.parseFloat(str);
                    }

                    int digit = ch - 48;
                    longValue = longValue * 10L + (long)digit;
                }
            }

            if (negative) {
                longValue = -longValue;
            }

            switch(scale) {
                case 0:
                    return (float)longValue;
                case 1:
                    return (float)longValue / 10.0F;
                case 2:
                    return (float)longValue / 100.0F;
                case 3:
                    return (float)longValue / 1000.0F;
                case 4:
                    return (float)longValue / 10000.0F;
                case 5:
                    return (float)longValue / 100000.0F;
                case 6:
                    return (float)longValue / 1000000.0F;
                case 7:
                    return (float)longValue / 1.0E7F;
                case 8:
                    return (float)longValue / 1.0E8F;
                case 9:
                    return (float)longValue / 1.0E9F;
                default:
                    return Float.parseFloat(str);
            }
        }
    }
}
