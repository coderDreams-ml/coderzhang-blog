package com.coderzhang.blog.common;

import lombok.Data;

/** 统一响应体 */
@Data
public class Result<T> {
    private int code;      // 0 成功，非 0 失败
    private String message;
    private T data;

    public static <T> Result<T> ok(T data) { return build(0, "ok", data); }
    public static Result<Void> ok() { return build(0, "ok", null); }
    public static <T> Result<T> fail(String message) { return build(1, message, null); }
    public static <T> Result<T> build(int code, String message, T data) {
        Result<T> r = new Result<>();
        r.setCode(code); r.setMessage(message); r.setData(data);
        return r;
    }
}
