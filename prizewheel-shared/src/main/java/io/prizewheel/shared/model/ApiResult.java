package io.prizewheel.shared.model;

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
        return new ApiResult<>("0000", "操作成功");
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>("0000", "操作成功", data);
    }

    public static <T> ApiResult<T> success(String message, T data) {
        return new ApiResult<>("0000", message, data);
    }

    public static <T> ApiResult<T> fail(String message) {
        return new ApiResult<>("0001", message);
    }

    public static <T> ApiResult<T> fail(String code, String message) {
        return new ApiResult<>(code, message);
    }

    public boolean isSuccess() {
        return "0000".equals(this.code);
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
