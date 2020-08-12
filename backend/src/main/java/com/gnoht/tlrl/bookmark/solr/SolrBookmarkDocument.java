package com.gnoht.tlrl.bookmark.solr;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import com.gnoht.tlrl.bookmark.Bookmark;
import com.gnoht.tlrl.bookmark.ReadStatus;
import com.gnoht.tlrl.bookmark.SharedStatus;
import com.gnoht.tlrl.bookmark.Tag;
import com.gnoht.tlrl.bookmark.WebUrl;
import com.gnoht.tlrl.user.User;

import org.apache.solr.client.solrj.beans.Field;

/**
 * 
 * @author ikumen@gnoht.com
 */
public class SolrBookmarkDocument {
  
  public static final String CREATED_DT_FLD = "createdDateTime";
  public static final String ID_FLD = "id";
  public static final String UPDATED_DT_FLD = "updatedDateTime";

  public static final String DESCRIPTION_FLD = "description";
  public static final String READ_STATUS_FLD = "readStatus";
  public static final String SHARED_STATUS_FLD = "sharedStatus";
  public static final String TAGS_FLD = "tags";
  public static final String TITLE_FLD = "title";
  
  public static final String URL_FLD = "url";
  public static final String WEB_URL_ID_FLD = "urlId";
  
  public static final String OWNER_ID_FLD = "ownerId";
  public static final String OWNER_NAME_FLD = "ownerName";
  

  private final Bookmark.Builder builder;
  private final WebUrl.Builder webUrlBuilder;
  private final User.Builder userBuilder;

  public SolrBookmarkDocument() {
    this.builder = Bookmark.builder();
    this.webUrlBuilder = WebUrl.Builder.builder();
    this.userBuilder = User.builder();
  }
  
  @Field(ID_FLD)
  public void setId(String id) {
    builder.id(Long.parseLong(id));
  }
    
  @Field(WEB_URL_ID_FLD)
  public void setUrlId(long urlId) {
    this.webUrlBuilder.id(urlId);
  }

  @Field(URL_FLD)
  public void setUrl(String url) {
    this.webUrlBuilder.url(url);
  }
  
  @Field(TITLE_FLD)
  public void setTitle(Collection<String> titles) {
    if (!titles.isEmpty())
      builder.title(titles.iterator().next());
  }
  
  @Field(DESCRIPTION_FLD)
  public void setDescription(Collection<String> descriptions) {
    if (!descriptions.isEmpty())
      builder.description(descriptions.iterator().next());
  }

  @Field(OWNER_ID_FLD)
  public void setOwnerId(long ownerId) {
    this.userBuilder.id(ownerId);
  }

  @Field(OWNER_NAME_FLD)
  public void setOwnerName(String ownerName) {
    this.userBuilder.name(ownerName);
  }
  
  @Field(READ_STATUS_FLD)
  public void setReadStatus(String readStatus) {
    builder.readStatus(ReadStatus.valueOf(readStatus));
  }
  
  @Field(SHARED_STATUS_FLD)
  public void setSharedStatus(String sharedStatus) {
    builder.sharedStatus(SharedStatus.valueOf(sharedStatus));
  }
  
  @Field(TAGS_FLD)
  public void setTags(Collection<String> tags) {
    if (!tags.isEmpty()) {
      builder.tags(tags.stream()
        .map(id -> new Tag(id))
        .collect(Collectors.toList()));
    }
  }

  @Field(CREATED_DT_FLD)
  public void setCreatedDateTime(Date date) {
    builder.createdDateTime(LocalDateTime
      .ofInstant(date.toInstant(), ZoneOffset.UTC));
  }
  
  public Bookmark toBookmark() {
    return builder
        .webUrl(webUrlBuilder.build())
        .owner(userBuilder.build())
      .build();
  }
}