package org.example.model;

public class I18nException extends RuntimeException{

    private final int status;
    private final String code;
    private final String message;

    public I18nException(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
