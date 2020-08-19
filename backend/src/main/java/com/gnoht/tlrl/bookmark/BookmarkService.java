package com.gnoht.tlrl.bookmark;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnoht.tlrl.bookmark.repository.BookmarkQueryFilter;
import com.gnoht.tlrl.bookmark.repository.BookmarkRepository;
import com.gnoht.tlrl.bookmark.repository.WebUrlRepository;
import com.gnoht.tlrl.bookmark.repository.jpa.JpaBookmarkRepository;
import com.gnoht.tlrl.core.AlreadyExistsException;
import com.gnoht.tlrl.core.NotAuthorizedException;
import com.gnoht.tlrl.search.SearchService;
import com.gnoht.tlrl.user.User;

/**
 * {@link BookmarkService} implementation that delegates core CRUD operations
 * to an underlying JPA repository, specifically {@link JpaBookmarkRepository}.
 *
 * @author ikumen@gnoht.com
 */
@Service
public class BookmarkService {

  private final static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private BookmarkRepository bookmarkRepository;
  private WebUrlRepository webUrlRepository;
  private SearchService<BookmarkResults, BookmarkQueryFilter> searchService;

  @Inject
  public BookmarkService(SearchService<BookmarkResults, BookmarkQueryFilter> searchService,
      BookmarkRepository bookmarkRepository, WebUrlRepository webUrlRepository) {
    this.searchService = searchService;
    this.bookmarkRepository = bookmarkRepository;
    this.webUrlRepository = webUrlRepository;
  }

  /**
   * 
   * @param bookmark
   * @return
   * @throws AlreadyExistsException
   */
  @Transactional
  public Bookmark create(Bookmark bookmark) throws AlreadyExistsException {

    WebUrl webUrl = bookmark.getWebUrl();
    // Check if we already have bookmark, else continue to save Bookmark
    if (bookmarkRepository.existsByWebUrlUrlAndOwner(webUrl.getUrl(), bookmark.getOwner()))
      throw new BookmarkAlreadyExistsException(bookmark);

    Bookmark.Builder builder = Bookmark.Builder.with(bookmark);
    // Use existing WebUrl if present
    webUrlRepository.findOneByUrl(bookmark.getWebUrl().getUrl())
        .ifPresent(builder::webUrl);

    return bookmarkRepository.save(builder.build());
  }

  /**
   * 
   * @param partial
   * @return
   * @throws BookmarkNotFoundException
   * @throws NotAuthorizedException
   */
  @Transactional
  public Bookmark update(Bookmark partial)
      throws BookmarkNotFoundException, NotAuthorizedException {
    LOG.info("updating: {}", partial);
    // Find the Bookmark to update, error if it doesn't exist
    Bookmark bookmark = bookmarkRepository.findById(partial.getId())
        .orElseThrow(BookmarkNotFoundException::new);

    // Make sure given user is the owner (e.g, partial.owner
    // should have been added and verified upstream.
    LOG.debug("bookmark.owner={}, partial.owner={}", bookmark.getOwner(), partial.getOwner());
    if (!bookmark.getOwner().equals(partial.getOwner()))
      throw new NotAuthorizedException();

    // Update the updatable properties
    setUpdatableProperties(partial, bookmark);
    return bookmark;
  }

  /**
   * 
   * @param id
   * @param owner
   * @throws BookmarkNotFoundException
   * @throws NotAuthorizedException
   */
  @Transactional
  public void delete(Long id, User owner)
      throws BookmarkNotFoundException, NotAuthorizedException {
    LOG.info("deleting: id={}, owner={}", id, owner);
    int deletedCount = bookmarkRepository.deleteOneByIdAndOwner(id, owner);
    if(deletedCount == 0) {
      Optional<Bookmark> results = bookmarkRepository.findById(id);
      if (!results.isPresent())
        throw new BookmarkNotFoundException();
      if (!results.get().getOwner().equals(owner))
        throw new NotAuthorizedException();
    }
  }

  /**
   * 
   * @param ids
   * @param owner
   * @throws BookmarkNotFoundException
   * @throws NotAuthorizedException
   */
  @Transactional
  public void deleteAll(List<Long> ids, User owner)
      throws BookmarkNotFoundException, NotAuthorizedException {
    int deletedCount = bookmarkRepository.deleteByIdInAndOwner(ids, owner);
    verifyAndHandleUpdatedCount(deletedCount, ids);
  }

  /**
   * 
   * @param status
   * @param ids
   * @param owner
   * @throws BookmarkNotFoundException
   * @throws NotAuthorizedException
   */
  @Transactional
  public void updateAllWithSharedStatus(SharedStatus status, List<Long> ids, User owner)
      throws BookmarkNotFoundException, NotAuthorizedException {
    int updatedCount = bookmarkRepository.updateSharedStatusByOwnerAndIdIn(
        status, owner, ids, LocalDateTime.now(ZoneOffset.UTC));
    verifyAndHandleUpdatedCount(updatedCount, ids);
  }

  /**
   * 
   * @param status
   * @param ids
   * @param owner
   * @throws BookmarkNotFoundException
   * @throws NotAuthorizedException
   */
  @Transactional
  public void updateAllWithReadStatus(ReadStatus status, List<Long> ids, User owner)
      throws BookmarkNotFoundException, NotAuthorizedException {
    int updatedCount = bookmarkRepository.updateReadStatusByOwnerAndIdIn(
        status, owner, ids, LocalDateTime.now(ZoneOffset.UTC));
    verifyAndHandleUpdatedCount(updatedCount, ids);
  }

  /**
   * 
   * @param user
   * @param criteria
   * @return
   */
  public BookmarkResults findAllWithFacets(User user, BookmarkQueryFilter queryFilter, Pageable pageable) {
    queryFilter.setOwner(Optional.of(user));
    return bookmarkRepository.findAllWithFacets(queryFilter, pageable);
  }

  /**
   * 
   * @param user
   * @param criteria
   * @param pageable
   * @return
   */
  public Page<Bookmark> findAll(User user, BookmarkQueryFilter queryFilter, Pageable pageable) {
    queryFilter.setOwner(Optional.of(user));
    return bookmarkRepository.findAll(queryFilter, pageable);
  }

  /**
   * 
   * @param terms
   * @param user
   * @param queryFilters
   * @param pageable
   * @return
   */
  public BookmarkResults searchAllWithFacets(String terms, User user, BookmarkQueryFilter queryFilters, Pageable pageable) {
    queryFilters.setOwner(Optional.of(user));
    return searchService.search(terms, queryFilters, pageable);
  }

  private void verifyAndHandleUpdatedCount(int updatedCount, List<Long> ids) {
    if (updatedCount != ids.size()) {
      if (bookmarkRepository.countByIdIn(ids) != ids.size())
        throw new BookmarkNotFoundException();
      throw new NotAuthorizedException();
    }
  }
  
  private void setUpdatableProperties(Bookmark from, Bookmark to) {
    if (from.getTitle() != null)
      to.setTitle(from.getTitle());
    if (from.getDescription() != null)
      to.setDescription(from.getDescription());
    if (from.getSharedStatus() != null)
      to.setSharedStatus(from.getSharedStatus());
    if (from.getReadStatus() != null)
      to.setReadStatus(from.getReadStatus());
    if (from.getTags() != null)
      to.setTags(from.getTags());
    if (from.getArchivedDateTime() != null)
      to.setArchivedDateTime(from.getArchivedDateTime());
    to.setUpdatedDateTime(LocalDateTime.now(ZoneOffset.UTC));
  }

}
