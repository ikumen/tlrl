package com.gnoht.tlrl.bookmark.jpa;

import com.gnoht.tlrl.bookmark.Bookmark;
import com.gnoht.tlrl.bookmark.BookmarkRepository;
import com.gnoht.tlrl.bookmark.ReadStatus;
import com.gnoht.tlrl.bookmark.SharedStatus;
import com.gnoht.tlrl.core.jpa.CustomSaveRepository;
import com.gnoht.tlrl.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ikumen@gnoht.com
 */
public interface JpaBookmarkRepository
    extends JpaRepository<Bookmark, Long>, BookmarkRepository, CustomSaveRepository<Bookmark, Long> {

  @Override
  @Transactional
  @Modifying(clearAutomatically = true)
  @Query(value = "update Bookmark b SET b.readStatus = :status, b.updatedDateTime = :updatedDateTime where b.owner = :owner and b.id in :ids")
  int updateReadStatusByOwnerAndIdIn(ReadStatus status, User owner, List<Long> ids, LocalDateTime updatedDateTime);

  @Override
  @Transactional
  @Modifying(clearAutomatically = true)
  @Query(value = "update Bookmark b set b.sharedStatus = :status, b.updatedDateTime = :updatedDateTime where b.owner = :owner and b.id in :ids")
  int updateSharedStatusByOwnerAndIdIn(SharedStatus status, User owner, List<Long> ids, LocalDateTime updatedDateTime);

  @Override
  @Transactional
  @Modifying(clearAutomatically = true)
  @Query(value = "delete Bookmark b where b.id = :id and b.owner = :owner")
  int deleteOneByIdAndOwner(Long id, User owner);

  @Override
  @Transactional
  int deleteByIdInAndOwner(List<Long> ids, User owner);
}
