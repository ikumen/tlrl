/**
 * 
 */
package com.gnoht.tlrl.bookmark.jpa;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.gnoht.tlrl.bookmark.BookmarkSpecifications;
import com.gnoht.tlrl.bookmark.QBookmark;
import com.gnoht.tlrl.bookmark.ReadStatus;
import com.gnoht.tlrl.bookmark.SharedStatus;
import com.gnoht.tlrl.bookmark.Tag;
import com.gnoht.tlrl.support.jpa.QuerydslSpecification;
import com.gnoht.tlrl.user.User;
import com.querydsl.core.types.Predicate;

/**
 * @author ikumen
 */
@Component
public class QuerydslBookmarkSpecifications implements BookmarkSpecifications {

  static QuerydslBookmarkSpecifications get() {
    return new QuerydslBookmarkSpecifications();
  }
  
  private QBookmark _bookmark = QBookmark.bookmark;
  private QuerydslSpecification specification = new QuerydslSpecification();

  @Override
  public Predicate toPredicate() {
    return specification.toPredicate();
  }

  @Override
  public BookmarkSpecifications isOwnedBy(User owner) {
    specification.and(_bookmark.owner.eq(owner)); 
    return this;
  }

  @Override
  public BookmarkSpecifications isPublic() {
    specification.and(_bookmark.sharedStatus.eq(SharedStatus.PUBLIC));
    return this;
  }

  @Override
  public BookmarkSpecifications isPrivate() {
    specification.and(_bookmark.sharedStatus.eq(SharedStatus.PRIVATE));
    return this;
  }

  @Override
  public BookmarkSpecifications hasReadStatus(ReadStatus status) {
    specification.and(_bookmark.readStatus.eq(status));
    return this;
  }

  @Override
  public BookmarkSpecifications taggedWith(Set<Tag> tags) {
    tags.forEach(tag -> specification
      .and(_bookmark.tags.any().id.id.eq(tag.getId())));
    return this;
  }

  @Override
  public BookmarkSpecifications notTaggedWith(Set<Tag> tags) {
    tags.forEach(tag -> specification
        .and(_bookmark.tags.any().id.id.ne(tag.getId())));
      return this;
  }

}
