/**
 * 
 */
package com.gnoht.tlrl.bookmark.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gnoht.tlrl.bookmark.Bookmark;
import com.gnoht.tlrl.bookmark.BookmarkFacets;
import com.gnoht.tlrl.bookmark.repository.BookmarkQueryFilter;
import com.gnoht.tlrl.user.User;

/**
 * @author ikumen
 */
public interface BookmarkRepositoryExtension {
  
  BookmarkFacets findAllFacets(User user, BookmarkQueryFilter queryFilter);
  Page<Bookmark> findAll(User user, BookmarkQueryFilter queryFilter, Pageable pageable);
}
