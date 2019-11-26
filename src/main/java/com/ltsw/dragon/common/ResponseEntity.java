package com.ltsw.dragon.common;

import lombok.Data;

/**
 * @author heshaobing
 */
@Data
public class ResponseEntity<T> {

    private int code;
    private String message;
    private T body;

    private ResponseEntity(HttpStatus status, String message) {
        this(status, message, null);
    }

    private ResponseEntity(HttpStatus status, String message, T body) {
        this.code = status.value();
        this.message = message;
        this.body = body;
    }

    public static ResponseEntity success() {
        return success(null);
    }

    public static ResponseEntity success(String message) {
        return new ResponseEntity(HttpStatus.SUCCESS, message);
    }

    public static <T> ResponseEntity success(T body) {
        return success(body, null);
    }

    public static <T> ResponseEntity success(T body, String message) {
        return new ResponseEntity<>(HttpStatus.SUCCESS, message, body);
    }

    public static ResponseEntity fail() {
        return fail(null);
    }

    public static ResponseEntity fail(String message) {
        return new ResponseEntity(HttpStatus.FAIL, message);
    }

    public static <T> ResponseEntity fail(T body) {
        return fail(body, null);
    }

    public static <T> ResponseEntity fail(T body, String message) {
        return new ResponseEntity<>(HttpStatus.FAIL, message, body);
    }

    /**
     * Enumeration of HTTP status .
     * <p>Retrievable via {@link ResponseEntity#code}.
     */
    public enum HttpStatus {
        /**
         * 成功
         */
        SUCCESS(0),
        /**
         * 失败
         */
        FAIL(-1);

        private final int value;

        HttpStatus(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }

        public static HttpStatus valueOf(int statusCode) {
            HttpStatus status = resolve(statusCode);
            if (status == null) {
                throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
            }
            return status;
        }

        public static HttpStatus resolve(int statusCode) {
            for (HttpStatus status : values()) {
                if (status.value == statusCode) {
                    return status;
                }
            }
            return null;
        }
    }

}
