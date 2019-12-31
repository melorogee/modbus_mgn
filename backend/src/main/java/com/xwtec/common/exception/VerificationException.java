package com.xwtec.common.exception;

import com.xwtec.common.constant.CommonConstant;

public class VerificationException extends Exception {
    private Integer code;

    public VerificationException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public VerificationException(String message) {
        super(message);
        this.code = CommonConstant.SC_INTERNAL_SERVER_ERROR_500;
        ;
    }

    public Integer getCode() {
        return code;
    }
}
