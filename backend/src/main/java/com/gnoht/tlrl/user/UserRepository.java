package com.gnoht.tlrl.user;

import com.gnoht.tlrl.core.Repository;

import java.util.Optional;

/**
 * @author ikumen@gnoht.com
 */
public interface UserRepository extends Repository<User, Long> {

  /**
   * Find {@link User} by the given name.
   * @param name
   * @return
   */
  Optional<User> findOneByName(String name);

  /**
   * Find {@link User} by the given email.
   * @param email
   * @return
   */
  Optional<User> findOneByEmail(String email);

  boolean existsByName(String name);

  boolean existsByEmail(String email);
}
