package com.gnoht.tlrl.bookmark;

import com.gnoht.tlrl.core.AlreadyExistsException;
import com.gnoht.tlrl.core.NotAuthorizedException;
import com.gnoht.tlrl.core.NotFoundException;
import com.gnoht.tlrl.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

/**
 * @author ikumen@gnoht.com
 */
@Service
public class BookmarkService {
  protected final static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private BookmarkRepository bookmarkRepository;
  private WebUrlRepository webUrlRepository;

  @Inject
  public BookmarkService(
      BookmarkRepository bookmarkRepository,
      WebUrlRepository webUrlRepository) {
    this.bookmarkRepository = bookmarkRepository;
    this.webUrlRepository = webUrlRepository;
  }

  @Transactional
  public Bookmark create(Bookmark bookmark) throws AlreadyExistsException {

    // Check if we already have bookmark, else continue to save Bookmark
    if (bookmarkRepository.existsByWebUrlUrlAndOwner(
        bookmark.getWebUrl().getUrl(), bookmark.getOwner()))
      throw new BookmarkAlreadyExistsException(bookmark);

    // Use existing WebUrl if present
    webUrlRepository.findOneByUrl(bookmark.getWebUrl().getUrl())
        .ifPresent(bookmark::setWebUrl);

    Bookmark createdBookmark = bookmarkRepository.save(bookmark);
    //onBookmarksCreated(Arrays.asList(createdBookmark));
    return createdBookmark;
  }

  @Transactional
  public Bookmark update(Bookmark partial)
      throws NotFoundException /*, NotAuthorizedException */
  {
    LOG.info("updating: {}", partial);
    // Find the Bookmark to update, error if it doesn't exist
    Bookmark bookmark = bookmarkRepository.findById(partial.getId())
      .orElseThrow(BookmarkNotFoundException::new);

    // Make sure given user is the owner (e.g, partial.owner
    // should have been added and verified upstream.
    LOG.info("bookmark.owner={}, partial.owner={}", bookmark.getOwner(), partial.getOwner());
//    if (!bookmark.getOwner().equals(partial.getOwner()))
//      throw new NotAuthorizedException();

    // Update the updatable properties
    setUpdatableProperties(partial, bookmark);
    //return bookmarkRepository.save(bookmark);
    return bookmark;
    //onBookmarksUpdated(Arrays.asList(bookmark));
  }

  @Transactional
  public void delete(Long id, User owner) throws NotFoundException, NotAuthorizedException
  {
    int deletedCount = bookmarkRepository.deleteOneByIdAndOwner(id, owner);
    if(deletedCount == 0) {
      Optional<Bookmark> results = bookmarkRepository.findById(id);
      if (!results.isPresent())
        throw new BookmarkNotFoundException();
      if (!results.get().getOwner().equals(owner))
        throw new NotAuthorizedException();
    }
//    onBookmarksDeleted(
//        Arrays.asList(Bookmark.Builder.builder()
//            .id(id)
//            .owner(owner)
//            .build()));
  }

  @Transactional
  public void deleteAll(List<Long> ids, User owner)
      throws NotFoundException, NotAuthorizedException {
    int deletedCount = bookmarkRepository.deleteByIdInAndOwner(ids, owner);
    verifyAndHandleUpdatedCount(deletedCount, ids);

//    onBookmarksDeleted(ids.stream()
//        .map(id -> Bookmark.Builder.builder()
//            .id(id).owner(owner).build())
//        .collect(Collectors.toList()));
  }

  @Transactional
  public void updateAll(SharedStatus status, List<Long> ids, User owner)
      throws NotFoundException, NotAuthorizedException {
    int updatedCount = bookmarkRepository.updateSharedStatusByOwnerAndIdIn(
        status, owner, ids, LocalDateTime.now(ZoneOffset.UTC));
    verifyAndHandleUpdatedCount(updatedCount, ids);
//    onBookmarksUpdated(ids.stream()
//        .map(id -> Bookmark.Builder.builder()
//            .id(id)
//            .owner(owner)
//            .sharedStatus(status)
//            .build())
//        .collect(Collectors.toList()));
  }

  @Transactional
  public void updateAll(ReadStatus status, List<Long> ids, User owner)
      throws NotFoundException, NotAuthorizedException {
    int updatedCount = bookmarkRepository.updateReadStatusByOwnerAndIdIn(
        status, owner, ids, LocalDateTime.now(ZoneOffset.UTC));
    verifyAndHandleUpdatedCount(updatedCount, ids);
//    onBookmarksUpdated(ids.stream()
//        .map(id -> Bookmark.Builder.builder()
//            .id(id)
//            .readStatus(status)
//            .owner(owner)
//            .build())
//        .collect(Collectors.toList()));
  }

  public Page<Bookmark> findAll(User owner, Pageable pageable) {
    return bookmarkRepository.findByOwner(owner, pageable);
  }

  private void verifyAndHandleUpdatedCount(int updatedCount, List<Long> ids) {
    if (updatedCount != ids.size()) {
      if (bookmarkRepository.countByIdIn(ids) != ids.size())
        throw new BookmarkNotFoundException();
      throw new NotAuthorizedException();
    }
  }

  private void setUpdatableProperties(Bookmark from, Bookmark to) {
    System.out.println("=========== updating bookmark");
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
