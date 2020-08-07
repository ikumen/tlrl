package com.gnoht.tlrl.core;

/**
 * @author ikumen@gnoht.com
 */
public abstract class AlreadyExistsException extends RuntimeException {

  public AlreadyExistsException(String message) {
    super(message);
  }

  public AlreadyExistsException(String message, Throwable cause) {
    super(message, cause);
  }

  public AlreadyExistsException(Throwable cause) {
    super(cause);
  }
}
