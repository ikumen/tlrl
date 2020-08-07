package com.gnoht.tlrl.user;

import com.gnoht.tlrl.core.AlreadyExistsException;

/**
 * @author ikumen@gnoht.com
 */
public class UserAlreadyExistsException extends AlreadyExistsException {
  private static final long serialVersionUID = 1L;

  public UserAlreadyExistsException(User user) {
    super("User (" + user.getEmail() + ") already exists!");
  }
}
