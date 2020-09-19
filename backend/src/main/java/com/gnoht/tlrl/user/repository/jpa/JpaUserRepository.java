package com.gnoht.tlrl.user.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gnoht.tlrl.user.User;
import com.gnoht.tlrl.user.repository.UserRepository;

/**
 * @author ikumen@gnoht.com
 */
@Repository
public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {

  @Override
  @Query(value = "SELECT u FROM User u WHERE u.name = ?1")
  Optional<User> findOneByName(String name);

  @Override
  @EntityGraph(value = "User.roles", type = EntityGraph.EntityGraphType.LOAD)
  Optional<User> findOneByOauthUserId(String email);
}
