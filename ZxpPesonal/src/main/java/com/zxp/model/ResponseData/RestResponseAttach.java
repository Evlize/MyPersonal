package com.zxp.model.ResponseData;

/**
 * rest返回对象
 *
 * @param <T>
 */
public class RestResponseAttach<T> {

    /**
     * 服务器响应数据
     */
    private T payload;

    /**
     * 请求是否成功
     */
    private boolean success;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 状态码
     */
    private int code = -1;

    /**
     * 服务器响应时间
     */
    private long timestamp;

    public RestResponseAttach() {
        this.timestamp = System.currentTimeMillis() / 1000;
    }

    public RestResponseAttach(boolean success) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
    }

    public RestResponseAttach(boolean success, T payload) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.payload = payload;
    }

    public RestResponseAttach(boolean success, T payload, int code) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.payload = payload;
        this.code = code;
    }

    public RestResponseAttach(boolean success, String msg) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.msg = msg;
    }

    public RestResponseAttach(boolean success, String msg, int code) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.msg = msg;
        this.code = code;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static RestResponseAttach ok() {
        return new RestResponseAttach(true);
    }

    public static <T> RestResponseAttach ok(T payload) {
        return new RestResponseAttach(true, payload);
    }

    public static <T> RestResponseAttach ok(int code) {
        return new RestResponseAttach(true, null, code);
    }

    public static <T> RestResponseAttach ok(T payload, int code) {
        return new RestResponseAttach(true, payload, code);
    }

    public static RestResponseAttach fail() {
        return new RestResponseAttach(false);
    }

    public static RestResponseAttach fail(String msg) {
        return new RestResponseAttach(false, msg);
    }

    public static RestResponseAttach fail(int code) {
        return new RestResponseAttach(false, null, code);
    }

    public static RestResponseAttach fail(int code, String msg) {
        return new RestResponseAttach(false, msg, code);
    }

}