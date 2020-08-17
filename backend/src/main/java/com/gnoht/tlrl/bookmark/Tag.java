package com.gnoht.tlrl.bookmark;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author ikumen@gnoht.com
 */
@Entity
public class Tag implements Serializable {
  public static final long serialVersionUID = 1L;

  @EmbeddedId
  private TagId id;

  @JsonIgnore
  @MapsId("bookmarkId")
  @ManyToOne(fetch = FetchType.LAZY)
  private Bookmark bookmark;

  private int pos;

  Tag() {/* For JPA */}

  public Tag(String id) {
    this.id = new TagId(id);
  }

  public Tag(String id, Bookmark bookmark, int pos) {
    this.id = new TagId(id, bookmark.getId());
    this.pos = pos;
    this.bookmark = bookmark;
  }

  public String getId() {
    return id.id;
  }

  @JsonIgnore
  public Bookmark getBookmark() {
    return bookmark;
  }

  void setPos(int pos) {
    this.pos = pos;
  }

  void setBookmark(Bookmark bookmark) {
    this.bookmark = bookmark;
    if (id != null) {
      id.bookmarkId = bookmark.getId();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Tag tag = (Tag) o;
    return Objects.equals(id, tag.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
  
  public static String toString(Collection<Tag> tags) {
    return tags == null ? "[]" : tags.stream().map(Tag::toString)
      .collect(Collectors.joining(","));
  }

  @Override
  public String toString() {
    return "Tag [id=" + (id == null ? "" : id.id) + 
      ", bookmark=" + (bookmark == null ? "" : bookmark.getId()) + 
      ", pos=" + pos + "]";
  }

  /**
   * The composite id for {@link Tag}.
   */
  @Embeddable
  static class TagId implements Serializable {
    public static final long serialVersionUID = 1L;

    private String id;
    private Long bookmarkId;

    TagId() {/* For JPA */}

    public TagId(String id, Long bookmarkId) {
      this.id = id;
      this.bookmarkId = bookmarkId;
    }

    public TagId(String id) {
      this.id = id;
    }
    @Override
    public int hashCode() {
      return Objects.hash(bookmarkId, id);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      TagId other = (TagId) obj;
      return Objects.equals(bookmarkId, other.bookmarkId) && Objects.equals(id, other.id);
    }
  }
}
