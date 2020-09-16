package com.gnoht.tlrl.bookmark;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gnoht.tlrl.core.Constants;
import com.gnoht.tlrl.user.User;

/**
 * @author ikumen@gnoht.com
 */
@Entity
@Table(uniqueConstraints = {
  @UniqueConstraint(columnNames = {
    Bookmark.OWNER_ID_COL, Bookmark.WEB_URL_ID_COL
  })
})
public class Bookmark implements Serializable {

  public static final long serialVersionUID = 1l;

  public static final int MAX_TAGS_PER_BOOKMARK = 5;

  static final String OWNER_ID_COL = "owner_id";
  static final String WEB_URL_ID_COL = "url_id";

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class,
      cascade = {CascadeType.REFRESH}, optional = false)
  @JoinColumn(name = OWNER_ID_COL)
  private User owner;

  @ManyToOne(fetch = FetchType.EAGER, targetEntity = WebUrl.class,
      cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE})
  @JoinColumn(name = WEB_URL_ID_COL)
  private WebUrl webUrl;

  private String title;

  @Column(columnDefinition = "text")
  private String description;

  @Enumerated(EnumType.ORDINAL)
  private ReadStatus readStatus;

  @Enumerated(EnumType.ORDINAL)
  private SharedStatus sharedStatus;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdDateTime;

  @Column(nullable = false)
  private LocalDateTime updatedDateTime;

  @Column()
  private LocalDateTime archivedDateTime;

  @OneToMany(mappedBy = "bookmark", cascade = {CascadeType.ALL},
      orphanRemoval = true, fetch = FetchType.LAZY)
  @OrderColumn(name = "pos")
  @Size(max = MAX_TAGS_PER_BOOKMARK)
  private List<Tag> tags;

  @Transient
  private String content;

  @Version
  @Column(columnDefinition = "integer DEFAULT 0", nullable = false)
  private Long version = 0l;

  Bookmark() {/* for JPA */}

  public Bookmark(Long id, User owner) {
    this.id = id;
    this.owner = owner;
  }

  @JsonCreator
  public Bookmark(
      @JsonProperty(value = "id") Long id,
      @JsonProperty(value = "owner") User owner,
      @JsonProperty(value = "webUrl") WebUrl webUrl,
      @JsonProperty(value = "title") String title,
      @JsonProperty(value = "description") String description,
      @JsonProperty(value = "readStatus") ReadStatus readStatus,
      @JsonProperty(value = "sharedStatus") SharedStatus sharedStatus,
      @JsonProperty(value = "createdDateTime") LocalDateTime createdDateTime,
      @JsonProperty(value = "updatedDateTime") LocalDateTime updatedDateTime,
      @JsonProperty(value = "archivedDateTime") LocalDateTime archivedDateTime,
      @JsonProperty(value = "tags") @Size(max = MAX_TAGS_PER_BOOKMARK) List<Tag> tags,
      @JsonProperty(value = "version") Long version)
  {
    this.id = id;
    this.owner = owner;
    this.webUrl = webUrl;
    this.title = title;
    this.description = description;
    this.readStatus = readStatus;
    this.sharedStatus = sharedStatus;
    this.createdDateTime = createdDateTime;
    this.updatedDateTime = updatedDateTime;
    this.archivedDateTime = archivedDateTime;
    setTags(tags);
    this.version = version == null ? 0l : version;
  }
    
  public Bookmark(Long id, User owner, WebUrl webUrl, String title, String description, 
      ReadStatus readStatus, SharedStatus sharedStatus, LocalDateTime createdDateTime, 
      LocalDateTime updatedDateTime, LocalDateTime archivedDateTime, Long version, Tag ...tags) 
  {
    this(id, owner, webUrl, title, description, readStatus, sharedStatus, createdDateTime, 
        updatedDateTime, archivedDateTime, 
        Arrays.stream(tags).filter(Objects::nonNull).collect(Collectors.toList()), 
        version);
  }

  public Long getId() {
    return id;
  }

  public User getOwner() {
    return owner;
  }

  public WebUrl getWebUrl() {
    return webUrl;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public ReadStatus getReadStatus() {
    return readStatus;
  }

  public SharedStatus getSharedStatus() {
    return sharedStatus;
  }

  public List<Tag> getTags() {
    return tags;
  }

  @JsonIgnore
  public LocalDateTime getCreatedDateTime() {
    return createdDateTime;
  }

  @JsonIgnore
  public LocalDateTime getUpdatedDateTime() {
    return updatedDateTime;
  }

  @JsonIgnore
  public LocalDateTime getArchivedDateTime() {
    return archivedDateTime;
  }

  @JsonProperty(value = "createdDateTime")
  @JsonFormat(shape = JsonFormat.Shape.STRING,
      pattern = Constants.JSON_DATE_FORMAT, timezone = Constants.JSON_DATE_TIMEZONE)
  public ZonedDateTime getZonedCreatedDateTime() {
    if (createdDateTime == null) return null;
    return createdDateTime.atZone(ZoneOffset.UTC);
  }

  @JsonProperty(value = "updatedDateTime")
  @JsonFormat(shape = JsonFormat.Shape.STRING,
      pattern = Constants.JSON_DATE_FORMAT, timezone = Constants.JSON_DATE_TIMEZONE)
  public ZonedDateTime getZonedUpdatedDateTime() {
    if (updatedDateTime == null) return null;
    return updatedDateTime.atZone(ZoneOffset.UTC);
  }

  @JsonProperty(value = "archivedDateTime")
  @JsonFormat(shape = JsonFormat.Shape.STRING,
      pattern = Constants.JSON_DATE_FORMAT, timezone = Constants.JSON_DATE_TIMEZONE)
  public ZonedDateTime getZonedArchivedDateTime() {
    if (archivedDateTime == null) return null;
    return archivedDateTime.atZone(ZoneOffset.UTC);
  }

  public String getContent() {
    return content;
  }

  public Long getVersion() {
    return version;
  }

  @PrePersist
  void prePersist() {
    createdDateTime = updatedDateTime = LocalDateTime.now(ZoneOffset.UTC);
    if (sharedStatus == null) {
      sharedStatus = SharedStatus.PRIVATE;
    }
    if (readStatus == null) {
      readStatus = ReadStatus.NA;
    }
  }

  void setId(Long id) {
    this.id = id;
  }

  void setWebUrl(WebUrl webUrl) {
    this.webUrl = webUrl;
  }

  void setOwner(User owner) {
    this.owner = owner;
  }

  void setTitle(String title) {
    this.title = title;
  }

  void setDescription(String description) {
    this.description = description;
  }

  void setReadStatus(ReadStatus readStatus) {
    this.readStatus = readStatus;
  }

  void setSharedStatus(SharedStatus sharedStatus) {
    this.sharedStatus = sharedStatus;
  }

  void setUpdatedDateTime(LocalDateTime updatedDateTime) {
    this.updatedDateTime = updatedDateTime;
  }

  void setArchivedDateTime(LocalDateTime archivedDateTime) {
    this.archivedDateTime = archivedDateTime;
  }

  void setTags(List<Tag> newTags) {
    if (newTags == null)
      return;

    for (int i=0; i < newTags.size(); i++) {
      // Assign this Bookmark as parent to the new Tags
      newTags.get(i).setBookmark(this);
      // Assign the order/position
      newTags.get(i).setPos(i);
    }

    // If Bookmark doesn't have any bookmarks, just assign new Tags
    if (this.tags == null) {
      this.tags = newTags;
    } else {
      // Otherwise, we to figure out which existing Tag was kept and
      // try to update it's position.
      for (int i=0; i < this.tags.size(); i++) {
        Tag existingTag = this.tags.get(i);
        int newIndex = newTags.indexOf(existingTag);
        if (newIndex != -1) {
          existingTag.setPos(newIndex);
          newTags.set(newIndex, existingTag);
        }
      }
      // Note, we can't simply overwrite existing Tags, as they are managed by
      // JPA provider, so we want to clear then re-add the existing and new Tags.
      this.tags.clear();
      this.tags.addAll(newTags);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Bookmark bookmark = (Bookmark) o;
    return Objects.equals(owner, bookmark.owner) &&
        Objects.equals(webUrl, bookmark.webUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(owner, webUrl);
  }

  @Override
  public String toString() {
    return "Bookmark {" +
        "id=" + id +
        ", owner=" + owner +
        ", webUrl=" + webUrl +
        ", title='" + title + '\'' +
        ", description='" + description + '\'' +
        ", readStatus=" + readStatus +
        ", sharedStatus=" + sharedStatus +
        ", createdDateTime=" + createdDateTime +
        ", updatedDateTime=" + updatedDateTime +
        ", archivedDateTime=" + archivedDateTime +
        ", tags=[" + Tag.toString(tags) + "]" +
        ", content='" + content + "'}";
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private Long id;
    private User owner;
    private WebUrl webUrl;
    private String title;
    private String description;
    private ReadStatus readStatus;
    private SharedStatus sharedStatus;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
    private LocalDateTime archivedDateTime;
    private List<Tag> tags;
    private String content;
    private Long version;

    private Builder() {}

    public Builder id(Long id) {
      this.id = id; return this;
    }
    public Builder owner(User owner) {
      this.owner = owner; return this;
    }
    public Builder webUrl(WebUrl webUrl) {
      this.webUrl = webUrl; return this;
    }
    public Builder title(String title) {
      this.title = title; return this;
    }
    public Builder description(String description) {
      this.description = description; return this;
    }
    public Builder readStatus(ReadStatus readStatus) {
      this.readStatus = readStatus; return this;
    }
    public Builder sharedStatus(SharedStatus sharedStatus) {
      this.sharedStatus = sharedStatus; return this;
    }
    public Builder createdDateTime(LocalDateTime createdDateTime) {
      this.createdDateTime = createdDateTime; return this;
    }
    public Builder updatedDateTime(LocalDateTime updatedDateTime) {
      this.updatedDateTime = updatedDateTime; return this;
    }
    public Builder archivedDateTime(LocalDateTime archivedDateTime) {
      this.archivedDateTime = archivedDateTime; return this;
    }
    public Builder tags(List<Tag> tags) {
      this.tags = tags; return this;
    }
    public Builder tag(Tag tag) {
      if (tags == null)
        tags = new ArrayList<>();
      tags.add(tag);
      return this;
    }
    public Builder content(String content) {
      this.content = content; return this;
    }
    public Builder version(Long version) {
      this.version = version; return this;
    }

    public static Builder with(Bookmark bookmark) {
      return new Builder()
          .id(bookmark.getId())
          .owner(bookmark.getOwner())
          .webUrl(bookmark.getWebUrl())
          .title(bookmark.getTitle())
          .description(bookmark.getDescription())
          .readStatus(bookmark.getReadStatus())
          .sharedStatus(bookmark.getSharedStatus())
          .createdDateTime(bookmark.getCreatedDateTime())
          .updatedDateTime(bookmark.getUpdatedDateTime())
          .archivedDateTime(bookmark.getArchivedDateTime())
          .tags(bookmark.getTags())
          .version(bookmark.getVersion());
    }

    public Bookmark build() {
      return new Bookmark(id,owner,webUrl,title,description,readStatus,sharedStatus,createdDateTime,updatedDateTime,archivedDateTime,tags,version);
    }
  }
}
