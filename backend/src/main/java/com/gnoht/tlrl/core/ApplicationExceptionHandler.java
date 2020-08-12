package com.gnoht.tlrl.core;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author ikumen
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Custom response for {@link AlreadyExistsException} errors.
   * 
   * @param ex
   * @param headers
   * @param request
   * @return
   */
  @ExceptionHandler(value = {AlreadyExistsException.class})
  public ResponseEntity<Object> handleConflict(AlreadyExistsException ex, WebRequest request) 
  {
    HttpHeaders headers = new HttpHeaders();
    ApiError error = ApiError.builder()
        .status(HttpStatus.CONFLICT)
        .message(ex.getLocalizedMessage())
        .build();
    
    return handleExceptionInternal(ex, error, headers, error.getStatus(), request);
  }
  
  /**
   * Wrapper general validation errors as {@link ApiError}s.
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpHeaders headers, 
      HttpStatus status, WebRequest request) 
  {
    ApiError.Builder builder = ApiError.builder()
        .status(status);
        //.message(); TODO: a generic localize validation error message
    
    // Parse out for specific validation errors and include in response
    ex.getBindingResult().getFieldErrors().forEach(err ->
      builder.error(err.getField(), err.getDefaultMessage()));
    ex.getBindingResult().getGlobalErrors().forEach(err -> 
      builder.error(err.getObjectName(), err.getDefaultMessage()));
    
    return handleExceptionInternal(ex, builder.build(), headers, status, request);
  }
    
}