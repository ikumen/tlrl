package com.gnoht.tlrl.web;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gnoht.tlrl.user.User;

/**
 * @author ikumen@gnoht.com
 */
@Controller
@RequestMapping(value = ArchiveController.ARCHIVE_URI_PREFIX)
public class ArchiveController {

  public static final String ARCHIVE_URI_PREFIX = "/archive";

  @GetMapping("/{userId}/{docId}")
  public void getArchive(
      HttpServletResponse response,
      @PathVariable Long userId,
      @PathVariable String docId,
      @AuthenticationPrincipal User user)
  {
    if (user == null || !user.getId().equals(userId)) {
      throw new AccessDeniedException("You are not authorized");
    } else {
      // An empty Content-Type will force nginx to determine appropriate type
      response.addHeader("Content-Type", "");
      response.addHeader("X-Accel-Redirect", "/_archive/" + userId + "/" + docId + ".pdf");
    }
  }
}
