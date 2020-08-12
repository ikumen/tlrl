package com.gnoht.tlrl.search;

import java.util.Map;

import com.gnoht.tlrl.user.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @param <T>
 * @author ikumen@gnoht.com
 */
public interface SearchService<T> {
  Page<T> search(String terms, Map<String, Object> filters, User user, Pageable pageable);
}