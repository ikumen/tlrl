/**
 * 
 */
package com.gnoht.tlrl.security.oauth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

/**
 * {@link OAuth2UserService} that delegates to provider specific OAuth2UserService.
 *
 * @author ikumen@gnoht.com
 */
@Component
public class DelegatingOAuth2UserService implements OAuth2UserService {
  
  private Map<String, OAuth2UserAdapterService> delegateUserAdapterServices = new HashMap<>();
  
  @Autowired
  public DelegatingOAuth2UserService(List<OAuth2UserAdapterService> userAdapterServices) {
    for (OAuth2UserAdapterService service: userAdapterServices) {
      delegateUserAdapterServices.put(service.getProviderId(), service);
    }
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    return delegateUserAdapterServices
        .get(userRequest.getClientRegistration().getRegistrationId())
      .loadUser(userRequest);
  }

}
