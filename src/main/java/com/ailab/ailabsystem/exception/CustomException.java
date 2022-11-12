package com.ailab.ailabsystem.exception;

import com.ailab.ailabsystem.enums.ResponseStatusEnum;

/**
 * @author xiaozhi
 * @description 自定义异常
 * @create 2022-10-2022/10/21 12:05
 */
public class CustomException extends RuntimeException{

    private int code;

    public CustomException(ResponseStatusEnum responseStatusEnum) {
        super(responseStatusEnum.msg());
        this.code = responseStatusEnum.status();
    }

    public int getCode() {
        return code;
    }
}
