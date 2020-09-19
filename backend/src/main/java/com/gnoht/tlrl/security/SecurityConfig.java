package com.gnoht.tlrl.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import com.gnoht.tlrl.security.oauth.DelegatingOAuth2UserService;
import com.gnoht.tlrl.security.oauth.DelegatingOidcUserService;

/**
 * 
 * @author ikumen@gnoht.com
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  
  public static final String BASE_API_URL = "/api";
  // Secured api endpoints
  public static final String USER_API_URL = BASE_API_URL + "/user";
  public static final String BOOKMARK_API_URL = BASE_API_URL +"/bookmarks/**";
  
  // Open to unconfirmed users
  public static final String SIGNUP_API_URL = BASE_API_URL + "/signup";
  
  @Autowired
  private DelegatingOAuth2UserService oauth2UserAdapterService;
  
  @Autowired
  private DelegatingOidcUserService oidcUserAdapterService;
  
  @Bean
  public CsrfTokenRepository csrfTokenRepository() {
    return new HttpSessionCsrfTokenRepository();
  }
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .formLogin().disable()
      .httpBasic().disable()
      .authorizeRequests()
        .antMatchers(USER_API_URL, BOOKMARK_API_URL, "/archive/**").hasAuthority(Role.ROLE_USER.name())
        .antMatchers(SIGNUP_API_URL).hasAuthority(Role.ROLE_UNCONFIRMED.name())
        .anyRequest().permitAll()
        .and()
      .csrf().csrfTokenRepository(csrfTokenRepository())
        .and()
      .headers().addHeaderWriter(new CsrfHeaderWriter(csrfTokenRepository()))
        .and()
      .exceptionHandling()
        .accessDeniedHandler(new UnconfirmedUserAccessDeniedHandler())
        .authenticationEntryPoint(new UnauthorizedEntryPoint())
      .and()
      .logout()
        .logoutUrl("/api/user/signout")
        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
        .and()//.cors().disable()
          //.csrf().disable()
      .oauth2Login()
        .loginPage("/signin")
        .successHandler(new UnconfirmedUserAuthenticationSuccessHandler())
        .userInfoEndpoint()
          .oidcUserService(oidcUserAdapterService)
          .userService(oauth2UserAdapterService);
  }
  
}
