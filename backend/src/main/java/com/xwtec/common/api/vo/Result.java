package com.xwtec.common.api.vo;

import com.xwtec.common.constant.CommonConstant;
import com.xwtec.common.exception.VerificationException;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口返回数据格式
 *
 * @author scott
 * @email jeecgos@163.com
 * @date 2019年1月19日
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    private boolean success;

    /**
     * 返回处理消息
     */
    private String message;

    /**
     * 返回代码
     */
    private Integer code;

    /**
     * 返回数据对象 data
     */
    private T result;

    private Object extraData;

    public Object getExtraData() {
        return extraData;
    }

    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    public void error500(String message) {
        this.message = message;
        this.code = CommonConstant.SC_INTERNAL_SERVER_ERROR_500;
        this.success = false;
    }

    public static Result getResultForError(String message, Integer code) {
        Result result = new Result();
        result.message = message;
        result.code = code;
        result.success = false;
        return result;
    }

    public static Result getResultForError(VerificationException e) {
        return getResultForError(e.getMessage(), e.getCode());
    }

    public static Result getResultForError() {
        return getResultForError("操作失败", CommonConstant.SC_INTERNAL_SERVER_ERROR_500);
    }

    public void success(String message) {
        this.message = message;
        this.code = CommonConstant.SC_OK_200;
        this.success = true;
    }

    public static Result getResultForSuccess(String message, Object obj) {
        Result result = new Result();
        result.message = "操作成功";
        result.code = CommonConstant.SC_OK_200;
        result.success = true;
        result.setResult(obj);
        return result;
    }

    public static Result getResultForSuccess(Object obj) {
        return getResultForSuccess("操作成功", obj);
    }

    public static Result getResultForSuccess() {
        return getResultForSuccess("操作成功");
    }

    public static Result<Object> error(String msg) {
        return error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, msg);
    }

    public static Result<Object> error(int code, String msg) {
        Result<Object> r = new Result<Object>();
        r.setCode(code);
        r.setMessage(msg);
        r.setSuccess(false);
        return r;
    }

    public static Result<Object> ok(String msg) {
        Result<Object> r = new Result<Object>();
        r.setSuccess(true);
        r.setCode(CommonConstant.SC_OK_200);
        r.setMessage(msg);
        return r;
    }

    public static Result<Object> ok(Object obj) {
        Result<Object> r = new Result<Object>();
        r.setSuccess(true);
        r.setCode(CommonConstant.SC_OK_200);
        r.setResult(obj);
        return r;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
