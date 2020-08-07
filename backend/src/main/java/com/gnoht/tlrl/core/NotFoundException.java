package com.gnoht.tlrl.core;

import java.util.function.Supplier;

/**
 * @author ikumen@gnoht.com
 */
public class NotFoundException extends RuntimeException {

  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotFoundException(Throwable cause) {
    super(cause);
  }

  public static Supplier<NotFoundException> supply(String message) {
    return () -> new NotFoundException(message);
  }
}
