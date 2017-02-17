package com.tmw.tracking.web.service.util.response;

/**
 * Created by pzhelnov on 2/16/2017.
 */
public class Error {
    private String message;
    private String code;

    public Error(){}

    public Error(final String message, final String code){
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}