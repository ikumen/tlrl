/**
 * 
 */
package com.gnoht.tlrl.security.oauth;

import javax.inject.Inject;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.gnoht.tlrl.security.Role;
import com.gnoht.tlrl.user.User;
import com.gnoht.tlrl.user.UserService;

/**
 * 
 * @author ikumen@gnoht.com
 */
@Component
public class OAuth2UserAdapterService extends DefaultOAuth2UserService {
  
  protected final UserService userService;
  
  @Inject
  public OAuth2UserAdapterService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oauthUser = super.loadUser(userRequest);
    String email = oauthUser.getAttribute("email");
    User user = userService.findByEmail(email)
      .orElse(User.builder()
          .name(oauthUser.getName())
          .email(email)
          .role(Role.ROLE_UNCONFIRMED)
          .build());
    
    return new OAuth2UserAdapter(user, oauthUser);
  }
}
