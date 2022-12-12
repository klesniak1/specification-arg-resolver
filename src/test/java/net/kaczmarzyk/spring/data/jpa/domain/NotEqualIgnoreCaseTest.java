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
package net.kaczmarzyk.spring.data.jpa.domain;

import com.jparams.verifier.tostring.ToStringVerifier;
import net.kaczmarzyk.spring.data.jpa.Customer;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.text.ParseException;

/**
 * @author Mateusz Fedkowicz
 **/
public class NotEqualIgnoreCaseTest extends NotEqualTest {

	@Test
	public void filtersByStringCaseInsensitive() {
		NotEqualIgnoreCase<Customer> notSimpson = notEqualIgnoreCaseSpec("lastName", "SIMpsOn");
		assertFilterMembers(notSimpson, joeQuimby);

		NotEqualIgnoreCase<Customer> notHomer = notEqualIgnoreCaseSpec("firstName", "HoMeR");
		assertFilterMembers(notHomer, margeSimpson, joeQuimby);
	}

	@Test
	public void filtersByEnumCaseInsensitive() {
		NotEqualIgnoreCase<Customer> notMale = notEqualIgnoreCaseSpec("gender", "maLe");
		assertFilterMembers(notMale, margeSimpson);

		NotEqualIgnoreCase<Customer> notFemale = notEqualIgnoreCaseSpec("gender", "fEmALE");
		assertFilterMembers(notFemale, homerSimpson);
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsMissingArgument() throws ParseException {
		new NotEqualIgnoreCase<>(queryCtx, "path", new String[] {}, defaultConverter);
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsTooManyArguments() throws ParseException {
		new NotEqualIgnoreCase<>(queryCtx, "path", new String[] { "2014-03-10", "2014-03-11", "2014-03-11" }, defaultConverter);
	}

	@Test
	public void equalsAndHashCodeContract() {
		EqualsVerifier.forClass(NotEqualIgnoreCase.class)
				.usingGetClass()
				.suppress(Warning.NONFINAL_FIELDS)
				.verify();
	}

	@Test
	public void toStringVerifier() {
		ToStringVerifier.forClass(NotEqualIgnoreCase.class)
				.withIgnoredFields("path", "queryContext")
				.verify();
	}

	private <T> NotEqualIgnoreCase<T> notEqualIgnoreCaseSpec(String path, Object expectedValue) {
		return new NotEqualIgnoreCase<>(queryCtx, path, new String[]{expectedValue.toString()}, defaultConverter);
	}

}
