package net.kaczmarzyk;

import net.kaczmarzyk.spring.data.jpa.A;
import net.kaczmarzyk.spring.data.jpa.ARepository;
import net.kaczmarzyk.spring.data.jpa.B;
import net.kaczmarzyk.spring.data.jpa.domain.NotNull;
import net.kaczmarzyk.spring.data.jpa.domain.Null;
import net.kaczmarzyk.spring.data.jpa.BRepository;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Join;
import net.kaczmarzyk.spring.data.jpa.web.annotation.OnTypeMismatch;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class TestClass extends E2eTestBase {

	@Autowired
	ARepository aRepository;

	@Autowired
	BRepository bRepository;

	@Join(path = "b", alias = "bb")
	@Spec(path = "bb.id", params = "bIsNull", spec = Null.class)
	public interface CustomSpec extends Specification<A> {}

	@Controller
	static class TestController {
		@Autowired
		ARepository aRepository;

		@RequestMapping(value = "/test", params = { "bIsNull" })
		@ResponseBody
		public Object findByNameAndStreet(CustomSpec spec) {

			Iterable<A> all = aRepository.findAll();
			List<A> as = aRepository.findAll(spec);
			return as;
		}
	}

	@Test
	public void findsNullAValues() throws Exception {
		aRepository.save(new A(null));
		B b = bRepository.save(new B());
		aRepository.save(new A(b));

		MvcResult mvcResult = mockMvc.perform(get("/test")
						.param("bIsNull", "true")
						.accept(MediaType.APPLICATION_JSON))
				.andReturn();


		System.out.println();
	}
}
