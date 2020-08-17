/**
 * 
 */
package com.gnoht.tlrl.security.oauth;

import javax.inject.Inject;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import com.gnoht.tlrl.security.Role;
import com.gnoht.tlrl.user.User;
import com.gnoht.tlrl.user.UserService;

/**
 * @author ikumen@gnoht.com
 */
@Component
public class OidcUserAdapterService extends OidcUserService {

  protected final UserService userService;
  
  @Inject
  public OidcUserAdapterService(UserService userService) {
    this.userService = userService;
  }
  
  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    OidcUser oidcUser = super.loadUser(userRequest);
    String email = oidcUser.getEmail();
    return new OidcUserAdapter(userService.findByEmail(email)
      .orElse(User.builder()
          .name(oidcUser.getName())
          .email(email)
          .role(Role.ROLE_UNCONFIRMED)
          .build()), oidcUser);
  }

}
