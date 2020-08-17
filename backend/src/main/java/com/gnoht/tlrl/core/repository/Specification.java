package com.gnoht.tlrl.core.repository;

/**
 * @author ikumen@gnoht.com
 */
public interface Specification<T> {

  Specification<T> and(T predicate);
  Specification<T> or(T predicate);
  
  default Specification<T> and(Specification<T> spec) {
    return and(spec.toPredicate());
  }
  
  default Specification<T> or(Specification<T> spec) {
    return or(spec.toPredicate());
  }
  
  T toPredicate();
  
  interface Builder<T> {
    Specification<T> build();
  }

}
