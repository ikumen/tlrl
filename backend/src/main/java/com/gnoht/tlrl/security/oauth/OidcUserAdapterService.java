package com.gnoht.tlrl.security.oauth;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

/**
 * @author ikumen@gnoht.com
 */
public interface OidcUserAdapterService {
  String getProviderId();
  OidcUserAdapter loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException;
}
