/**
 * 
 */
package com.gnoht.tlrl.bookmark.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gnoht.tlrl.bookmark.Bookmark;
import com.gnoht.tlrl.bookmark.BookmarkSpecifications;
import com.gnoht.tlrl.bookmark.Tag;
import com.gnoht.tlrl.user.User;

/**
 * @author ikumen
 */
public interface BookmarkRepositoryExtension {
  
  List<Tag> findRelatedTags(User user, BookmarkSpecifications specifications);
  Page<Bookmark> findAll(User user, BookmarkSpecifications specifications, Pageable pageable);
}
