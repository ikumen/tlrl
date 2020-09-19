package com.gnoht.tlrl.security.oauth.providers;

import com.gnoht.tlrl.security.Role;
import com.gnoht.tlrl.security.oauth.OidcUserAdapter;
import com.gnoht.tlrl.security.oauth.OidcUserAdapterService;
import com.gnoht.tlrl.user.User;
import com.gnoht.tlrl.user.UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Google specific {@link com.gnoht.tlrl.security.oauth.OAuth2UserAdapterService}, responsible for loading a user from
 * OAuth provider Google, then adapting the OAuth2User to our internal {@link User}.
 *
 * @author ikumen@gnoht.com
 */
@Component
public class GoogleOidcUserAdapterService extends OidcUserService implements OidcUserAdapterService {

  public static final String PROVIDER_ID = "google";
  private UserService userService;

  @Inject
  public GoogleOidcUserAdapterService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public String getProviderId() {
    return PROVIDER_ID;
  }

  @Override
  public OidcUserAdapter loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    OidcUser oidcUser = super.loadUser(userRequest);
    String oauthUserId = PROVIDER_ID + ":" + oidcUser.getSubject();

    return new OidcUserAdapter(userService.findByOauthUserId(oauthUserId)
        .orElse(User.builder()
            .name(oidcUser.getName())
            .oauthUserId(oauthUserId)
            .email(oidcUser.getEmail())
            .role(Role.ROLE_UNCONFIRMED)
            .build()), oidcUser);
  }
}
