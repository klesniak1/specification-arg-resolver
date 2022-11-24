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

import net.kaczmarzyk.spring.data.jpa.utils.QueryContext;

import javax.management.Query;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

public class DefaultProcessingContext implements ProcessingContext {

	private Class<?> specInterface;

	private Map<String, List<String>> args;
	private Map<String, String> pathVariableArgs;
	private Map<String, List<String>> parameterArgs;
	private Map<String, String> headerArgs;

	private QueryContext queryContext;

	public DefaultProcessingContext(Class<?> specInterface, Map<String, List<String>> args, Map<String, String> pathVariableArgs, Map<String, List<String>> parameterArgs, Map<String, String> headerArgs) {
		this.specInterface = specInterface;
		this.args = args;
		this.pathVariableArgs = pathVariableArgs;
		this.parameterArgs = parameterArgs;
		this.headerArgs = headerArgs;
		this.queryContext = new DefaultQueryContext();
	}

	@Override
	public Class<?> getParameterType() {
		return specInterface;
	}

	@Override
	public Annotation[] getParameterAnnotations() {
		return specInterface.getAnnotations();
	}

	@Override
	public QueryContext queryContext() {
		if (isNull(queryContext)) {
			this.queryContext = new DefaultQueryContext();
		}
		return queryContext;
	}

	@Override
	public String getRequestHeaderValue(String headerKey) {
		return headerArgs.getOrDefault(headerKey, args.get(headerKey).get(0));
	}

	@Override
	public String[] getParameterValues(String webParamName) {
		List<String> list = parameterArgs.getOrDefault(webParamName, args.get(webParamName));
		return list.toArray(new String[]{});
	}

	@Override
	public String getPathVariableValue(String pathVariableName) {
		return pathVariableArgs.getOrDefault(pathVariableName, args.get(pathVariableName).get(0));
	}
}
