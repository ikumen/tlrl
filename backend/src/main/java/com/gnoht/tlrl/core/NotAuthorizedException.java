package com.gnoht.tlrl.core;

import java.util.function.Supplier;

/**
 * @author ikumen@gnoht.com
 */
public class NotAuthorizedException extends RuntimeException {
  public NotAuthorizedException() {
    super("Not authorized");
  }

  public NotAuthorizedException(String message) {
    super(message);
  }

  public NotAuthorizedException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotAuthorizedException(Throwable cause) {
    super(cause);
  }

}
