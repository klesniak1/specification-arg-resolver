package net.kaczmarzyk.spring.data.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ARepository extends PagingAndSortingRepository<A, Long>, JpaSpecificationExecutor<A> {
}
