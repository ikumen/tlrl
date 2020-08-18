package com.gnoht.tlrl.bookmark;

/**
 * The status indicating a Bookmarks privacy setting.
 *
 * @author ikumen@gnoht.com
 */
public enum SharedStatus {
  // Note: Must maintain order, we are using JPA EnumType.ORDINAL

  /**
   * Status indicating Bookmark should not be shared.
   */
  PRIVATE,

  /**
   * Status indicating Bookmark can be shared.
   */
  PUBLIC;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
