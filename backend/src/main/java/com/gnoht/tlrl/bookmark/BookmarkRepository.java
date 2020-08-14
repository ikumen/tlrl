/**
 * 
 */
package com.gnoht.tlrl.bookmark;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.gnoht.tlrl.support.Specification;
import com.gnoht.tlrl.user.User;

/**
 * @author ikumen
 */
public interface BookmarkRepository {
  
  /**
   * Save given {@link Bookmark} to the underlying data repository.
   * 
   * @param <S>
   * @param entity
   * @return
   */
  <S extends Bookmark> S save(S bookmark);

  /**
   * Find a {@link Bookmark} by the given id.
   * @param id
   * @return
   */
  Optional<Bookmark> findById(Long id);
  
  /**
   * Find all {@link Bookmark}s belonging to given {@link User}, filtered by the given {@link Specification}.
   * 
   * @param user
   * @param specification
   * @param pageable
   * @return
   */
  Page<Bookmark> findAll(User user, BookmarkSpecifications specifications, Pageable pageable);
  
  /**
   * 
   * @param user
   * @param specifications
   * @return
   */
  List<Tag> findRelatedTags(User user, BookmarkSpecifications specifications);
  
  /**
   * 
   * @return
   */
  BookmarkSpecifications newSpecifications();

  /**
   * Find the {@link Bookmark} with given url and owner.
   *
   * Note: owner will always exists, so passing in the entity is possible,
   * where WebUrl maybe new/transient so query explicitly for WebUrl.url,
   * thus forcing a join in the resulting SQL.
   *
   * @param url
   * @param owner
   * @return
   */
  Optional<Bookmark> findOneByWebUrlUrlAndOwner(String url, User owner);

  /**
   * True if {@link Bookmark} with given {@link WebUrl} and {@link User} exists.
   * @param url
   * @param owner
   * @return
   */
  boolean existsByWebUrlUrlAndOwner(String url, User owner);

  /**
   * Finds all {@link Bookmark}s belonging to the given {@link User}.
   *
   * @param owner
   * @param pageable
   * @return
   */
  Page<Bookmark> findByOwner(User owner, Pageable pageable);

  /**
   * Find all {@link Bookmark}s owned by given user and in given id list.
   *
   * @param owner
   * @param ids
   * @return
   */
  List<Bookmark> findByOwnerAndIdIn(User owner, List<Long> ids);

  /**
   * Find all {@link Bookmark}s by given id list and return the count.
   * @param ids
   * @return
   */
  int countByIdIn(List<Long> ids);

  /**
   * Update the {@link ReadStatus} for all {@link Bookmark}s in given list and
   * owned by given {@link User}.
   *
   * @param status
   * @param owner
   * @param ids
   * @return
   */
  int updateReadStatusByOwnerAndIdIn(ReadStatus status, User owner, List<Long> ids, LocalDateTime updatedDateTime);

  /**
   * Update the {@link SharedStatus} for all {@link Bookmark}s in given list and
   * owned by given {@link User}.
   *
   * @param status
   * @param owner
   * @param ids
   * @return
   */
  int updateSharedStatusByOwnerAndIdIn(SharedStatus status, User owner, List<Long> ids, LocalDateTime updatedDateTime);

  /**
   * Deletes a {@link Bookmark} by id and {@link User}.
   *
   * @param id
   * @param owner
   * @return
   */
  int deleteOneByIdAndOwner(Long id, User owner);

  /**
   * Deletes all {@link Bookmark}s in given list and owned by {@link User}.
   *
   * @param ids
   * @param owner
   * @return
   */
  @Transactional
  int deleteByIdInAndOwner(List<Long> ids, User owner);
}
