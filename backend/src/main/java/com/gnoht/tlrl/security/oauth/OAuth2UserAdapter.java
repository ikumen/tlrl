package com.gnoht.tlrl.security.oauth;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gnoht.tlrl.security.Role;
import com.gnoht.tlrl.user.User;

/**
 * Default {@link UserAdapter} implementation.
 * 
 * @author ikumen@gnoht.com
 */
public class OAuth2UserAdapter extends User implements UserAdapter<OAuth2UserAdapter, OAuth2User>, OAuth2User {
  
  private final OAuth2User oauthUser;
  
  public OAuth2UserAdapter(User user, OAuth2User oauthUser) {
    super(user.getId(), user.getName(), user.getEmail(), user.getRoles());
    this.oauthUser = oauthUser;
  }
  
  @JsonIgnore
  @Override
  public Map<String, Object> getAttributes() {
    return oauthUser.getAttributes();
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
  public OAuth2User getExternalUser() {
    return oauthUser;
  }

  /**
   * Create a new {@link OAuth2UserAdapter} with this {@link User} and the given
   * external {@link OAuth2User}.
   * @param extUser
   * @return
   */
  @JsonIgnore
  @Override
  public OAuth2UserAdapter withExternal(OAuth2User extUser) {

    return new OAuth2UserAdapter(this, extUser);
  }

  @JsonIgnore
  @Override
  public OAuth2UserAdapter withUser(User user) {
    return new OAuth2UserAdapter(user, oauthUser);
  }

}
