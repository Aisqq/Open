package com.me.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理参数校验异常（方法级别）
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("参数校验异常: {}", ex.getMessage());
        return buildErrorResponse(400, ex.getMessage());
    }

    // 处理参数校验异常（对象级别）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("参数校验异常: {}", ex.getMessage());
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return buildErrorResponse(400, errorMessage);
    }

    // 处理参数类型不匹配异常
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error("参数类型不匹配异常: {}", ex.getMessage());
        String errorMessage = String.format("参数类型不匹配，%s必须是%s类型",
                ex.getName(), ex.getRequiredType().getSimpleName());
        return buildErrorResponse(400, errorMessage);
    }

    // 处理请求体解析异常
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("请求体解析异常: {}", ex.getMessage());
        return buildErrorResponse(400, "无效的请求体格式");
    }

    // 处理其他未明确处理的异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleException(Exception ex) {
        log.error("请求错误", ex);
        return buildErrorResponse(500, "系统内部错误，请稍后再试");
    }

    private Map<String, Object> buildErrorResponse(int code, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("message", message);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}