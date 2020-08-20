package com.gnoht.tlrl.bookmark;

import java.lang.invoke.MethodHandles;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.gnoht.tlrl.bookmark.repository.BookmarkQueryFilter;
import com.gnoht.tlrl.user.User;

/**
 * @author ikumen@gnoht.com
 */
@RestController
@RequestMapping(path = "/api/bookmarks")
public class BookmarkController {
  private final static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private BookmarkService bookmarkService;

  @Inject
  public BookmarkController(BookmarkService bookmarkService) {
    this.bookmarkService = bookmarkService;
  }

  /**
   * Handles request for creating a new {@link Bookmark}.
   *
   * @param bookmark
   * @param uriBuilder
   * @param user
   * @return
   */
  @PostMapping
  public ResponseEntity<Bookmark> create(@RequestBody Bookmark bookmark, UriComponentsBuilder uriBuilder,
      @AuthenticationPrincipal User user) {
    LOG.info("Creating bookmark: {}", bookmark);
    bookmark.setOwner(user);
    Bookmark createdBookmark = bookmarkService.create(bookmark);

    UriComponents uriComponents = uriBuilder.path("/api/bookmarks/{1}").buildAndExpand(createdBookmark.getId());

    return ResponseEntity.created(uriComponents.toUri()).body(createdBookmark);
  }

  /**
   * Handles request for updating the given {@link Bookmark}.
   *
   * @param id
   * @param partial
   * @return
   */
  @PatchMapping(path = "/{id}")
  public ResponseEntity<Bookmark> update(@PathVariable Long id, @RequestBody Bookmark partial,
      @AuthenticationPrincipal User user) {
    LOG.info("Updating bookmark: id={}, partial={}", id, partial);
    partial.setOwner(user);
    partial.setId(id);
    Bookmark updatedBookmark = bookmarkService.update(partial);
    return ResponseEntity.ok(updatedBookmark);
  }

  /**
   * Updates the {@link ReadStatus} for {@link Bookmark}s in the given id list.
   *
   * @param readStatus
   * @param bookmarkIds
   * @return
   */
  @PatchMapping("/read/{status}")
  public ResponseEntity<List<Long>> updateAll(@PathVariable("status") ReadStatus readStatus,
      @RequestBody List<Long> bookmarkIds, @AuthenticationPrincipal User user) {
    LOG.info("Updating ReadStatus: status={}, ids={}", readStatus, bookmarkIds);
    bookmarkService.updateAllWithReadStatus(readStatus, bookmarkIds, user);
    return ResponseEntity.ok(bookmarkIds);
  }

  /**
   * Updates the {@link SharedStatus} for {@link Bookmark}s in the given id list.
   *
   * @param sharedStatus
   * @param bookmarkIds
   * @return
   */
  @PatchMapping("/shared/{status}")
  public ResponseEntity<List<Long>> updateAll(@PathVariable("status") SharedStatus sharedStatus,
      @RequestBody List<Long> bookmarkIds, @AuthenticationPrincipal User user) {
    LOG.info("Updating SharedStatus: status={}, ids={}", sharedStatus, bookmarkIds);
    bookmarkService.updateAllWithSharedStatus(sharedStatus, bookmarkIds, user);
    return ResponseEntity.ok(bookmarkIds);
  }

  /**
   * Deletes a {@link Bookmark} by the given id.
   *
   * @param id
   * @return
   */
  @DeleteMapping(path = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
    LOG.info("Deleting id={}", id);
    bookmarkService.delete(id, user);
    return ResponseEntity.ok().build();
  }

  /**
   * Deletes all {@link Bookmark}s in the given id list.
   *
   * @param ids
   * @return
   */
  @DeleteMapping()
  public ResponseEntity<List<Long>> deleteAll(@RequestBody List<Long> ids, @AuthenticationPrincipal User user) {
    LOG.info("Deleting all ids={}", ids);
    bookmarkService.deleteAll(ids, user);
    return ResponseEntity.ok(ids);
  }

  @GetMapping("/facets")
  public ResponseEntity<Page<Bookmark>> findAll(@Valid BookmarkQueryFilter queryFilter, 
      @PageableDefault Pageable pageable, @AuthenticationPrincipal User user) {
    LOG.info("findAll: queryFilter={}", queryFilter);
    Page<Bookmark> results = bookmarkService.findAll(user, queryFilter, pageable);
    return ResponseEntity.ok(results);
  }
  
  @GetMapping
  public ResponseEntity<BookmarkResults> findAllWithFacets(
      @Valid BookmarkQueryFilter queryFilter, 
      @PageableDefault Pageable pageable,
      @AuthenticationPrincipal User user) {
    LOG.info("findAllFacets: queryFilter={}", queryFilter);
    return ResponseEntity.ok(bookmarkService.findAllWithFacets(user, queryFilter, pageable));
  }

  @GetMapping(path = "/search")
  public ResponseEntity<BookmarkResults> searchAllWithFacets(
      @RequestParam(name = "terms", required = false, defaultValue = "") String terms,
      @Valid BookmarkQueryFilter queryFilter, @PageableDefault Pageable pageable, 
      @AuthenticationPrincipal User user) {
    LOG.info("Searching for: {}", terms);
    return ResponseEntity.ok(bookmarkService
        .searchAllWithFacets(terms, user, queryFilter, pageable));
  }

}
