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
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.In;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Join;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

import static net.kaczmarzyk.spring.data.jpa.utils.SpecificationBuilder.specification;
import static org.assertj.core.api.Assertions.assertThat;

public class SpecificationBuilderTest extends E2eTestBase {

	@Join(path = "orders", alias = "o")
	@Spec(path = "o.itemName", params = "orderIn", spec = In.class)
	public interface CustomSpecification extends Specification<Customer> {
	}

	@Test
	public void shouldCreateSpecification() {
		Specification<?> spec = specification(CustomSpecification.class)
				.withParams("orderIn", Arrays.asList("Pizza"))
				.build();

		List<Customer> customers = customerRepo.findAll((Specification<Customer>) spec);

		assertThat(customers.size()).isEqualTo(1);
	}
}
