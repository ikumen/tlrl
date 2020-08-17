/**
 * 
 */
package com.gnoht.tlrl.bookmark;

import com.gnoht.tlrl.core.Facet;

/**
 * @author ikumen
 */
public class SharedStatusFacet implements Facet<SharedStatus> {
  private SharedStatus value;
  private long count;
  
  public SharedStatusFacet(SharedStatus value) {
    this.value = value;
  }
  
  public SharedStatusFacet(SharedStatus value, long count) {
    this.value = value;
    this.count = count;
  }

  public SharedStatus getValue() {
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
    SharedStatusFacet other = (SharedStatusFacet) obj;
    if (value != other.value)
      return false;
    return true;
  }
  
}
