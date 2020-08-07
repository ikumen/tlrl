package com.gnoht.tlrl.bookmark;

import com.gnoht.tlrl.core.AlreadyExistsException;

/**
 * @author ikumen@gnoht.com
 */
public class BookmarkAlreadyExistsException extends AlreadyExistsException {
  public BookmarkAlreadyExistsException(Bookmark bookmark) {
    super("Bookmark already exists.");
  }
}
