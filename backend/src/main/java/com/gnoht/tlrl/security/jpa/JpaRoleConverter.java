package com.gnoht.tlrl.security.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.gnoht.tlrl.security.Role;

/**
import javax.persistence.Converter;

/**
 * Converts {@link Role} enum to it's {@link Role#getCode()} for persistence and
 * from it's code when retrieving from the database.
 *
 * @author ikumen@gnoht.com
 */
@Converter(autoApply = true)
public class JpaRoleConverter implements AttributeConverter<Role, String> {

  @Override
  public String convertToDatabaseColumn(Role role) {
    return role.getCode();
  }

  @Override
  public Role convertToEntityAttribute(String code) {
    return Role.lookup(code);
  }
}