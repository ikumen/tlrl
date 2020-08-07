/**
 * 
 */
package com.gnoht.tlrl.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.gnoht.tlrl.security.oauth.UserAdapter;
import com.gnoht.tlrl.user.User;

/**
 * @author ikumen
 */
@Component
public class SecurityHelper {
  
  /**
   * Reset the {@link Authentication} for the current security context with the 
   * given {@link User}. Usually called after a User's roles has changed.
   * 
   * @param user
   */
  public void resetAuthenticatedPrincipal(User user) {
    OAuth2AuthenticationToken auth = (OAuth2AuthenticationToken) 
        SecurityContextHolder.getContext().getAuthentication();

    OAuth2User oauth2User = (OAuth2User)((UserAdapter)auth.getPrincipal()).withUser(user);
    OAuth2AuthenticationToken newAuth = new OAuth2AuthenticationToken(
        oauth2User, user.getRoles(), auth.getAuthorizedClientRegistrationId());
    SecurityContextHolder.getContext().setAuthentication(newAuth);
  }
  
}
