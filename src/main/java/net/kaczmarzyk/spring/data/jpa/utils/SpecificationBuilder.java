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
package net.kaczmarzyk.spring.data.jpa.utils;

import net.kaczmarzyk.spring.data.jpa.web.ProcessingContext;
import net.kaczmarzyk.spring.data.jpa.web.SpecificationFactory;
import net.kaczmarzyk.spring.data.jpa.web.StandaloneProcessingContext;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kacper Leśniak
 */
public class SpecificationBuilder<T extends Specification> {

	private static SpecificationFactory DEFAULT_SPECIFICATION_FACTORY = new SpecificationFactory(null, null);

	private Class<T> specInterface;

	private Map<String, String[]> args = new HashMap<>();
	private Map<String, String> pathVars = new HashMap<>();
	private Map<String, String[]> params = new HashMap<>();
	private Map<String, String> headers = new HashMap<>();

	private SpecificationBuilder(Class<T> specInterface) {
		this.specInterface = specInterface;
	}

	public static <T extends Specification<?>> SpecificationBuilder<T> specification(Class<T> specInterface) {
		return new SpecificationBuilder<T>(specInterface);
	}

	/**
	 * Method is not recommended to use, it can provide to specific behaviour which might not be expected.
	 * e.g. It will take only first value of passed array of values when defined specification is set to take them from pathVariable or header.
	 * It's caused by assumptions of SAR, that single pathVariable or header can consist single value.
	 * Recommended way is to use same argument type as defined in specification.
	 */
	public SpecificationBuilder<T> withArg(String arg, String... values) {
		this.args.put(arg, values);
		return this;
	}

	public SpecificationBuilder<T> withParam(String param, String... values) {
		this.params.put(param, values);
		return this;
	}

	public SpecificationBuilder<T> withPathVar(String pathVar, String value) {
		this.pathVars.put(pathVar, value);
		return this;
	}

	public SpecificationBuilder<T> withHeader(String header, String value) {
		this.headers.put(header, value);
		return this;
	}

	public T build() {
		ProcessingContext context = createContext();
		return (T) DEFAULT_SPECIFICATION_FACTORY.createSpecificationDependingOn(context);
	}

	private ProcessingContext createContext() {
		return new StandaloneProcessingContext(specInterface, args, pathVars, params, headers);
	}

}
