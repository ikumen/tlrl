/**
 * 
 */
package com.gnoht.tlrl.bookmark.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gnoht.tlrl.bookmark.Bookmark;
import com.gnoht.tlrl.bookmark.BookmarkFacets;
import com.gnoht.tlrl.bookmark.BookmarkResults;
import com.gnoht.tlrl.bookmark.repository.BookmarkQueryFilter;

/**
 * @author ikumen
 */
public interface BookmarkRepositoryExtension {
  
  BookmarkFacets findAllFacets(BookmarkQueryFilter queryFilter);
  BookmarkResults findAllWithFacets(BookmarkQueryFilter queryFilter, Pageable pageable);
  Page<Bookmark> findAll(BookmarkQueryFilter queryFilter, Pageable pageable);
}
