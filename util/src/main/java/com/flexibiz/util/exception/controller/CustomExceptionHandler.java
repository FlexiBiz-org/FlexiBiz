package com.flexibiz.util.exception.controller;

import com.flexibiz.util.exception.model.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody CustomException handeException (ServerHttpRequest request, Exception ex) {
        return new CustomException(
                request.getPath().pathWithinApplication().value(),
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }
}
