package net.kaczmarzyk.spring.data.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ERepository extends PagingAndSortingRepository<E, Long>, JpaSpecificationExecutor<E> {
}
