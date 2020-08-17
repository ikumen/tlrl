/**
 * 
 */
package com.gnoht.tlrl.bookmark;

import com.gnoht.tlrl.core.Facet;

/**
 * @author ikumen
 */
public class TagFacet implements Facet<Tag> {
  private Tag value;
  private long count;
  
  public TagFacet(Tag value) {
    this.value = value;
  }
  
  public TagFacet(Tag value, long count) {
    this.value = value;
    this.count = count;
  }

  public Tag getValue() {
    return value;
  }

  public long getCount() {
    return count;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((value == null) ? 0 : value.hashCode());
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
    if (value == null) {
      if (other.value != null)
        return false;
    } else if (!value.equals(other.value))
      return false;
    return true;
  }
  
}
