package net.kaczmarzyk.spring.data.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CRepository extends PagingAndSortingRepository<C, Long>, JpaSpecificationExecutor<C> {
}
