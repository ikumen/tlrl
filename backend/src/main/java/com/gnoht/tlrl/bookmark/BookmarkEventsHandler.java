package com.gnoht.tlrl.bookmark;

import com.gnoht.tlrl.user.User;

import java.util.Collection;
import java.util.List;

/**
 * @author ikumen@gnoht.com
 */
public interface BookmarkEventsHandler {

  /**
   * Handler for newly created {@link Bookmark}.
   * @param bookmark
   */
  void onCreated(Bookmark bookmark);

  /**
   * Handler for recently updated {@link Bookmark}s
   * @param bookmarks
   */
  void onUpdated(List<Bookmark> bookmarks);

  /**
   * Handler for recently deleted {@link Bookmark}s.
   * @param bookmarks
   */
  void onDeleted(List<Bookmark> bookmarks);

  /**
   * Handler for recently archived {@link Bookmark}s.
   * @param bookmark
   */
  void onArchived(Bookmark bookmark);
}
