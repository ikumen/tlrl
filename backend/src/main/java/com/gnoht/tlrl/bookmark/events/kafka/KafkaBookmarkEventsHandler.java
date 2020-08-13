package com.gnoht.tlrl.bookmark.events.kafka;

import com.gnoht.tlrl.bookmark.Bookmark;
import com.gnoht.tlrl.bookmark.BookmarkEventsHandler;
import com.gnoht.tlrl.bookmark.BookmarkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;

/**
 * @author ikumen@gnoht.com
 */
@Service
public class KafkaBookmarkEventsHandler implements BookmarkEventsHandler {

  private final static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final KafkaTemplate<String, List<Bookmark>> kafkaTemplate;
  private final KafkaBookmarkConfig kafkaBookmarkConfig;
  private final BookmarkService bookmarkService;

  @Inject
  public KafkaBookmarkEventsHandler(
      BookmarkService bookmarkService,
      @Qualifier("bookmarkKafkaTemplate") KafkaTemplate<String, List<Bookmark>> kafkaTemplate,
      KafkaBookmarkConfig kafkaBookmarkConfig) {
    this.bookmarkService = bookmarkService;
    this.kafkaTemplate = kafkaTemplate;
    this.kafkaBookmarkConfig = kafkaBookmarkConfig;
  }

  /**
   * Handler for newly created {@link Bookmark}.
   */
  @Override
  public void onCreated(Bookmark bookmark) {
    LOG.debug("Producing created topic: {}", bookmark);
    kafkaTemplate.send(kafkaBookmarkConfig.getEventConfig().created(),
      bookmark.getOwner().getId().toString(), Collections.singletonList(bookmark));
  }

  /**
   * Handler for recently updated {@link Bookmark}s
   */
  @Override
  public void onUpdated(List<Bookmark> bookmarks) {
    LOG.debug("Producing updated topic: {}", bookmarks);
    kafkaTemplate.send(kafkaBookmarkConfig.getEventConfig().updated(),
      bookmarks.get(0).getOwner().getId().toString(), bookmarks);
  }

  /**
   * Handler for recently deleted {@link Bookmark}s.
   */
  @Override
  public void onDeleted(List<Bookmark> bookmarks) {
    LOG.debug("Producing deleted topic: {}", bookmarks);
    kafkaTemplate.send(kafkaBookmarkConfig.getEventConfig().deleted(),
        bookmarks.get(0).getOwner().getId().toString(), bookmarks);
  }

  /**
   * Handler for recently archived {@link Bookmark}s.
   */
  @KafkaListener(
      groupId = "#{kafkaBookmarkConfig.getGroupId()}",
      topics = "#{kafkaBookmarkConfig.getEventConfig().archived()}",
      containerFactory = "bookmarkKafkaListenerContainerFactory")
  @Override
  public void onArchived(Bookmark bookmark) {
    //TODO: we have a mini-cycle here where:
    //  onCreate -> fetcher -> onArchived -> update -> onUpdate -> fetcher
    // we can make onUpdate smarter to avoid calling fetcher again.
    LOG.debug("Consuming archived topic message: {}", bookmark);
    bookmarkService.update(bookmark);
  }
}
