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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link OidcUserService} that delegates to provider specific {@link OidcUserService}.
 *
 * @author ikumen@gnoht.com
 */
@Component
public class DelegatingOidcUserService extends OidcUserService {

  private Map<String, OidcUserAdapterService> delegateUserAdapterServices = new HashMap<>();
  
  @Inject
  public DelegatingOidcUserService(List<OidcUserAdapterService> userAdapterServices) {
    for (OidcUserAdapterService service : userAdapterServices) {
      delegateUserAdapterServices.put(service.getProviderId(), service);
    }
  }
  
  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    return delegateUserAdapterServices
        .get(userRequest.getClientRegistration().getRegistrationId())
      .loadUser(userRequest);
  }

}
