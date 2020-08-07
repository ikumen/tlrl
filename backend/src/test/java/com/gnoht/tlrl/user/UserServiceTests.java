package com.gnoht.tlrl.user;

import com.gnoht.tlrl.security.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ikumen@gnoht.com
 */
@SpringBootTest
@Transactional
public class UserServiceTests {

  @Autowired
  private UserService userService;

  @Test
  public void shouldThrowUsernameAlreadyExistsException() {
    User user1 = new User();
    user1.setEmail("user1@acme.org");
    user1.setName("user1");
    userService.signUp(user1);

    User user2 = new User();
    user2.setEmail("user2@acme.org");
    user2.setName("user1"); // duplicate name

    Exception ex = assertThrows(UsernameAlreadyExistsException.class,
      () -> userService.signUp(user2));

    assertEquals("User name '" + user2.getName() + "' already taken!",
      ex.getMessage());
  }

  @Test
  @Transactional(propagation = Propagation.NEVER)
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  public void shouldDataIntegrityViolationExceptionIfEmailExists() {
    User user1 = new User();
    user1.setEmail("user1@acme.org");
    user1.setName("user1");
    userService.signUp(user1);

    User user2 = new User();
    user2.setEmail("user1@acme.org"); // duplicate email
    user2.setName("user2");

    assertThrows(DataIntegrityViolationException.class,
      () -> userService.signUp(user2));
  }

  @Test
  public void shouldAutomaticallyAssignRoleUserToSignedUpUsers() {
    User user1 = new User();
    user1.setEmail("user1@acme.org");
    user1.setName("us");
    user1.setRoles(new HashSet<>(Arrays.asList(Role.ROLE_UNCONFIRMED)));

    userService.signUp(user1);
    assertTrue(!user1.getRoles().contains(Role.ROLE_UNCONFIRMED));
    assertTrue(user1.getRoles().contains(Role.ROLE_USER));
  }
}
