/**
 * 
 */
package com.gnoht.tlrl.security;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import com.gnoht.tlrl.user.User;

/**
 * Handle {@link User}s that belong to {@link Role#ROLE_UNCONFIRMED}. Users that 
 * have successfully authenticated with an OAuth provider, but do not exists yet 
 * in our system (e.g, Role.UNCONFIRMED), we push their credentials onto HTTP 
 * Response headers (this class), then forward them to sign up page (later in 
 * security chain).
 *  
 * @author ikumen
 */
public class UnconfirmedUserAccessDeniedHandler extends AccessDeniedHandlerImpl {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException 
  {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getAuthorities().contains(Role.ROLE_UNCONFIRMED)) {
      User oauthUser = (User) ((OAuth2AuthenticationToken) authentication).getPrincipal();
      response.setHeader("user-name", oauthUser.getName());
      response.setHeader("user-email", oauthUser.getEmail());
      response.setHeader("user-roles", oauthUser.getRoles().stream()
          .map(r -> r.name())
          .collect(Collectors.joining(",")));    
    }
    
    super.handle(request, response, accessDeniedException);
  }
}
