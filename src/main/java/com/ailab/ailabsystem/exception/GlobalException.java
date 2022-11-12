package com.ailab.ailabsystem.exception;

import com.ailab.ailabsystem.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;


/**
 * @author xiaozhi
 * @description 全局异常拦截处理
 * @create 2022-10-2022/10/21 12:12
 */
@RestControllerAdvice
@Slf4j
public class GlobalException {

    /**
     * 拦截自定义异常
     */
    @ExceptionHandler(CustomException.class)
    public R returnErrorInfo(CustomException e) {
        return R.error(e.getCode(), e.getMessage());
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(BindException.class)
    public R returnErrorInfo(BindException e) {
        String message = e.getFieldErrors().get(0).getDefaultMessage();
        return R.error(message);
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R returnErrorInfo(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return R.error(message);
    }

    /**
     * 文件超过指定大小异常
     */
    @ExceptionHandler({MultipartException.class})
    public R getSizeOverError(MaxUploadSizeExceededException e) {
        return R.error(e.getMessage());
    }

    /**
     * 服务器异常
     */
    @ExceptionHandler(RuntimeException.class)
    public R runtimeException(RuntimeException e) {
        log.error("服务器异常：{}", e.getMessage());
        return R.error("服务器异常，请稍等~~~");
    }

}
