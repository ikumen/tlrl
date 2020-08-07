package com.gnoht.tlrl.bookmark.jpa;

import com.gnoht.tlrl.bookmark.Bookmark;
import com.gnoht.tlrl.bookmark.BookmarkEventsHandler;
import com.gnoht.tlrl.support.ApplicationContextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;

/**
 * Listen for {@link Bookmark} persistence events, and map them to {@link BookmarkEventsHandler}.
 *
 * @author ikumen@gnoht.com
 */
public final class JpaBookmarkListener {

  private final static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private BookmarkEventsHandler bookmarkEventsHandler;

  private BookmarkEventsHandler getBookmarkEventsListener() {
    if (bookmarkEventsHandler == null)
      bookmarkEventsHandler = ApplicationContextHelper.getBean(BookmarkEventsHandler.class);
    return bookmarkEventsHandler;
  }

  @PostPersist
  public void postPersist(Bookmark bookmark) {
    LOG.info("New bookmark created: {}", bookmark);
    getBookmarkEventsListener().onCreated(bookmark);
  }

  @PostRemove
  public void postRemove(Bookmark bookmark) {
    LOG.info("Bookmark removed: {}", bookmark);
    getBookmarkEventsListener().onDeleted(Arrays.asList(bookmark));
  }

  @PostUpdate
  public void postUpdate(Bookmark bookmark) {
    LOG.info("Bookmark updated: {}", bookmark);
    getBookmarkEventsListener().onUpdated(Arrays.asList(bookmark));
  }
}
