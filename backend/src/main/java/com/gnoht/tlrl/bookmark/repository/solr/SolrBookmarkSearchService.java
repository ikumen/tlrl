package com.gnoht.tlrl.bookmark.repository.solr;

import static com.gnoht.tlrl.bookmark.repository.solr.SolrBookmarkDocument.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gnoht.tlrl.bookmark.Bookmark;
import com.gnoht.tlrl.search.SearchService;
import com.gnoht.tlrl.user.User;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author ikumen@gnoht.com
 */
@Service
public class SolrBookmarkSearchService implements SearchService<Bookmark> {
  private SolrClient solrClient;

  public SolrBookmarkSearchService(SolrClient solrClient) {
    this.solrClient = solrClient;
  }
  
  protected String[] buildQueryFilters(Map<String, Object> filters) {
    return filters.entrySet().stream().map(e -> 
        String.format("%s:%s", e.getKey(), e.getValue()))
      .toArray(String[]::new);
  }

  public Page<Bookmark> search(String terms, Map<String, Object> filters, User user, Pageable pageable) {
    filters.put(SolrBookmarkDocument.OWNER_ID_FLD, user.getId());
    return search(terms, filters, pageable);
  }
  
  public Page<Bookmark> search(String terms, Map<String, Object> filters, Pageable pageable) {
    final List<Bookmark> bookmarks = new ArrayList<>();
    final SolrQuery solrQuery = terms == null || "".equals(terms)
        ? new SolrQuery("*:*") 
        : new SolrQuery(terms);
    
    solrQuery.addField(ID_FLD)
      .addField(WEB_URL_ID_FLD)
      .addField(URL_FLD)
      .addField(OWNER_ID_FLD)
      .addField(OWNER_NAME_FLD)
      .addField(TAGS_FLD)
      .addField(CREATED_DT_FLD)
      .addField(ARCHIVED_DT_FLD)
      .addField(DESCRIPTION_FLD)
      .addField(READ_STATUS_FLD)
      .addField(SHARED_STATUS_FLD)
      .addField(TITLE_FLD);

    if (!filters.isEmpty()) {
      solrQuery.addFilterQuery(buildQueryFilters(filters));
    }
      
    try {
      QueryResponse resp = solrClient.query(solrQuery);
      for (SolrBookmarkDocument doc : resp.getBeans(SolrBookmarkDocument.class)) {
        bookmarks.add(doc.toBookmark());
      }
    } catch (IOException|SolrServerException e) {
      e.printStackTrace();
    }
    return new PageImpl<>(bookmarks);
  }
  
}