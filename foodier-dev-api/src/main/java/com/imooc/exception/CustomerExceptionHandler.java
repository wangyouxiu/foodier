package com.imooc.exception;

import com.imooc.utils.IMOOCJSONResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * @version 1.0
 * @ClassName CustomerExceptionHandler
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/3/4 22:40
 **/
@RestControllerAdvice
public class CustomerExceptionHandler {
    /**
     * 捕获上传文件内容过大时产生的异常
     * @param ex
     * @return
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public IMOOCJSONResult handlerMaxUploadFile(MaxUploadSizeExceededException ex) {
        return IMOOCJSONResult.errorMsg("文件大小不得超过50kb,请重试");
    }
}
