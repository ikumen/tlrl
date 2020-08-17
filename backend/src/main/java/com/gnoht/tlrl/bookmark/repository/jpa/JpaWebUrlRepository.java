package com.gnoht.tlrl.bookmark.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gnoht.tlrl.bookmark.WebUrl;
import com.gnoht.tlrl.bookmark.repository.WebUrlRepository;

/**
 * @author ikumen@gnoht.com
 */
public interface JpaWebUrlRepository extends JpaRepository<WebUrl, Long>, WebUrlRepository {
}
