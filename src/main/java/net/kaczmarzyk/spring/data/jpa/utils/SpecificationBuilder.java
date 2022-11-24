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
import net.kaczmarzyk.spring.data.jpa.web.SpecificationArgumentResolver;
import net.kaczmarzyk.spring.data.jpa.web.SpecificationFactory;
import net.kaczmarzyk.spring.data.jpa.web.StandaloneProcessingContext;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecificationBuilder {

	private Class<?> specInterface;

	private Map<String, List<String>> args = new HashMap<>();
	private Map<String, String> pathVars = new HashMap<>();
	private Map<String, List<String>> params = new HashMap<>();
	private Map<String, String> headers = new HashMap<>();

	SpecificationFactory specificationFactory;

	private SpecificationBuilder(Class<?> specInterface) {
		this.specInterface = specInterface;
		this.specificationFactory = new SpecificationFactory();
	}

	public static SpecificationBuilder specification(Class<? extends Specification<?>> specInterface) {
		return new SpecificationBuilder(specInterface);
	}

	public SpecificationBuilder withArg(String arg, List<String> values) {
		this.args.put(arg, values);
		return this;
	}

	public SpecificationBuilder withParams(String param, List<String> values) {
		this.params.put(param, values);
		return this;
	}

	public SpecificationBuilder withPathVar(String pathVar, String value) {
		this.pathVars.put(pathVar, value);
		return this;
	}

	public SpecificationBuilder withHeader(String header, String value) {
		this.headers.put(header, value);
		return this;
	}

	public Specification<?> build() {
		ProcessingContext context = new StandaloneProcessingContext(specInterface, args, pathVars, params, headers);
		return specificationFactory.createSpecificationDependingOn(context);
	}

}
