package com.activiti.utils.exception;

import com.activiti.utils.R;
import com.activiti.utils.constant.HttpStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 异常处理器
 * @author liuguofeng
 * @date 2023/10/18 11:28
 */
@Log4j2
@RestControllerAdvice
public class AExceptionHandler {
    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public R<String> handleBindException(BindException e) {
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return R.fail(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return R.fail(message);
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(AException.class)
    public R<String> handleException(AException e) {
        return R.fail(e.getMessage());
    }

    /**
     * 路径不存在
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public R<String> handlerNoFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return R.fail(HttpStatus.NOT_FOUND, "路径不存在，请检查路径是否正确");
    }

    /**
     * 数据库添加主键重复
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public R<String> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return R.fail("数据库中已存在该记录");
    }

    /**
     * 未知异常
     */
    @ExceptionHandler(Exception.class)
    public R<String> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return R.fail("未知异常!请联系管理员");
    }

}
