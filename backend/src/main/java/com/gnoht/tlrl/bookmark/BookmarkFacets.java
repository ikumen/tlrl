/**
 * 
 */
package com.gnoht.tlrl.bookmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ikumen
 */
public class BookmarkFacets {
        
  private List<TagFacet> tags = new ArrayList<>();
  private Map<SharedStatus, Long> sharedStatuses = new HashMap<>();;
  private Map<ReadStatus, Long> readStatuses = new HashMap<>();;
  
  public BookmarkFacets() {}
  
  public BookmarkFacets(List<TagFacet> tags, 
      Map<SharedStatus, Long> sharedStatuses, Map<ReadStatus,Long> readStatuses) {
    this.tags = tags;
    this.sharedStatuses = sharedStatuses;
    this.readStatuses = readStatuses;
  }
  
  public List<TagFacet> getTags() {
    return tags;
  }

  public Map<SharedStatus, Long> getSharedStatuses() {
    return sharedStatuses;
  }

  public Map<ReadStatus, Long> getReadStatuses() {
    return readStatuses;
  }

  public void setTags(List<TagFacet> tags) {
    this.tags = tags;
  }

  public void setSharedStatuses(Map<SharedStatus, Long> sharedStatuses) {
    this.sharedStatuses = sharedStatuses;
  }

  public void setReadStatuses(Map<ReadStatus, Long> readStatuses) {
    this.readStatuses = readStatuses;
  }

  @Override
  public String toString() {
    return "BookmarkFacets [tags=" + tags + ", sharedStatuses=" + sharedStatuses + ", readStatuses=" + readStatuses
        + "]";
  }
  
  public static class TagFacet {
    private Tag tag;
    private long count;
    
    public TagFacet(Tag tag) {
      this.tag = tag;
    }
    
    public TagFacet(Tag tag, long count) {
      this.tag = tag;
      this.count = count;
    }

    @JsonProperty(value = "id")
    public String getId() {
      return tag.getId();
    }
    
    @JsonIgnore
    public Tag getTag() {
      return tag;
    }

    public long getCount() {
      return count;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((tag == null) ? 0 : tag.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      TagFacet other = (TagFacet) obj;
      if (tag == null) {
        if (other.tag != null)
          return false;
      } else if (!tag.equals(other.tag))
        return false;
      return true;
    }    
  }
}
