package com.gnoht.tlrl.bookmark.jpa;

import com.gnoht.tlrl.bookmark.Bookmark;
import com.gnoht.tlrl.bookmark.BookmarkCriteria;
import com.gnoht.tlrl.bookmark.BookmarkSpecifications;
import com.gnoht.tlrl.bookmark.QBookmark;
import com.gnoht.tlrl.bookmark.QTag;
import com.gnoht.tlrl.bookmark.QWebUrl;
import com.gnoht.tlrl.bookmark.ReadStatus;
import com.gnoht.tlrl.bookmark.SharedStatus;
import com.gnoht.tlrl.bookmark.Tag;
import com.gnoht.tlrl.bookmark.WebUrl;
import com.gnoht.tlrl.user.QUser;
import com.gnoht.tlrl.user.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author ikumen@gnoht.com
 */
public class BookmarkRepositoryExtensionImpl implements BookmarkRepositoryExtension {

  private EntityManager entityManager;
  private JPAQueryFactory jpaQueryFactory;

  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
    this.jpaQueryFactory = new JPAQueryFactory(entityManager);
  }

  public List<Tag> findRelatedTags(User user, BookmarkSpecifications specifications) {
    QTag _tag = QTag.tag;
    QBookmark _bookmark = QBookmark.bookmark;
    
    return jpaQueryFactory
        .select(Projections
          .constructor(Tag.class, 
              _tag.id.id, 
              _tag.id.id.count()))
        .from(_tag, _bookmark)
        .where(_bookmark.eq(_tag.bookmark)
          .and(((QuerydslBookmarkSpecifications)specifications).toPredicate()))
        .groupBy(_tag.id.id)
        .orderBy(_tag.count().desc(), _tag.id.id.asc())
      .fetch();
    
  }
  
  @Override
  public Page<Bookmark> findAll(User user, BookmarkSpecifications specifications, Pageable pageable) {
    QBookmark _bookmark = QBookmark.bookmark;
    QWebUrl _webUrl = QWebUrl.webUrl;
    QUser _owner = QUser.user;
    QTag _tag0 = new QTag("t0")
        ,_tag1 = new QTag("t1")
        ,_tag2 = new QTag("t2")
        ,_tag3 = new QTag("t3")
        ,_tag4 = new QTag("t4");
    
    List<Bookmark> bookmarks = jpaQueryFactory.
      select(Projections
        .constructor(Bookmark.class,
          _bookmark.id, 
          Projections.constructor(User.class, _owner.id, _owner.name, _owner.email), 
          Projections.constructor(WebUrl.class, _webUrl.id, _webUrl.url, _webUrl.createdDateTime), 
          _bookmark.title, 
          _bookmark.description, 
          _bookmark.readStatus, 
          _bookmark.sharedStatus, 
          _bookmark.createdDateTime, 
          _bookmark.updatedDateTime, 
          _bookmark.archivedDateTime,
          _bookmark.version,
           Projections.constructor(Tag.class, _tag0.id.id).skipNulls(),
           Projections.constructor(Tag.class, _tag1.id.id).skipNulls(),
           Projections.constructor(Tag.class, _tag2.id.id).skipNulls(),
           Projections.constructor(Tag.class, _tag3.id.id).skipNulls(),
           Projections.constructor(Tag.class, _tag4.id.id).skipNulls())
      )
      .from(_bookmark)
        .innerJoin(_bookmark.webUrl, _webUrl)
        .innerJoin(_bookmark.owner, _owner)
        .leftJoin(_bookmark.tags, _tag0).on(_tag0.pos.eq(0))
        .leftJoin(_bookmark.tags, _tag1).on(_tag1.pos.eq(1))
        .leftJoin(_bookmark.tags, _tag2).on(_tag2.pos.eq(2))
        .leftJoin(_bookmark.tags, _tag3).on(_tag3.pos.eq(3))
        .leftJoin(_bookmark.tags, _tag4).on(_tag4.pos.eq(4))
      .where(((QuerydslBookmarkSpecifications) specifications).toPredicate())
        .orderBy(_bookmark.createdDateTime.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
      .fetch();
    
    return new PageImpl<>(bookmarks, pageable, bookmarks.size());
  }
}
