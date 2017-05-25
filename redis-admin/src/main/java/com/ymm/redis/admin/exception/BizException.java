package com.ymm.redis.admin.exception;


public class BizException extends RuntimeException {

    private static final long serialVersionUID = 2398237039893178106L;

    private int code;

    private String msg;

    public BizException() {

    }

    public BizException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
