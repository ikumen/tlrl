/**
 * 
 */
package com.gnoht.tlrl.bookmark.repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.gnoht.tlrl.bookmark.ReadStatus;
import com.gnoht.tlrl.bookmark.SharedStatus;
import com.gnoht.tlrl.bookmark.Tag;

/**
 * @author ikumen
 */
public class BookmarkQueryFilter {
  
  private Optional<ReadStatus> readStatus = Optional.empty();
  private Optional<SharedStatus> sharedStatus = Optional.empty();
  private Set<Tag> tags = new HashSet<>();
  
  public Optional<ReadStatus> getReadStatus() {
    return readStatus;
  }
  public void setReadStatus(Optional<ReadStatus> readStatus) {
    this.readStatus = readStatus;
  }
  public Optional<SharedStatus> getSharedStatus() {
    return sharedStatus;
  }
  public void setSharedStatus(Optional<SharedStatus> sharedStatus) {
    this.sharedStatus = sharedStatus;
  }
  public Set<Tag> getTags() {
    return tags;
  }
  public void setTags(Set<Tag> tags) {
    this.tags = tags;
  }
  
  @Override
  public String toString() {
    return "BookmarkQueryFilter [readStatus=" + readStatus + ", sharedStatus=" + sharedStatus + ", tags=" + tags + "]";
  }
  
}
