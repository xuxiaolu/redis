package com.xuxl.redis.client.exception;

public class ClientException extends RuntimeException {

    private static final long serialVersionUID = 6685594728683482078L;

    public ClientException(String msg) {
        super(msg);
    }

}
