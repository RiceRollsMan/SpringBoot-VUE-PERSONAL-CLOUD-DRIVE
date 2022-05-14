package com.example.youryun.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
class Exception {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public void upLpadException(MaxUploadSizeExceededException e, HttpServletResponse resp) throws IOException{
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.write("上传文件超出限制");
        out.flush();
        out.close();
    }
    @ExceptionHandler(IOException.class)
    public void IOException(IOException e, HttpServletResponse resp) throws IOException{
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.write("文件操作失败啦");
        out.flush();
        out.close();
    }
}
