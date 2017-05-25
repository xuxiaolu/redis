package com.ymm.redis.common.zookeeper;


class ZkException extends RuntimeException {

    private static final long serialVersionUID = -6244510255228040310L;

    ZkException(String message, Throwable cause) {
        super(message, cause);
    }
}
