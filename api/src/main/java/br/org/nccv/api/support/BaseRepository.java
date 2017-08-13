package br.org.nccv.api.support;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Base repository with less option to choose.
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends Repository<T, ID> {

  /**
   * @see CrudRepository#save(Object)
   */
  <S extends T> S save(S entity);

  /**
   * @see CrudRepository#save(Iterable)
   */
  <S extends T> List<S> save(Iterable<S> entities);

  /**
   * Finds one with optional result.
   *
   * @param id Entity identifier
   * @return Optional value
   * @see CrudRepository#findOne(Serializable)
   */
  Optional<T> findOne(ID id);

  /**
   * @see PagingAndSortingRepository#findAll(Pageable)
   */
  Page<T> findAll(Pageable pageable);

  /**
   * @see JpaSpecificationExecutor#findAll(Specification, Pageable)
   */
  Page<T> findAll(Specification<T> spec, Pageable pageable);
}