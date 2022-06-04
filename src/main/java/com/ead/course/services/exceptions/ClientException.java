package com.ead.course.services.exceptions;


public class ClientException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ClientException(String msg) {
        super(msg);
    }


}
