package com.gnoht.tlrl.user;

import com.gnoht.tlrl.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * @author ikumen@gnoht.com
 */
@Service
public class UserService {

  private UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> findAll(Pageable pageable) {
    return userRepository.findAll(pageable).getContent();
  }

  public void delete(User user) {
    userRepository.delete(user);
  }

  /**
   * Find and return a {@link User} with the given email if they exist.
   *
   * @param email
   * @return
   */
  public Optional<User> findByEmail(String email) {
    return userRepository.findOneByEmail(email);
  }

  /**
   * Find and return a {@link User} with the given name if they exist.
   *
   * @param name
   * @return
   */
  public Optional<User> findByName(String name) {
    return userRepository.findOneByName(name);
  }

  /**
   * Given a new user, assign them the {@link Role#ROLE_USER} and save the User
   * to the underlying data store.
   *
   * @param user
   * @return the newly created User with new id.
   * @throws UsernameAlreadyExistsException
   */
  @Transactional
  public User signUp(@NotNull User user) throws UsernameAlreadyExistsException {
    if (user.getId() != null) {
      throw new UserAlreadyExistsException(user);
    } else if (userRepository.existsByName(user.getName())) {
      throw new UsernameAlreadyExistsException(user.getName());
    } else {
      user.setRoles(new HashSet<>(Arrays.asList(Role.ROLE_USER)));
      return userRepository.save(user);
    }
  }
}
