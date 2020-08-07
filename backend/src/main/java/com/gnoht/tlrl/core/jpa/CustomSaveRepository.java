package com.gnoht.tlrl.core.jpa;

/**
 * @author ikumen@gnoht.com
 */
public interface CustomSaveRepository<T, ID> {
  <S extends T> S save(S entity);
}
