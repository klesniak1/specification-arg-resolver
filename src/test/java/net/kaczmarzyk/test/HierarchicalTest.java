package net.kaczmarzyk.test;

import net.kaczmarzyk.E2eTestBase;
import net.kaczmarzyk.spring.data.jpa.*;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Join;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HierarchicalTest extends E2eTestBase {

	@Controller
	public static class TestController {

		@Autowired
		BRepository bRepository;

		@Autowired
		CRepository cRepository;

		@RequestMapping(value = "/test", params = { "comment" })
		@ResponseBody
		public Object findByNote(
				@Join(path = "d", alias = "d")
				@Join(path = "d.e", alias = "e")
				@Spec(path = "e.comment", params = {"comment"}, spec = Equal.class) Specification<B> spec) {
			return bRepository.findAll(spec);
		}
	}

	@Test
	public void findsByNote() throws Exception {
		E e = new E("aaa");
		D d = new D(e);
		C c = new C("test", d);
		em.persist(e);
		em.persist(d);
		em.persist(c);
		MvcResult mvcResult = mockMvc.perform(get("/test")
				.param("comment", "aaa")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[?(@.d.e.comment=='aaa')]").exists())
				.andReturn();

	}

}
