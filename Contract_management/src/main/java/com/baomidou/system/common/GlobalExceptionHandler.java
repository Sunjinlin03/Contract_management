package com.baomidou.system.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler 类用于处理应用程序中未捕获的异常，
 * 并返回标准化的 API 响应。通过 @RestControllerAdvice 注解，
 * 该类可以在整个应用程序中拦截控制器层的异常。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常 BusinessException。
     * 当业务逻辑中抛出 BusinessException 时，会被此方法捕获并处理。
     *
     * @param ex 捕获到的 BusinessException 实例
     * @return 返回封装后的 ApiResponse 对象，包含错误码和错误信息
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 返回 HTTP 400 状态码
    public ApiResponse<String> handleBusinessException(BusinessException ex) {
        // 返回包含自定义错误码和消息的 ApiResponse
        return ApiResponse.error(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理所有其他未被单独捕获的异常。
     * 如果应用程序中出现任何未捕获的异常（除 BusinessException 之外），
     * 将被此方法捕获并处理。
     *
     * @param ex 捕获到的 Exception 实例
     * @return 返回封装后的 ApiResponse 对象，包含通用的错误信息
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 返回 HTTP 500 状态码
    public ApiResponse<String> handleException(Exception ex) {
        // 打印异常栈跟踪信息，以便调试和日志记录
        ex.printStackTrace();
        // 返回包含通用错误信息的 ApiResponse
        return ApiResponse.error(500, "服务器内部错误");
    }
}

