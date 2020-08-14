/**
 * 
 */
package com.gnoht.tlrl.bookmark;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.gnoht.tlrl.support.Criteria;
import com.gnoht.tlrl.user.User;

/**
 * @author ikumen
 */
public class BookmarkCriteria implements Criteria {
  
  private User owner;
  private ReadStatus readStatus;
  private SharedStatus sharedStatus;
  private Set<Tag> tags = new HashSet<>();
  
  public Optional<User> getOwner() {
    return Optional.ofNullable(owner);
  }
  public Optional<ReadStatus> getReadStatus() {
    return Optional.ofNullable(readStatus);
  }
  public Optional<SharedStatus> getSharedStatus() {
    return Optional.ofNullable(sharedStatus);
  }
  public Set<Tag> getTags() {
    return tags;
  }
  
  public void setOwner(User owner) {
    this.owner = owner;
  }
  public void setReadStatus(ReadStatus readStatus) {
    this.readStatus = readStatus;
  }
  public void setSharedStatus(SharedStatus sharedStatus) {
    this.sharedStatus = sharedStatus;
  }
  public void setTags(Set<Tag> tags) {
    this.tags = tags;
  }
  @Override
  public String toString() {
    return "BookmarkCriteria [owner=" + owner + ", readStatus=" + readStatus + ", sharedStatus=" + sharedStatus
        + ", tags=[" + Tag.toString(tags) + "]]";
  }
}
