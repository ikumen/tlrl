package com.gnoht.tlrl.bookmark;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ikumen@gnoht.com
 */
@Entity
public class WebUrl implements Serializable {
  public static final long serialVersionUID = 1l;

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(columnDefinition = "text", unique = true, nullable = false, updatable = false)
  private String url;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdDateTime;

  WebUrl() {/* for JPA */}

  public WebUrl(Long id, String url, LocalDateTime createdDateTime) {
    this.id = id;
    this.url = url;
    this.createdDateTime = createdDateTime;
  }

  public WebUrl(String url) {
    this.url = url;
  }

  public Long getId() {
    return id;
  }

  public String getUrl() {
    return url;
  }

  @JsonIgnore
  public LocalDateTime getCreatedDateTime() {
    return createdDateTime;
  }

  @JsonProperty(value = "createdDateTime")
  public ZonedDateTime getZonedCreatedDateTime() {
    if (createdDateTime != null) {
      return createdDateTime.atZone(ZoneOffset.UTC);
    }
    return null;
  }

  void setId(Long id) {
    this.id = id;
  }

  void setUrl(String url) {
    this.url = url;
  }

  void setCreatedDateTime(LocalDateTime createdDateTime) {
    this.createdDateTime = createdDateTime;
  }

  @PrePersist
  public void beforePersist() {
    this.createdDateTime = LocalDateTime.now(ZoneOffset.UTC);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    WebUrl webUrl = (WebUrl) o;
    return url.equals(webUrl.url);
  }

  @Override
  public int hashCode() {
    return url.hashCode();
  }

  @Override
  public String toString() {
    return "WebUrl {" +
        "id=" + id +
        ", url='" + url + '\'' +
        ", createdDateTime=" + createdDateTime +
        '}';
  }
  
  public static final class Builder {
    private Long id;
    private String url;
    private LocalDateTime createdDateTime;
    private Builder() {}
    public static Builder builder() {
      return new Builder();
    }
    
    public Builder id(Long id) {
      this.id = id; return this;
    }
    public Builder url(String url) {
      this.url = url; return this;
    }
    public Builder createdDateTime(LocalDateTime date) {
      this.createdDateTime = date; return this;
    }
    public WebUrl build() {
      return new WebUrl(id, url, createdDateTime);
    }
  }  
}
