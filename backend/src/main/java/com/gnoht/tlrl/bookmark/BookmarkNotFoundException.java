package com.gnoht.tlrl.bookmark;

import com.gnoht.tlrl.core.NotFoundException;

/**
 * @author ikumen@gnoht.com
 */
public class BookmarkNotFoundException extends NotFoundException {
  public BookmarkNotFoundException() {
    super("Bookmark does not exists!");
  }
}
