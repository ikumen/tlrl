package com.gnoht.tlrl.user;

import com.gnoht.tlrl.core.AlreadyExistsException;

/**
 * @author ikumen@gnoht.com
 */
public final class UsernameAlreadyExistsException extends AlreadyExistsException {
  private static final long serialVersionUID = 1L;

  public UsernameAlreadyExistsException(String name) {
    super("User name '" + name + "' already taken!");
  }
}
