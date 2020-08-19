/**
 * 
 */
package com.gnoht.tlrl.bookmark;

import java.util.List;

import org.springframework.data.domain.Page;

/**
 * @author ikumen
 */
public class BookmarkResults {
  
  private Page<Bookmark> results;
  private BookmarkFacets facets;
  
  public BookmarkResults(Page<Bookmark> results) {
    this.results = results;
  }
  
  public BookmarkResults(Page<Bookmark> results, BookmarkFacets facets) {
    this.results = results;
    this.facets = facets;
  }

  public List<Bookmark> getBookmarks() {
    return results.getContent();
  }
  
  public BookmarkFacets getFacets() {
    return facets;
  }
  
  public int getSize() {
    return results.getSize();
  }
  
  public int getPage() {
    return results.getNumber();
  }
  
  public long getTotal() {
    return results.getTotalElements();
  }
  
  public boolean isFirst() {
    return results.isFirst();
  }
  
  public boolean isLast() {
    return results.isLast();
  }
}
