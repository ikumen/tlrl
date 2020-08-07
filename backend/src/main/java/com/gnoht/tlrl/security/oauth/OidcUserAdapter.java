package com.gnoht.tlrl.security.oauth;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gnoht.tlrl.security.Role;
import com.gnoht.tlrl.user.User;

/**
 * {@link OidcUser} specific {@link UserAdapter} implementation.
 * 
 * @author ikumen@gnoht.com
 */
public class OidcUserAdapter extends User implements UserAdapter<OidcUserAdapter, OidcUser>, OidcUser {

  private final OidcUser oidcUser;
  
  public OidcUserAdapter(User user, OidcUser oidcUser) {
    super(user.getId(), user.getName(), user.getEmail(), user.getRoles());
    this.oidcUser = oidcUser;
  }

  @JsonIgnore
  @Override
  public Map<String, Object> getClaims() {
    return oidcUser.getClaims();
  }

  @JsonIgnore
  @Override
  public OidcUserInfo getUserInfo() {
    return oidcUser.getUserInfo();
  }

  @JsonIgnore
  @Override
  public OidcIdToken getIdToken() {
    return oidcUser.getIdToken();
  }

  @JsonIgnore
  @Override
  public Map<String, Object> getAttributes() {
    return oidcUser.getAttributes();
  }

  @JsonIgnore
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return super.getRoles();
  }

  @JsonIgnore
  @Override
  public User getUser() {
    return this;
  }

  @JsonIgnore
  @Override
  public OidcUser getExternalUser() {
    return oidcUser;
  }

  @JsonIgnore
  @Override
  public OidcUserAdapter withExternal(OidcUser extUser) {
    return new OidcUserAdapter(this, extUser);
  }

  @JsonIgnore
  @Override
  public OidcUserAdapter withUser(User user) {
    return new OidcUserAdapter(user, oidcUser);
  }
  
}
