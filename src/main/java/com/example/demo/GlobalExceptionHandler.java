package com.example.demo;

import org.apache.commons.logging.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(WebClientResponseException.class)
  public ResponseEntity<String> handleWebClientException(WebClientResponseException ex) {
    log.error("Error: {}", ex.getMessage(), ex);
    return ResponseEntity.status(ex.getStatusCode()).body("Error occurred");
  }
}