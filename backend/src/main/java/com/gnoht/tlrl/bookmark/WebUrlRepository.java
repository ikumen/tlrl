package com.gnoht.tlrl.bookmark;

import java.util.Optional;

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
