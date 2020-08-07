package com.gnoht.tlrl.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ikumen@gnoht.com
 */
@Controller
public class SpaEntryController {

  /**
   * Return the SPA for the given paths.
   * @return
   */
  @GetMapping(path = {
      "/",
      "/@**",
      "/about",
      "/doh",
      "/help",
      "/search",
      "/signin"
  })
  public String index() {
    return "index.html";
  }

  /**
   * Return the bookmarklet add page.
   * @return
   */
  @GetMapping(path = {"/add"})
  public String add() {
    return "add.html";
  }
}
