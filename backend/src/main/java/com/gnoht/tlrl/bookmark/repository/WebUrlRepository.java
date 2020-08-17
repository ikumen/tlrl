package com.gnoht.tlrl.bookmark.repository;

import java.util.Optional;

import com.gnoht.tlrl.bookmark.WebUrl;

/**
 * @author ikumen@gnoht.com
 */
public interface WebUrlRepository {
  /**
   * Find {@link WebUrl} by given url.
   * @param url
   * @return
   */
  Optional<WebUrl> findOneByUrl(String url);
}
