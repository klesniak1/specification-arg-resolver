package net.kaczmarzyk.spring.data.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BRepository extends PagingAndSortingRepository<B, Long>, JpaSpecificationExecutor<B> {
}
