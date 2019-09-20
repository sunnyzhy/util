package com.zhy.util.exception;

/**
 * @author zhy
 * @date 2019/9/19 15:34
 */
public class UtilException extends RuntimeException {
    public UtilException() {
    }

    public UtilException(String message) {
        super(message);
    }

    public UtilException(String message, Throwable cause) {
        super(message, cause);
    }
}