package com.gnoht.tlrl.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gnoht.tlrl.security.Role;
import com.gnoht.tlrl.security.jpa.JpaRoleConverter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ikumen@gnoht.com
 */
@Entity
@Table
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

  @Column(unique = true)
  private String email;

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

  @JsonCreator
  public User(
      @JsonProperty("id") Long id,
      @NotEmpty @NotNull @Size(min = 3, max = 255)
      @Pattern(regexp = "^[a-zA-Z0-9_]*$")
      @JsonProperty("name") String name,
      @JsonProperty("email") String email,
      @JsonProperty("roles") Set<Role> roles) {
    this.id = id;
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

  void setId(Long id) {
    this.id = id;
  }

  void setName(String name) {
    this.name = name;
  }

  void setEmail(String email) {
    this.email = email;
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
        || (email != null && email.equals(user.email));
  }

  @Override
  public int hashCode() {
    return email.hashCode();
  }

  @Override
  public String toString() {
    return "User {" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", roles=" + roles + ", email=" + email +
        '}';
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private Long id;
    private String name;
    private String email;
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
    public Builder role(Role role) {
      this.roles.add(role); return this;
    }
    public Builder roles(Set<Role> roles) {
      this.roles = roles; return this;
    }
    public User build() {
      return new User(id, name, email, roles);
    }
  }
}
