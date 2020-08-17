/**
 * 
 */
package com.gnoht.tlrl.bookmark;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ikumen
 */
public class BookmarkFacets {
  private List<TagFacet> tags;
  private List<SharedStatusFacet> sharedStatuses;
  private List<ReadStatusFacet> readStatuses;
  
  public BookmarkFacets() {
    this.tags = new ArrayList<>();
    this.sharedStatuses = new ArrayList<>();
    this.readStatuses = new ArrayList<>();    
  };
  
  public BookmarkFacets(List<TagFacet> tags, List<SharedStatusFacet> sharedStatuses, List<ReadStatusFacet> readStatuses) {
    this.tags = tags;
    this.sharedStatuses = sharedStatuses;
    this.readStatuses = readStatuses;
  }

  public List<TagFacet> getTags() {
    return tags;
  }

  public List<SharedStatusFacet> getSharedStatuses() {
    return sharedStatuses;
  }

  public List<ReadStatusFacet> getReadStatuses() {
    return readStatuses;
  }

  @Override
  public String toString() {
    return "BookmarkFacets [tags=" + tags + ", sharedStatuses=" + sharedStatuses + ", readStatuses=" + readStatuses
        + "]";
  }

  
}
