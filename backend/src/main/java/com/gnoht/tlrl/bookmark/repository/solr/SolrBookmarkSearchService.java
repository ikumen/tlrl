package com.gnoht.tlrl.bookmark.repository.solr;

import static com.gnoht.tlrl.bookmark.repository.solr.SolrBookmarkDocument.ARCHIVED_DT_FLD;
import static com.gnoht.tlrl.bookmark.repository.solr.SolrBookmarkDocument.CREATED_DT_FLD;
import static com.gnoht.tlrl.bookmark.repository.solr.SolrBookmarkDocument.DESCRIPTION_FLD;
import static com.gnoht.tlrl.bookmark.repository.solr.SolrBookmarkDocument.ID_FLD;
import static com.gnoht.tlrl.bookmark.repository.solr.SolrBookmarkDocument.OWNER_ID_FLD;
import static com.gnoht.tlrl.bookmark.repository.solr.SolrBookmarkDocument.OWNER_NAME_FLD;
import static com.gnoht.tlrl.bookmark.repository.solr.SolrBookmarkDocument.READ_STATUS_FLD;
import static com.gnoht.tlrl.bookmark.repository.solr.SolrBookmarkDocument.SHARED_STATUS_FLD;
import static com.gnoht.tlrl.bookmark.repository.solr.SolrBookmarkDocument.TAGS_FLD;
import static com.gnoht.tlrl.bookmark.repository.solr.SolrBookmarkDocument.TITLE_FLD;
import static com.gnoht.tlrl.bookmark.repository.solr.SolrBookmarkDocument.URL_FLD;
import static com.gnoht.tlrl.bookmark.repository.solr.SolrBookmarkDocument.WEB_URL_ID_FLD;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.FacetParams;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gnoht.tlrl.bookmark.Bookmark;
import com.gnoht.tlrl.bookmark.BookmarkFacets;
import com.gnoht.tlrl.bookmark.BookmarkFacets.TagFacet;
import com.gnoht.tlrl.bookmark.BookmarkResults;
import com.gnoht.tlrl.bookmark.ReadStatus;
import com.gnoht.tlrl.bookmark.SharedStatus;
import com.gnoht.tlrl.bookmark.Tag;
import com.gnoht.tlrl.bookmark.repository.BookmarkQueryFilter;
import com.gnoht.tlrl.core.TlrlException;
import com.gnoht.tlrl.search.SearchService;

/**
 * @author ikumen@gnoht.com
 */
@Service
public class SolrBookmarkSearchService implements SearchService<BookmarkResults, BookmarkQueryFilter> {
  static final String FQ_FORMAT = "%s:%s";
  private SolrClient solrClient;

  public SolrBookmarkSearchService(SolrClient solrClient) {
    this.solrClient = solrClient;
  }
  
  private String buildFilterQuery(String field, String value) {
    return String.format(FQ_FORMAT, field, value);
  }
  
  protected void applyQueryFilters(SolrQuery solrQuery, BookmarkQueryFilter queryFilter) {
    queryFilter.getOwner().ifPresent(owner -> 
      solrQuery.addFilterQuery(buildFilterQuery(OWNER_ID_FLD, owner.getId().toString())));
    queryFilter.getReadStatus().ifPresent(status ->
      solrQuery.addFilterQuery(buildFilterQuery(READ_STATUS_FLD, status.name())));
    queryFilter.getSharedStatus().ifPresent(status ->
      solrQuery.addFilterQuery(buildFilterQuery(SHARED_STATUS_FLD, status.name())));
    queryFilter.getTags().forEach(tag ->
      solrQuery.addFilterQuery(buildFilterQuery(TAGS_FLD, tag.getId())));
  }

  @Override
  public BookmarkResults search(String terms, BookmarkQueryFilter queryFilter, Pageable pageable) {
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

    applyQueryFilters(solrQuery, queryFilter);
    solrQuery.addFacetField(TAGS_FLD, READ_STATUS_FLD, SHARED_STATUS_FLD);
    solrQuery.set(CommonParams.START, pageable.getPageNumber() * pageable.getPageSize());
    solrQuery.set(CommonParams.ROWS, pageable.getPageSize());
    solrQuery.setFacetMinCount(1); // show only related tags for given filters
    solrQuery.set(FacetParams.FACET_EXCLUDETERMS, queryFilter.getTags().stream()
      .map(Tag::getId)
      .collect(Collectors.joining(",")));
      
    BookmarkFacets facets = new BookmarkFacets();
    final List<Bookmark> bookmarks = new ArrayList<>();
    try {
      QueryResponse resp = solrClient.query(solrQuery);
      List<FacetField> fields = resp.getFacetFields();
      if (fields != null) {
        fields.forEach(field -> {
          String fieldName = field.getName();
          if (fieldName.equals(TAGS_FLD)) {
            field.getValues().forEach(t -> facets.getTags()
                .add(new TagFacet(new Tag(t.getName()), t.getCount())));            
          } else if (fieldName.equals(READ_STATUS_FLD)) {
            field.getValues().forEach(s -> facets.getReadStatuses()
                .put(ReadStatus.valueOf(s.getName()), s.getCount()));
          } else if (fieldName.equals(SHARED_STATUS_FLD)) {
            field.getValues().forEach(s -> facets.getSharedStatuses()
                .put(SharedStatus.valueOf(s.getName()), s.getCount()));
          }
        });
      }
      
      for (SolrBookmarkDocument doc : resp.getBeans(SolrBookmarkDocument.class)) {
        bookmarks.add(doc.toBookmark());
      }
      
      return new BookmarkResults(new PageImpl<>(
        bookmarks, pageable, resp.getResults().getNumFound()), facets);
      
    } catch (IOException|SolrServerException e) {
      throw new TlrlException(e);
    }
  }
  
}