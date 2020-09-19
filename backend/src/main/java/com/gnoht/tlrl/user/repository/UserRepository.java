package com.gnoht.tlrl.user.repository;

import java.util.Optional;

import com.gnoht.tlrl.core.repository.Repository;
import com.gnoht.tlrl.user.User;

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

  /**
   * Find {@link User} by the given oauthUserId.
   * @param oauthUserId
   * @return
   */
  Optional<User> findOneByOauthUserId(String oauthUserId);

  boolean existsByName(String name);

  boolean existsByEmail(String email);
}
