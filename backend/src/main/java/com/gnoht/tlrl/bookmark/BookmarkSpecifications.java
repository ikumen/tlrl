package com.gnoht.tlrl.bookmark;

import com.gnoht.tlrl.user.User;

import java.util.Set;

/**
 * @author ikumen@gnoht.com
 */
public interface BookmarkSpecifications {
  
  BookmarkSpecifications isOwnedBy(User owner);
  BookmarkSpecifications isPublic();
  BookmarkSpecifications isPrivate();
  BookmarkSpecifications hasReadStatus(ReadStatus status);
  BookmarkSpecifications taggedWith(Set<Tag> tags);
  BookmarkSpecifications notTaggedWith(Set<Tag> tags);
  Object toPredicate();
  
}
