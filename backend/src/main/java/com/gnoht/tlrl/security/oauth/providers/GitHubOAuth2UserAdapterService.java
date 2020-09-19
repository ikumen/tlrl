package com.gnoht.tlrl.security.oauth.providers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gnoht.tlrl.security.Role;
import com.gnoht.tlrl.security.oauth.OAuth2UserAdapter;
import com.gnoht.tlrl.security.oauth.OAuth2UserAdapterService;
import com.gnoht.tlrl.user.User;
import com.gnoht.tlrl.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.net.URI;
import java.util.List;

/**
 * GitHub specific {@link OAuth2UserAdapterService}, responsible for loading a user from
 * OAuth provider GitHub, then adapting the OAuth2User to our internal {@link User}.
 *
 * @author ikumen@gnoht.com
 */
@Component
public class GitHubOAuth2UserAdapterService extends DefaultOAuth2UserService implements OAuth2UserAdapterService {

  public static final String PROVIDER_ID = "github";
  private static final String userEmailUriKey = "${spring.security.oauth2.client.registration."+ PROVIDER_ID +".user-email-uri}";
  private UserService userService;
  private RestTemplate restTemplate;
  private URI emailEndpoint;

  @Inject
  public GitHubOAuth2UserAdapterService(
      UserService userService,
      RestTemplate restTemplate,
      @Value(userEmailUriKey) String emailEndpoint) {
    this.userService = userService;
    this.restTemplate = restTemplate;
    this.emailEndpoint = UriComponentsBuilder.fromHttpUrl(emailEndpoint).build().toUri();
  }

  @Override
  public String getProviderId() {
    return PROVIDER_ID;
  }

  /**
   * Returns an {@link OAuth2User} after obtaining the user attributes of the End-User from the UserInfo Endpoint.
   *
   * @param userRequest the user request
   * @return an {@link OAuth2User}
   * @throws OAuth2AuthenticationException if an error occurs while attempting to obtain the user attributes from the UserInfo Endpoint
   */
  @Override
  public OAuth2UserAdapter loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oauthUser = super.loadUser(userRequest);
    String oauthUserId = PROVIDER_ID + ":" + oauthUser.getAttribute("id");
    String email = oauthUser.getAttribute("email");
    if (email == null) {
      email = loadEmail(userRequest);
    }

    User user = userService.findByOauthUserId(oauthUserId)
        .orElse(User.builder()
            .oauthUserId(oauthUserId)
            .name(oauthUser.getName())
            .email(email)
            .role(Role.ROLE_UNCONFIRMED)
            .build());

    return new OAuth2UserAdapter(user, oauthUser);
  }

  private String loadEmail(OAuth2UserRequest userRequest) {
    RequestEntity requestEntity = RequestEntity.get(emailEndpoint)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userRequest.getAccessToken().getTokenValue()).build();
    ResponseEntity<List<EmailAttributes>> response = restTemplate.exchange(requestEntity,
        new ParameterizedTypeReference<List<EmailAttributes>>() {});

    for (EmailAttributes emailAttrs: response.getBody()) {
      if (emailAttrs.primary)
        return emailAttrs.email;
    }
    return null;
  }

  static class EmailAttributes {
    String email;
    boolean primary;

    @JsonCreator
    public EmailAttributes(
        @JsonProperty("email") String email,
        @JsonProperty("primary") boolean primary) {
      this.email = email;
      this.primary = primary;
    }
  }
}
