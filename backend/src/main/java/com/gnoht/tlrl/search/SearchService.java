package com.gnoht.tlrl.search;

import org.springframework.data.domain.Pageable;

/**
 * @param <T>
 * @author ikumen@gnoht.com
 */
public interface SearchService<T, E> {

  T search(String terms, E filters, Pageable pageable);
}