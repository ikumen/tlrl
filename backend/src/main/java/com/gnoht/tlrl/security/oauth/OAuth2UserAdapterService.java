package com.gnoht.tlrl.security.oauth;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

/**
 * @author ikumen@gnoht.com
 */
public interface OAuth2UserAdapterService {
  String getProviderId();
  OAuth2UserAdapter loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException;
}
