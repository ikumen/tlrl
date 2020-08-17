package com.gnoht.tlrl.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * @author ikumen@gnoht.com
 */
public interface Repository<T, ID> {

  /**
   * Save the given entity.
   * @param entity
   * @param <S>
   * @return
   */
  <S extends T> S save(S entity);

  /**
   * Delete the given entity.
   * @param entity
   */
  void delete(T entity);

  /**
   * Return an entity with the given id.
   * @param id
   * @return
   */
  Optional<T> findById(ID id);

  /**
   * Returns all entities managed by this repository.
   * @param pageable
   * @return
   */
  Page<T> findAll(Pageable pageable);

  /**
   * Returns the number of entities managed by this repository.
   * @return
   */
  long count();
  
}