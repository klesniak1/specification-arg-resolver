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
package net.kaczmarzyk.spring.data.jpa.web;

import net.kaczmarzyk.spring.data.jpa.domain.In;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Kacper Leśniak
 */
public class StandaloneProcessingContextTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private StandaloneProcessingContext context;

	@Spec(path = "firstName", pathVars = "name", spec = In.class)
	public interface TestInterface extends Specification<Object> {}

	@Before
	public void setupContext() {
		Map<String, String[]> args = new HashMap<>();
		Map<String, String[]> params = new HashMap<>();
		Map<String, String> pathVars = new HashMap<>();
		Map<String, String> headers = new HashMap<>();

		args.put("name", new String[]{"example"});
		params.put("xxx", new String[]{"example"});
		pathVars.put("yyy", "test");

		context = new StandaloneProcessingContext(
				TestInterface.class,
				args,
				pathVars,
				params,
				headers);
	}

	@Test
	public void throwsExceptionIfPathVariableDoesntExist() {
		thrown.expect(InvalidPathVariableRequestedException.class);
		thrown.expectMessage("Requested path variable {notExisting} is not present in Controller request mapping annotations");

		context.getPathVariableValue("notExisting");
	}

	@Test
	public void shouldSearchValueInFallbackMapWhenValueIsNotPresentInArgumentSpecificMap() {
		String fallbackValueForPathVariable = context.getPathVariableValue("name");
		String[] fallbackValueForParams = context.getParameterValues("name");
		String fallbackValueForHeader = context.getRequestHeaderValue("name");

		assertThat(fallbackValueForPathVariable).isEqualTo("example");
		assertThat(fallbackValueForParams).isEqualTo(new String[]{"example"});
		assertThat(fallbackValueForHeader).isEqualTo("example");
	}

}