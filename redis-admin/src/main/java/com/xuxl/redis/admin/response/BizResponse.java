package com.xuxl.redis.admin.response;

import com.xuxl.redis.admin.utils.CodeConstants;

import java.io.Serializable;


public class BizResponse implements Serializable {

    private static final long serialVersionUID = 6914352942940829668L;

    private int code;

    private String msg;

    private Object data;

    public BizResponse() {
        this.code = CodeConstants.SUCCESS_CODE;
        this.msg = CodeConstants.SUCCESS_MSG;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
