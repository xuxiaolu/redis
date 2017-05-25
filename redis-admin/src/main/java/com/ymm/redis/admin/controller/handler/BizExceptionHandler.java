package com.ymm.redis.admin.controller.handler;


import com.ymm.redis.admin.exception.BizException;
import com.ymm.redis.admin.response.BizResponse;
import com.ymm.redis.admin.utils.CodeConstants;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class BizExceptionHandler {


    @ExceptionHandler(BizException.class)
    @ResponseBody
    @Order(1)
    public BizResponse response(BizException e) {
        BizResponse response = new BizResponse();
        response.setCode(e.getCode());
        response.setMsg(e.getMsg());
        return response;
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    @Order(2)
    public BizResponse response(Exception e) {
        BizResponse response = new BizResponse();
        response.setCode(CodeConstants.ERROR_CODE);
        response.setMsg(CodeConstants.ERROR_MSG);
        return response;
    }

}
