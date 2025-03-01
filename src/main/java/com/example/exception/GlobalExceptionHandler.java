package com.example.exception;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleIllegalArgumentException(IllegalArgumentException e) {
    return e.getMessage();
  }

  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String handleNoSuchElementException(NoSuchElementException e) {
    return e.getMessage();
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.OK)
  public String handleRuntimeException(RuntimeException e) {
    return e.getMessage();
  }

}
