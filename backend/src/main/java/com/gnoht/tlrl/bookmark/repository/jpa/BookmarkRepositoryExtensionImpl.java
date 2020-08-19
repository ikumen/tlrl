package com.gnoht.tlrl.bookmark.repository.jpa;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.gnoht.tlrl.bookmark.Bookmark;
import com.gnoht.tlrl.bookmark.BookmarkFacets;
import com.gnoht.tlrl.bookmark.BookmarkFacets.TagFacet;
import com.gnoht.tlrl.bookmark.BookmarkResults;
import com.gnoht.tlrl.bookmark.QBookmark;
import com.gnoht.tlrl.bookmark.QTag;
import com.gnoht.tlrl.bookmark.QWebUrl;
import com.gnoht.tlrl.bookmark.ReadStatus;
import com.gnoht.tlrl.bookmark.SharedStatus;
import com.gnoht.tlrl.bookmark.Tag;
import com.gnoht.tlrl.bookmark.WebUrl;
import com.gnoht.tlrl.bookmark.repository.BookmarkQueryFilter;
import com.gnoht.tlrl.user.QUser;
import com.gnoht.tlrl.user.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
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
  public Page<Bookmark> findAll(BookmarkQueryFilter queryFilter, Pageable pageable) {    
    JPAQuery<Long> findAllQuery = createFindAllQuery(queryFilter);
    return doFindAll(findAllQuery, pageable);
  }
  @Override
  public BookmarkResults findAllWithFacets(BookmarkQueryFilter queryFilter, Pageable pageable) {
    JPAQuery<Long> findAllQuery = createFindAllQuery(queryFilter);
    Page<Bookmark> results = doFindAll(findAllQuery.clone(), pageable);
    BookmarkFacets facets = doFindAllFacetsBasedOnResults(queryFilter, findAllQuery, results.getTotalElements()); 
    return new BookmarkResults(results, facets);
  }
   
  @Override
  public BookmarkFacets findAllFacets(BookmarkQueryFilter queryFilter) {
    JPAQuery<Long> findAllQuery = createFindAllQuery(queryFilter);
    return doFindAllFacets(queryFilter, findAllQuery);
  }
  
  private BookmarkFacets doFindAllFacets(BookmarkQueryFilter queryFilter, JPAQuery<Long> baseFindAllQuery) {
    long count = baseFindAllQuery.fetchCount();
    return doFindAllFacetsBasedOnResults(queryFilter, baseFindAllQuery, count);
  }
  
  private BookmarkFacets doFindAllFacetsBasedOnResults(BookmarkQueryFilter queryFilter, JPAQuery<Long> baseFindAllQuery, long count) {
    BookmarkFacets facets = new BookmarkFacets();
    facets.setTags(doFindAllTagFacets(baseFindAllQuery, queryFilter.getTags()));
  
    if (queryFilter.getSharedStatus().isPresent()) {
      facets.getSharedStatuses().put(queryFilter.getSharedStatus().get(), count);
    } else {
      facets.setSharedStatuses(doFindAllSharedStatusFacets(baseFindAllQuery.clone())); // modifies state, make a clone
    }
    
    if (queryFilter.getReadStatus().isPresent()) {
      facets.getReadStatuses().put(queryFilter.getReadStatus().get(), count);
    } else {
      facets.setReadStatuses(doFindAllReadStatusFacets(baseFindAllQuery)); // modifies state as well, but last usage
    }
    return facets;
  }
  
  private Map<SharedStatus, Long> doFindAllSharedStatusFacets(JPAQuery<Long> findAllQuery) {
    return findAllQuery
        .select( _bookmark.sharedStatus, _bookmark.sharedStatus.count())
        .groupBy(_bookmark.sharedStatus)
        .transform(GroupBy.groupBy(_bookmark.sharedStatus)
            .as(_bookmark.sharedStatus.count()));
  }
  
  private Map<ReadStatus, Long> doFindAllReadStatusFacets(JPAQuery<Long> findAllQuery) {
    return findAllQuery
        .select( _bookmark.readStatus, _bookmark.readStatus.count())
        .groupBy(_bookmark.readStatus)
        .transform(GroupBy.groupBy(_bookmark.readStatus)
            .as(_bookmark.readStatus.count()));
  }
  
  private List<TagFacet> doFindAllTagFacets(JPAQuery<Long> findAllQuery, Set<Tag> tagsToExclude) {
    QTag _tag = QTag.tag;
    BooleanBuilder excludeTagPredicate = new BooleanBuilder();
    tagsToExclude.forEach(t -> excludeTagPredicate.and(_tag.id.id.ne(t.getId())));
    
    return jpaQueryFactory
      .select(Projections.constructor(TagFacet.class, 
          Projections.constructor(Tag.class, _tag.id.id), _tag.id.id.count()))
      .from(_tag, _bookmark)
      .where(_bookmark.eq(_tag.bookmark)
          .and(_bookmark.id.in(findAllQuery))
          .and(excludeTagPredicate))      
      .groupBy(_tag.id.id)
      .orderBy(_tag.id.id.count().desc())
      .fetch();
  }
  
  private Page<Bookmark> doFindAll(JPAQuery<Long> baseFindAllQuery, Pageable pageable) {
    JPAQuery<Bookmark> findAllQuery = baseFindAllQuery
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

  private Predicate buildFindAllWherePredicate(BookmarkQueryFilter queryFilter) {
    BooleanBuilder builder = new BooleanBuilder();
    queryFilter.getOwner().ifPresent(u -> builder.and(_bookmark.owner.eq(u)));
    queryFilter.getTags().forEach(tag -> 
      builder.and(_bookmark.tags.any().id.id.eq(tag.getId())));
    queryFilter.getReadStatus().ifPresent(status -> 
      builder.and(_bookmark.readStatus.eq(status)));
    queryFilter.getSharedStatus().ifPresent(status -> 
      builder.and(_bookmark.sharedStatus.eq(status)));
    return builder;
  }
  
  private JPAQuery<Long> createFindAllQuery(BookmarkQueryFilter queryFilter) {    
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
      .where(buildFindAllWherePredicate(queryFilter));
  }
}
