package com.gnoht.tlrl.bookmark;

import com.gnoht.tlrl.user.User;
import com.gnoht.tlrl.user.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ikumen@gnoht.com
 */
@SpringBootTest
@Transactional
public class BookmarkServiceTests {

  @Autowired BookmarkService bookmarkService;
  @Autowired BookmarkRepository bookmarkRepository;
  @Autowired UserService userService;

  User user;

  @BeforeEach
  @Transactional(propagation = Propagation.NEVER)
  public void beforeEach() {
    if (user == null) {
      user = userService.signUp(new User(null, "user1", "user1@acme.org", new HashSet<>()));
    }
  }

  @Test
  public void shouldCreateNewBookmark() {
    assertEquals(0, bookmarkRepository.count());
    Bookmark bookmark = Bookmark.builder()
        .webUrl(new WebUrl("https://spring.io/"))
        .owner(user)
        .tag(new Tag("java"))
        .build();

    assertNull(bookmark.getId());
    bookmark = bookmarkService.create(bookmark);
    assertEquals(1, bookmarkRepository.count());
    assertNotNull(bookmark.getId());
  }

  @Test
  public void shouldUpdateBookmarkWithNewTags() {
    Bookmark bookmark = bookmarkService.create(
      Bookmark.builder()
        .webUrl(new WebUrl("https://spring.io/"))
        .owner(user)
        .tag(new Tag("java"))
      .build());

    Tag tag = new Tag("tlrl");
    Bookmark updated = bookmarkService.update(
      Bookmark.Builder.with(bookmark)
        .tags(Arrays.asList(tag))
      .build());

    //assertTrue(updated.getTags().size() == 1);
    //assertTrue(updated.getTags().contains(tag));
    bookmarkRepository.findById(updated.getId())
        .ifPresent(b -> {
          System.out.println("---- after updating");
          b.getTags().forEach(System.out::println);
        });
  }

  @Test
  public void shouldThrowBookmarkAlreadyExists() {
    Bookmark bookmark = Bookmark.builder()
        .webUrl(new WebUrl("https://spring.io/"))
        .owner(user)
        .build();

    bookmarkService.create(bookmark);
    Exception ex = assertThrows(BookmarkAlreadyExistsException.class,
        () -> bookmarkService.create(bookmark));
    assertEquals("Bookmark already exists.", ex.getMessage());
  }

  @Test
  public void shouldStartWithZeroBookmarks() {
    assertEquals(0, bookmarkRepository.count());
  }
}
