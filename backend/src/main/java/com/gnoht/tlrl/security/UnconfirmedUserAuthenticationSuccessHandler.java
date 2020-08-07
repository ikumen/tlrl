/**
 * 
 */
package com.gnoht.tlrl.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * @author ikumen
 */
public class UnconfirmedUserAuthenticationSuccessHandler 
    extends SavedRequestAwareAuthenticationSuccessHandler {
  
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    if (authentication.getAuthorities().contains(Role.ROLE_UNCONFIRMED)) {
      clearAuthenticationAttributes(request);
      response.sendRedirect("/signup");  
    } else {
      this.setAlwaysUseDefaultTargetUrl(true);
      this.setDefaultTargetUrl("/");
      super.onAuthenticationSuccess(request, response, authentication);  
    }
  }
}
