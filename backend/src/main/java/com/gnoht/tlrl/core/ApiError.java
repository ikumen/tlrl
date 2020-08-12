package com.gnoht.tlrl.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

/**
 * @author ikumen@gnoht.com
 */
public class ApiError {
  private HttpStatus status;
  private String message;
  private List<String> errors;

  public ApiError(HttpStatus status, String message, List<String> errors) {
    this.status = status;
    this.message = message;
    this.errors = errors;
  }
  
  public static Builder builder() {
    return new Builder();
  }
  
  public HttpStatus getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  public List<String> getErrors() {
    return errors;
  }
  

  public static class Builder {
    private HttpStatus status;
    private String message;
    private List<String> errors;

    private Builder() {}
    
    public Builder status(HttpStatus status) {
      this.status = status; return this;
    }
    
    public Builder message(String message) {
      this.message = message; return this;
    }
    
    public Builder errors(List<String> errors) {
      this.errors = errors; return this;
    }
    public Builder error(String name, String message) {
      return error(name + ": " + message);
    }
    
    public Builder error(String error) {
      if (errors == null)
        errors = new ArrayList<>();
      errors.add(error); 
      return this;
    }
    
    public ApiError build() {
      return new ApiError(status, message, errors);
    }
  }
}