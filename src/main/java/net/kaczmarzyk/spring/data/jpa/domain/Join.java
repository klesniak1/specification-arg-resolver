/**
 * Copyright 2014-2023 the original author or authors.
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
package net.kaczmarzyk.spring.data.jpa.domain;

import static net.kaczmarzyk.spring.data.jpa.utils.JoinPathUtils.pathToJoinContainsAlias;
import static net.kaczmarzyk.spring.data.jpa.utils.JoinPathUtils.pathToJoinSplittedByDot;

import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import net.kaczmarzyk.spring.data.jpa.utils.QueryContext;

/**
 * @author Tomasz Kaczmarzyk
 * @author Jakub Radlica
 */
public class Join<T> implements Specification<T>, Fake {

	private static final long serialVersionUID = 1L;

	private String pathToJoinOn;
	private String alias;
	private JoinType joinType;
	private QueryContext queryContext;
	private boolean distinctQuery;


	public Join(QueryContext queryContext, String pathToJoinOn, String alias, JoinType joinType, boolean distinctQuery) {
		this.pathToJoinOn = pathToJoinOn;
		this.alias = alias;
		this.joinType = joinType;
		this.queryContext = queryContext;
		this.distinctQuery = distinctQuery;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		query.distinct(distinctQuery);

		if (!pathToJoinContainsAlias(pathToJoinOn)) {
                        if(!queryContext.existsJoin(alias, root)) {
                            queryContext.putLazyVal(alias, (r) -> r.join(pathToJoinOn, joinType));
                        }
		} else {
			String[] pathToJoinOnSplittedByDot = pathToJoinSplittedByDot(pathToJoinOn);

			String extractedAlias = pathToJoinOnSplittedByDot[0];
			
			if (!queryContext.existsJoin(extractedAlias, root)) {
				throw new IllegalArgumentException(
						"Join definition with alias: '" + extractedAlias + "' not found! " +
								"Make sure that join with the alias '" + extractedAlias +"' is defined before the join with path: '" + pathToJoinOn + "'"
				);
			}

			String extractedPathToJoin = pathToJoinOnSplittedByDot[1];
                queryContext.putLazyVal(
                        alias,
                        (r) -> {
                        	javax.persistence.criteria.Join<?, ?> evaluated = queryContext.getEvaluated(extractedAlias, root);
                        	return evaluated.join(extractedPathToJoin, joinType);
                        }
                );
		}
		return null;
	}

	@Override
	public int hashCode() {
		return Objects.hash(alias, distinctQuery, joinType, pathToJoinOn, queryContext);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Join other = (Join) obj;
		return Objects.equals(alias, other.alias) && distinctQuery == other.distinctQuery && joinType == other.joinType
				&& Objects.equals(pathToJoinOn, other.pathToJoinOn) && Objects.equals(queryContext, other.queryContext);
	}

	@Override
	public String toString() {
		return "Join [pathToJoinOn=" + pathToJoinOn + ", alias=" + alias + ", joinType=" + joinType + ", queryContext=" + queryContext
				+ ", distinctQuery=" + distinctQuery + "]";
	}
}
