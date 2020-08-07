package com.gnoht.tlrl.bookmark.jpa;

import com.gnoht.tlrl.bookmark.Bookmark;
import com.gnoht.tlrl.bookmark.BookmarkRepository;
import com.gnoht.tlrl.core.jpa.CustomSaveRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author ikumen@gnoht.com
 */
public class JpaBookmarkRepositoryImpl
      implements CustomSaveRepository<Bookmark, Long> {

  private EntityManager entityManager;

  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Bookmark save(Bookmark bookmark) {
    if (bookmark.getId() == null) {
      System.out.println("----------- persisting");
      entityManager.persist(bookmark);
      return bookmark;
    }
    System.out.println("-------- merging");
    return entityManager.merge(bookmark);
  }
}
