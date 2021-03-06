package com.bei.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Administrator
 * @date 2020/8/17 0017 - 16:51
 */


//HTTP状态码
@ResponseStatus(HttpStatus.NOT_FOUND)
//自定义NotFoundException异常,会跳转到404页面
public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
