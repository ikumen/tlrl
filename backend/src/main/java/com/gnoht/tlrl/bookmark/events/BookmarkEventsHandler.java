package com.gnoht.tlrl.bookmark.events;

import java.util.List;

import com.gnoht.tlrl.bookmark.Bookmark;

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
