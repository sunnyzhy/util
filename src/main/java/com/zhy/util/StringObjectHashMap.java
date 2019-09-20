package com.zhy.util;

import com.zhy.util.alibaba.TypeUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

/**
 * @author zhy
 * @date 2019/9/19 16:19
 */
public class StringObjectHashMap extends HashMap<String, Object> {
    public StringObjectHashMap(){

    }

    public Boolean getBoolean(String key) {
        Object value = this.get(key);
        return value == null ? null : TypeUtils.castToBoolean(value);
    }

    public byte[] getBytes(String key) {
        Object value = this.get(key);
        return value == null ? null : TypeUtils.castToBytes(value);
    }

    public boolean getBooleanValue(String key) {
        Object value = this.get(key);
        Boolean booleanVal = TypeUtils.castToBoolean(value);
        return booleanVal == null ? false : booleanVal;
    }

    public Byte getByte(String key) {
        Object value = this.get(key);
        return TypeUtils.castToByte(value);
    }

    public byte getByteValue(String key) {
        Object value = this.get(key);
        Byte byteVal = TypeUtils.castToByte(value);
        return byteVal == null ? 0 : byteVal;
    }

    public Short getShort(String key) {
        Object value = this.get(key);
        return TypeUtils.castToShort(value);
    }

    public short getShortValue(String key) {
        Object value = this.get(key);
        Short shortVal = TypeUtils.castToShort(value);
        return shortVal == null ? 0 : shortVal;
    }

    public Integer getInteger(String key) {
        Object value = this.get(key);
        return TypeUtils.castToInt(value);
    }

    public int getIntValue(String key) {
        Object value = this.get(key);
        Integer intVal = TypeUtils.castToInt(value);
        return intVal == null ? 0 : intVal;
    }

    public Long getLong(String key) {
        Object value = this.get(key);
        return TypeUtils.castToLong(value);
    }

    public long getLongValue(String key) {
        Object value = this.get(key);
        Long longVal = TypeUtils.castToLong(value);
        return longVal == null ? 0L : longVal;
    }

    public Float getFloat(String key) {
        Object value = this.get(key);
        return TypeUtils.castToFloat(value);
    }

    public float getFloatValue(String key) {
        Object value = this.get(key);
        Float floatValue = TypeUtils.castToFloat(value);
        return floatValue == null ? 0.0F : floatValue;
    }

    public Double getDouble(String key) {
        Object value = this.get(key);
        return TypeUtils.castToDouble(value);
    }

    public double getDoubleValue(String key) {
        Object value = this.get(key);
        Double doubleValue = TypeUtils.castToDouble(value);
        return doubleValue == null ? 0.0D : doubleValue;
    }

    public BigDecimal getBigDecimal(String key) {
        Object value = this.get(key);
        return TypeUtils.castToBigDecimal(value);
    }

    public BigInteger getBigInteger(String key) {
        Object value = this.get(key);
        return TypeUtils.castToBigInteger(value);
    }

    public String getString(String key) {
        Object value = this.get(key);
        return value == null ? null : value.toString();
    }
}
