/**
 * 
 */
package com.gnoht.tlrl.support.jpa;

import com.gnoht.tlrl.support.Specification;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

/**
 * @author ikumen
 */
public class QuerydslSpecification implements Specification<Predicate> {

  private final BooleanBuilder builder;
  
  public QuerydslSpecification() {
    builder = new BooleanBuilder();
  }

  public QuerydslSpecification(Predicate predicate) {
    builder = new BooleanBuilder(predicate);
  }

  @Override
  public Specification<Predicate> and(Predicate predicate) {
    builder.and(predicate);
    return this;
  }

  @Override
  public Specification<Predicate> or(Predicate predicate) {
    builder.or(predicate);
    return this;
  }

  @Override
  public Predicate toPredicate() {
    return builder.getValue();
  }
}
