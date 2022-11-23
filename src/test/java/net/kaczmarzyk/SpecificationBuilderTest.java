/**
 * Copyright 2014-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.kaczmarzyk;


import net.kaczmarzyk.spring.data.jpa.Customer;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

import static net.kaczmarzyk.spring.data.jpa.utils.SpecificationBuilder.specification;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

public class SpecificationBuilderTest extends E2eTestBase {

	@Or({
			@Spec(path = "firstName", params = "name", spec = LikeIgnoreCase.class),
			@Spec(path = "lastName", params = "name", spec = LikeIgnoreCase.class)
	})
	public interface CustomSpecification extends Specification<Customer> {
	}

	@Test
	public void shouldCreateSpecification() {
		Specification<Customer> spec = (Specification<Customer>) specification(CustomSpecification.class)
				.withArg("name", Arrays.asList("l"))
				.build();

		List<Customer> customers = customerRepo.findAll(spec);

		System.out.println();

		assertThat(customers.size()).isEqualTo(4);
	}


}
