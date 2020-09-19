package com.gnoht.tlrl.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gnoht.tlrl.security.Role;
import com.gnoht.tlrl.security.jpa.JpaRoleConverter;

/**
 * @author ikumen@gnoht.com
 */
@Entity
@Table(name = "app_user")
@NamedEntityGraph(name = "User.roles",
    attributeNodes = @NamedAttributeNode("roles"))
public class User implements Serializable {
  public static final long serialVersionUID = 1l;

  static final String USER_ID_COL = "user_id";
  static final String ROLE_ID_COL = "role_id";

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotEmpty @NotNull
  @Size(min = 3, max = 255)
  @Pattern(regexp = "^[a-zA-Z0-9_]*$")
  @Column(unique = true)
  private String name;

  @JsonIgnore
  @Column(nullable = false)
  private String email;

  @JsonIgnore
  @Column(nullable = false, unique = true)
  private String oauthUserId;

  @JsonIgnore // we don't need roles exposed to client
  @ElementCollection(targetClass = Role.class)
  @Convert(converter = JpaRoleConverter.class)
  @CollectionTable(name = "user_role",
      joinColumns = {@JoinColumn(name = USER_ID_COL)},
      uniqueConstraints = {@UniqueConstraint(columnNames = {USER_ID_COL, ROLE_ID_COL})},
      indexes = {@Index(columnList = ROLE_ID_COL)})
  @Column(name = ROLE_ID_COL, length = Role.CODE_LENGTH)
  private Set<Role> roles = new HashSet<>();

  User() {/* for JPA */}

  public User(Long id, String oauthUserId, String name, String email) {
    this.id = id;
    this.oauthUserId = oauthUserId;
    this.name = name;
    this.email = email;
  }

  @JsonCreator
  public User(
      @JsonProperty("id") Long id,
      @JsonProperty("oauthUserId") String oauthUserId,
      @NotEmpty @NotNull @Size(min = 3, max = 255)
      @Pattern(regexp = "^[a-zA-Z0-9_]*$")
      @JsonProperty("name") String name,
      @JsonProperty("email") String email,
      @JsonProperty("roles") Set<Role> roles) {
    this.id = id;
    this.oauthUserId = oauthUserId;
    this.name = name;
    this.email = email;
    this.roles = roles;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public String getOauthUserId() {
    return oauthUserId;
  }

  void setId(Long id) {
    this.id = id;
  }

  void setName(String name) {
    this.name = name;
  }

  void setEmail(String email) {
    this.email = email;
  }

  void setOauthUserId(String oauthUserId) {
    this.oauthUserId = oauthUserId;
  }

  void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || !(o instanceof User)) return false;
    User user = (User) o;
    return (id != null && id.equals(user.getId()))
        || (oauthUserId != null && oauthUserId.equals(user.oauthUserId));
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((oauthUserId == null) ? 0 : oauthUserId.hashCode());
    return result;
  }
  
  @Override
  public String toString() {
    return "User {" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", oauthUserId='" + oauthUserId + '\'' +
        ", roles=" + roles +
        ", email='" + email + '\'' +
      '}';
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private Long id;
    private String name;
    private String email;
    private String oauthUserId;
    private Set<Role> roles = new HashSet<>();
    private Builder() {}

    public Builder id(Long id) {
      this.id = id; return this;
    }
    public Builder name(String name) {
      this.name = name; return this;
    }
    public Builder email(String email) {
      this.email = email; return this;
    }
    public Builder oauthUserId(String oauthUserId) {
      this.oauthUserId = oauthUserId; return this;
    }
    public Builder role(Role role) {
      this.roles.add(role); return this;
    }
    public Builder roles(Set<Role> roles) {
      this.roles = roles; return this;
    }
    public User build() {
      return new User(id, oauthUserId, name, email, roles);
    }
  }
}
