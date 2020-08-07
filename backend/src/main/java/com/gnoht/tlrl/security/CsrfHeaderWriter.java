/**
 * 
 */
package com.gnoht.tlrl.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.header.HeaderWriter;

/**
 * Writes CSRF tokens to all HTTP Response Headers.
 * 
 * @author ikumen
 */
public class CsrfHeaderWriter implements HeaderWriter {
  
  private CsrfTokenRepository csrfTokenRepository;
  
  public CsrfHeaderWriter(CsrfTokenRepository csrfTokenRepository) {
    this.csrfTokenRepository = csrfTokenRepository;
  }

  @Override
  public void writeHeaders(HttpServletRequest request, HttpServletResponse response) {
    CsrfToken csrfToken = csrfTokenRepository.loadToken(request);
    if (csrfToken == null) {
      csrfToken = csrfTokenRepository.generateToken(request);
      csrfTokenRepository.saveToken(csrfToken, request, response);
    }
    response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
    response.setHeader(csrfToken.getParameterName(), csrfToken.getToken());
  }

}
