/**
 * 
 */
package com.gnoht.tlrl.bookmark;

import com.gnoht.tlrl.core.Facet;

/**
 * @author ikumen
 */
public class ReadStatusFacet implements Facet<ReadStatus> {

  private ReadStatus value;
  private long count;
  
  public ReadStatusFacet(ReadStatus value) {
    this.value = value;
  }
  
  public ReadStatusFacet(ReadStatus value, long count) {
    this.value = value;
    this.count = count;
  }

  public ReadStatus getValue() {
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
    ReadStatusFacet other = (ReadStatusFacet) obj;
    if (value != other.value)
      return false;
    return true;
  } 
  
}
