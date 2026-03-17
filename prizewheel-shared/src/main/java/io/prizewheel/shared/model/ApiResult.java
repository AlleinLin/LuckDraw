package io.prizewheel.shared.model;

import io.prizewheel.shared.constant.SystemConst;

import java.io.Serializable;

/**
 * 统一响应结果
 * 
 * @author Allein
 * @since 1.0.0
 */
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;
    private String message;
    private T data;

    public ApiResult() {}

    public ApiResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResult(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResult<T> success() {
        return new ApiResult<>(SystemConst.SUCCESS_CODE, SystemConst.SUCCESS_MSG);
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(SystemConst.SUCCESS_CODE, SystemConst.SUCCESS_MSG, data);
    }

    public static <T> ApiResult<T> success(String message, T data) {
        return new ApiResult<>(SystemConst.SUCCESS_CODE, message, data);
    }

    public static <T> ApiResult<T> fail(String message) {
        return new ApiResult<>(SystemConst.ERROR_CODE, message);
    }

    public static <T> ApiResult<T> fail(String code, String message) {
        return new ApiResult<>(code, message);
    }

    public static <T> ApiResult<T> unavailable() {
        return new ApiResult<>(SystemConst.UNAVAILABLE_CODE, SystemConst.UNAVAILABLE_MSG);
    }

    public boolean isSuccess() {
        return SystemConst.SUCCESS_CODE.equals(this.code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
