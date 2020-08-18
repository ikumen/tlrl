package com.gnoht.tlrl.bookmark;

/**
 * The status indicating if a {@link Bookmark} should be read (e.g, reading list).
 *
 * @author ikumen@gnoht.com
 */
public enum ReadStatus {
  // Note: Must maintain order, we are using JPA EnumType.ORDINAL

  /**
   * Status indicating Bookmark was bookmarked but not flagged to be read.
   */
  NA,

  /**
   * Status indicating Bookmark if flagged to be read.
   */
  UNREAD,

  /**
   * Status indicating Bookmark has been read.
   */
  READ;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
