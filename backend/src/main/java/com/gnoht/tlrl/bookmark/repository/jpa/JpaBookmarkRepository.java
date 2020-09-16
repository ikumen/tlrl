package com.gnoht.tlrl.bookmark.repository.jpa;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gnoht.tlrl.bookmark.Bookmark;
import com.gnoht.tlrl.bookmark.ReadStatus;
import com.gnoht.tlrl.bookmark.SharedStatus;
import com.gnoht.tlrl.bookmark.repository.BookmarkRepository;
import com.gnoht.tlrl.user.User;

/**
 * @author ikumen@gnoht.com
 */
public interface JpaBookmarkRepository extends JpaRepository<Bookmark, Long>, 
      BookmarkRepository, BookmarkRepositoryExtension {
  
  /**
   * Update the {@link ReadStatus} for all {@link Bookmark}s in given list and
   * owned by given {@link User}.
   *
   * @param status
   * @param owner
   * @param ids
   * @return
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(value = "update Bookmark b SET b.readStatus = :status, b.updatedDateTime = :updatedDateTime where b.owner = :owner and b.id in :ids")
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
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(value = "update Bookmark b set b.sharedStatus = :status, b.updatedDateTime = :updatedDateTime where b.owner = :owner and b.id in :ids")
  int updateSharedStatusByOwnerAndIdIn(SharedStatus status, User owner, List<Long> ids, LocalDateTime updatedDateTime);

  /**
   * 
   * @param owner
   * @param id
   * @param archivedDateTime
   * @return
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(value = "update Bookmark b set b.archivedDateTime = :archivedDateTime where b.owner = :owner and b.id = :id")
  int updateArchivedDateTimeByOwnerAndId(User owner, Long id, LocalDateTime archivedDateTime);
  
  /**
   * Deletes a {@link Bookmark} by id and {@link User}.
   *
   * @param id
   * @param owner
   * @return
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(value = "delete Bookmark b where b.id = :id and b.owner = :owner")
  int deleteOneByIdAndOwner(Long id, User owner);

  /**
   * Deletes all {@link Bookmark}s in given list and owned by {@link User}.
   *
   * @param ids
   * @param owner
   * @return
   */
  @Transactional
  @Modifying
  @Query(value = "delete Bookmark b where b.owner = :owner and b.id in :ids")
  int deleteByIdInAndOwner(List<Long> ids, User owner);
  
}
