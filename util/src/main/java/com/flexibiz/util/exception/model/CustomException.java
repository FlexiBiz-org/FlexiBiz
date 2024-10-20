package com.flexibiz.util.exception.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomException {

    private ZonedDateTime timestamp;
    private String path;
    private HttpStatus status;
    private String message;

    public CustomException(String path, HttpStatus status, String message) {
        this.path = path;
        this.message = message;
        this.status = status;
        this.timestamp = ZonedDateTime.now();

    }

}
