package com.gnoht.tlrl.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gnoht.tlrl.web.ArchiveController;
import com.gnoht.tlrl.web.SpaEntryController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;

public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
  
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {

    if (request.getRequestURI().startsWith(ArchiveController.ARCHIVE_URI_PREFIX)) {
      response.sendRedirect(SpaEntryController.DEFAULT_ENTRY_URI);
    } else {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
  }

}
