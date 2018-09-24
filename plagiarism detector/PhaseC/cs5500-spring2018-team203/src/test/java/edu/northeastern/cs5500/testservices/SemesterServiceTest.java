package edu.northeastern.cs5500.testservices;

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import edu.northeastern.cs5500.Cs5500Spring2018Team203Application;
import edu.northeastern.cs5500.service.SemesterService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes=Cs5500Spring2018Team203Application.class)
@DataJpaTest

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class SemesterServiceTest {

	private MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new SemesterService()).build();
	
	
	@Test
    public void testGetAllSemester() throws Exception {
		
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/getAllSemester").accept(
				MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		System.out.println("get all courses"+result.getResponse().getContentAsString());
		assertEquals(true,result.getResponse().getContentAsString().contains("Spring 2018"));
		
	
	}
	
	
}
