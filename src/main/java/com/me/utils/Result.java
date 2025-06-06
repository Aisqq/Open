package com.me.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private boolean flag;
    private String message;
    private T data;
    public Result(boolean flag,String message){
        this.flag = flag;
        this.message = message;
    }

    public static <T> Result<T> success(String message,T data) {
        return new Result<>(true, message, data);
    }
    public static <T> Result<T> success(String message) {
        return new Result<>(true, message);
    }
    public static <T> Result<T> error(String message) {
        return new Result<>(false, message);
    }
}
