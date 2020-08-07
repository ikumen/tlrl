package com.gnoht.tlrl.security;

import java.lang.invoke.MethodHandles;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gnoht.tlrl.core.AlreadyExistsException;
import com.gnoht.tlrl.security.oauth.UserAdapter;
import com.gnoht.tlrl.user.User;
import com.gnoht.tlrl.user.UserService;

@Controller
public class SecurityController {
  
  protected final static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  
  private final UserService userService;
  private final SecurityHelper securityHelper;

  public SecurityController(UserService userService, SecurityHelper securityHelper) {
    this.userService = userService;
    this.securityHelper = securityHelper;
  }
  
  /**
   * Serve the sign up page to newly authenticated {@link User}. Routing through
   * this controller gives us the chance to attach the user's details to the 
   * Response headers for the sign up page.  
   * 
   * @param authUser
   * @param response
   * @return
   */
  @GetMapping("/signup")
  public String index(@AuthenticationPrincipal User authUser, HttpServletResponse response) {
    LOG.info("Serve the sign up page for: {} ", authUser);
    if (authUser != null && authUser.getRoles().contains(Role.ROLE_UNCONFIRMED)) {
      User user = (User) authUser;
      response.setHeader("user-name", user.getName());
      response.setHeader("user-email", user.getEmail());
      response.setHeader("user-roles", user.getRoles().stream()
        .map(r -> r.name())
        .collect(Collectors.joining(",")));
    } else {
      LOG.warn("Only authenticated, unconfirmed users allowed: {}", authUser);
    }
    return "index.html";
  }

  /**
   * Sign up the currently authenticated {@link User}. Upon initial being authenticated
   * by an OAuth provider, the user is assigned a {@link Role#ROLE_UNCONFIRMED},
   * then forwarded to a sign up page to pick out a user name. Note, the user is
   * not persisted, and only exists in Http session. From the sign up page, the
   * user picks a name and submits to here to be handled. First we validate the 
   * name and check if it's available. If all is good, we assign the user a role
   * of {@link Role#ROLE_USER} and persists their details to the database. 
   * 
   * @param signUpUser
   * @param authUser
   * @return
   */
  @PostMapping(path = "/api/user/signup", consumes = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<User> signUp(
      @RequestBody @Valid User signUpUser,
      @AuthenticationPrincipal User authUser) 
  {
    LOG.info("Handling sign up: signUpUser={}, oauthUser={}", signUpUser, authUser);
    User createdUser =userService.signUp(User.builder()
        .role(Role.ROLE_USER)
        .email(authUser.getEmail())
        .name(signUpUser.getName())
      .build());
    securityHelper.resetAuthenticatedPrincipal(createdUser);
    return ResponseEntity.ok(createdUser);
  }
  
  @GetMapping("/api/user")
  public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal User user) {
    return ResponseEntity.ok(user);
  }
  
}
