/**
 * 
 */
package com.gnoht.tlrl.core;

/**
 * @author ikumen
 */
public class TlrlException extends RuntimeException {
  public TlrlException(String message) {
    super(message);
  }

  public TlrlException(String message, Throwable cause) {
    super(message, cause);
  }

  public TlrlException(Throwable cause) {
    super(cause);
  }
}
