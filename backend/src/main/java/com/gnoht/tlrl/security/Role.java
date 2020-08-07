package com.gnoht.tlrl.security;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
  ROLE_ADMIN("ADM"),
  ROLE_UNCONFIRMED("UNC"),
  ROLE_USER("USR");

  public static final int CODE_LENGTH = 3;
  
  private String code;
  
  private Role(String code) {
    this.code = code;
  }

  @Override
  public String getAuthority() {
    return name();
  }
  
  public String getCode() {
    return code;
  }
  
  private static Map<String, Role> roles = new HashMap<>();
  
  /**
   * Lookup a Role for the given code.
   * @param code
   * @return
   */
  public static Role lookup(String code) {
	  if (!roles.containsKey(code))
	    throw new NoSuchElementException(code);
	  return roles.get(code);
  }

  static {
    for (Role role: Role.values()) {
      if (roles.containsKey(role.code))
        throw new IllegalArgumentException("Duplicate Role code: '" + role.code + "'");
      if (role.code.length() != CODE_LENGTH)
        throw new IllegalArgumentException("Role code must be " + CODE_LENGTH + " chars.");
      roles.put(role.code, role);
    }
  }
  
}
