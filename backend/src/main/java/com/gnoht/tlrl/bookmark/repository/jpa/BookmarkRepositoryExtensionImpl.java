package com.gnoht.tlrl.bookmark.repository.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.gnoht.tlrl.bookmark.Bookmark;
import com.gnoht.tlrl.bookmark.BookmarkFacets;
import com.gnoht.tlrl.bookmark.QBookmark;
import com.gnoht.tlrl.bookmark.QTag;
import com.gnoht.tlrl.bookmark.QWebUrl;
import com.gnoht.tlrl.bookmark.ReadStatusFacet;
import com.gnoht.tlrl.bookmark.SharedStatusFacet;
import com.gnoht.tlrl.bookmark.Tag;
import com.gnoht.tlrl.bookmark.TagFacet;
import com.gnoht.tlrl.bookmark.WebUrl;
import com.gnoht.tlrl.bookmark.repository.BookmarkQueryFilter;
import com.gnoht.tlrl.user.QUser;
import com.gnoht.tlrl.user.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

/**
 * @author ikumen@gnoht.com
 */
public class BookmarkRepositoryExtensionImpl implements BookmarkRepositoryExtension {

  private EntityManager entityManager;
  private JPAQueryFactory jpaQueryFactory;
  private final QBookmark _bookmark = QBookmark.bookmark;
  private final QWebUrl _webUrl = QWebUrl.webUrl;
  private final QUser _owner = QUser.user;
  private final QTag _tag0 = new QTag("t0")
      ,_tag1 = new QTag("t1")
      ,_tag2 = new QTag("t2")
      ,_tag3 = new QTag("t3")
      ,_tag4 = new QTag("t4");

  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
    this.jpaQueryFactory = new JPAQueryFactory(entityManager);
  }
  
  @Override
  public BookmarkFacets findAllFacets(User user, BookmarkQueryFilter queryFilter) {
    QTag _tag = QTag.tag;
    
    JPAQuery<Long> findAllSubQuery = createFindAllQuery(user, queryFilter);
    
    BooleanBuilder excludeTagPredicate = new BooleanBuilder();
    queryFilter.getTags().forEach(t -> excludeTagPredicate.and(_tag.id.id.ne(t.getId())));
    
    List<TagFacet> tags = jpaQueryFactory
      .select(Projections.constructor(TagFacet.class, 
          Projections.constructor(Tag.class, _tag.id.id), _tag.id.id.count()))
      .from(_tag, _bookmark)
      .where(_bookmark.eq(_tag.bookmark)
          .and(_bookmark.id.in(findAllSubQuery))
          .and(excludeTagPredicate))
      .groupBy(_tag.id.id)
      .orderBy(_tag.count().desc(), _tag.id.id.asc())
      .fetch();

    List<SharedStatusFacet> sharedStatuses = findAllSubQuery.clone() // modifies state, make a clone
      .select(Projections.constructor(SharedStatusFacet.class, 
          _bookmark.sharedStatus, _bookmark.sharedStatus.count()))
      .groupBy(_bookmark.sharedStatus)
      .fetch();
      
    List<ReadStatusFacet> readStatuses = findAllSubQuery // modifies state as well, but last usage
      .select(Projections.constructor(ReadStatusFacet.class, 
          _bookmark.readStatus, _bookmark.readStatus.count()))
      .groupBy(_bookmark.readStatus)
      .orderBy(_bookmark.readStatus.asc())
      .fetch();
    
    return new BookmarkFacets(tags, sharedStatuses, readStatuses);
  }
  
  @Override
  public Page<Bookmark> findAll(User user, BookmarkQueryFilter queryFilter, Pageable pageable) {
    
    JPAQuery<Bookmark> findAllQuery = createFindAllQuery(user, queryFilter)
      .select(Projections.constructor(Bookmark.class,
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
           Projections.constructor(Tag.class, _tag4.id.id).skipNulls()));

    List<Bookmark> bookmarks = findAllQuery
        .orderBy(_bookmark.createdDateTime.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
      .fetch();
    
    long count = findAllQuery.fetchCount();
        
    return new PageImpl<>(bookmarks, pageable, count);
  }
  
  private Predicate buildFindAllWherePredicate(User user, BookmarkQueryFilter queryFilter) {
    BooleanBuilder builder = new BooleanBuilder(_bookmark.owner.eq(user));
    queryFilter.getTags().forEach(tag -> 
      builder.and(_bookmark.tags.any().id.id.eq(tag.getId())));
    queryFilter.getReadStatus().ifPresent(status -> 
      builder.and(_bookmark.readStatus.eq(status)));
    queryFilter.getSharedStatus().ifPresent(status -> 
      builder.and(_bookmark.sharedStatus.eq(status)));
    return builder;
  }
  
  private JPAQuery<Long> createFindAllQuery(User user, BookmarkQueryFilter queryFilter) {    
    return jpaQueryFactory.
      select(_bookmark.id)
      .from(_bookmark)
        .innerJoin(_bookmark.webUrl, _webUrl)
        .innerJoin(_bookmark.owner, _owner)
        .leftJoin(_bookmark.tags, _tag0).on(_tag0.pos.eq(0))
        .leftJoin(_bookmark.tags, _tag1).on(_tag1.pos.eq(1))
        .leftJoin(_bookmark.tags, _tag2).on(_tag2.pos.eq(2))
        .leftJoin(_bookmark.tags, _tag3).on(_tag3.pos.eq(3))
        .leftJoin(_bookmark.tags, _tag4).on(_tag4.pos.eq(4))
      .where(buildFindAllWherePredicate(user, queryFilter));
  }
}
