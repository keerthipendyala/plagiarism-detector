package edu.northeastern.cs5500.testservices;

import edu.northeastern.cs5500.Cs5500Spring2018Team203Application;
import edu.northeastern.cs5500.daos.SubmissionDao;
import edu.northeastern.cs5500.models.Submission;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runners.MethodSorters;

import edu.northeastern.cs5500.service.SubmissionService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes=Cs5500Spring2018Team203Application.class)
@DataJpaTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SubmissionServiceTest {
	
	 public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	        return mapper.writeValueAsBytes(object);
	    }
	
	 
	private MockMvc mockMvc= MockMvcBuilders.standaloneSetup(new SubmissionService()).build();
	
	
	@Test
	public void testBAllSubmissionHistory() throws Exception {
		
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/getAllStudentSubmissions/"+1+"/"+200).accept(
				MediaType.APPLICATION_JSON);
		MvcResult result;
		
			
			
			result = mockMvc.perform(requestBuilder).andReturn();
			System.out.println(result.getResponse().getContentAsString());
			assertEquals(true,result.getResponse().getContentAsString().contains("testsubmission"));
			assertEquals(true, result.getResponse().getContentAsString().contains("testname"));
		 
		
	}
	
	@Test
	public void testASubmitAssignment() throws IOException, Exception {
		
		Submission s = new Submission(100,26,"testsubmission",1,"testlink","testdesc",200);
		s.setstudentname("testname");
			mockMvc.perform(post("/api/submitAssignment")
					.contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
							MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8")))
					.content(SubmissionServiceTest.convertObjectToJsonBytes(s)))
	               .andExpect(status().isOk());
		
		
	}
	
	
	@Test
	public void testCSubmissionScore() throws Exception {
		
		/**
		 * Please while clearing the database dont delete the score with id 151 
		 */
		
		Submission s = new Submission(969,26,"testsubmission",1,"testlink","testdesc",200);
		Submission [] sarr=new Submission[1];
		sarr[0]=s;
		 mockMvc.perform(post("/api/getSubmissionScores")
					.contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
							MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8")))
					.content(SubmissionServiceTest.convertObjectToJsonBytes(sarr)))
	               .andExpect(status().isOk());
					
		
	}
	
	
	@Test
	public void testDAllSubmissions() throws Exception {
		
		/**
		 * Please while clearing the database dont delete the score with id 151 
		 */
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/getAllSubmission/"+31+"/"+3+"/"+200).accept(
				MediaType.APPLICATION_JSON);
		
		 mockMvc.perform(requestBuilder).andExpect(status().isOk())
					.andExpect(jsonPath("$[0].courseid",is(3)));
				
	}
	
	@Test
	public void testEGetSemSubmissions() throws IOException, Exception{
		
		int[] semids=new int[2];
		semids[0]=1;
		semids[1]=2;
		
		mockMvc.perform(post("/api/semesterSubmissions/1/0/CS5200/false")
				.contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
						MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8")))
				.content(SubmissionServiceTest.convertObjectToJsonBytes(semids)))
               .andExpect(status().isOk());
		
		mockMvc.perform(post("/api/semesterSubmissions/1/3/CS5200/true")
				.contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
						MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8")))
				.content(SubmissionServiceTest.convertObjectToJsonBytes(semids)))
               .andExpect(status().isOk());
	}
	
	
	
	@AfterClass
	public static void testTeardown(){
		
		SubmissionDao subdao=SubmissionDao.getInstance();
		subdao.removeSubmission(26, 1, 200);
		
	}
	
}
	
	


