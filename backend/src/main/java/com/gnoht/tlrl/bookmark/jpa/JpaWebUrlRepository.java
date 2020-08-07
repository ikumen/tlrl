package com.gnoht.tlrl.bookmark.jpa;

import com.gnoht.tlrl.bookmark.WebUrl;
import com.gnoht.tlrl.bookmark.WebUrlRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ikumen@gnoht.com
 */
public interface JpaWebUrlRepository extends JpaRepository<WebUrl, Long>, WebUrlRepository {
}
