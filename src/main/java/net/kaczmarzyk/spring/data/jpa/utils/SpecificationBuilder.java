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

import net.kaczmarzyk.spring.data.jpa.web.SpecificationArgumentResolver;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecificationBuilder {

	private Class<?> classType;

	private Map<String, List<String>> args;
	private Map<String, String> pathVariableArgs;
	private Map<String, List<String>> parameterArgs;
	private Map<String, String> headerArgs;

	SpecificationArgumentResolver specificationArgumentResolver = new SpecificationArgumentResolver();

	private SpecificationBuilder(Class<?> classType) {
		this.classType = classType;
		this.args = new HashMap<>();
		this.pathVariableArgs = new HashMap<>();
		this.parameterArgs = new HashMap<>();
		this.headerArgs = new HashMap<>();
	}

	public static SpecificationBuilder specification(Class<? extends Specification<?>> spec) {
		return new SpecificationBuilder(spec);
	}

	public SpecificationBuilder withArg(String arg, List<String> values) {
		this.args.put(arg, values);
		return this;
	}

	public SpecificationBuilder withPathVariableArg(String arg, String value) {
		this.pathVariableArgs.put(arg, value);
		return this;
	}

	public SpecificationBuilder withParameterArg(String arg, List<String> values) {
		this.parameterArgs.put(arg, values);
		return this;
	}

	public SpecificationBuilder withHeaderArg(String arg, String value) {
		this.headerArgs.put(arg, value);
		return this;
	}

	public Object build() {
		return specificationArgumentResolver.resolveArgument(classType, args, pathVariableArgs, parameterArgs, headerArgs);
	}

}
