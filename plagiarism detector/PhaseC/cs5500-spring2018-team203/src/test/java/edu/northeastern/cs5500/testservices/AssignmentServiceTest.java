package edu.northeastern.cs5500.testservices;

import edu.northeastern.cs5500.Cs5500Spring2018Team203Application;
import edu.northeastern.cs5500.daos.AssignmentDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.junit.AfterClass;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;

import edu.northeastern.cs5500.models.Assignment;
import edu.northeastern.cs5500.service.AssignmentService;

/**
 * this Class is used to the Assignment Service methods 
 * @author harip
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes=Cs5500Spring2018Team203Application.class)
@DataJpaTest
public class AssignmentServiceTest {
	
	private static MockMvc mockMvc= MockMvcBuilders.standaloneSetup(new AssignmentService()).build();
	private static int assignmentid=0; 
	private static int courseid=0;
	
	/**
	 * this method converts the object to JsonBytes 
	 * @param object
	 * @return the JsonBytes
	 * @throws IOException
	 */
	public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
	 }
	
	/**
	 * this method test if all the assignments are retrieved properly
	 * @throws Exception
	 */
	@Test
	public void testgetAllAssignments() throws Exception {

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/getAllAssignments/6").accept(
				MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(true,result.getResponse().getContentAsString().contains("assg1-racket"));
	}

	/**
	 * this method test if the assignments are getting created in the database
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void testAcreateAssignment() throws IOException, Exception {

		Assignment a=new Assignment(0, "testassg", 1, "testassg");

		mockMvc.perform(post("/api/createAssignment/").contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
						MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8")))
				.content(AssignmentServiceTest.convertObjectToJsonBytes(a)))
                .andExpect(status().isOk());
	}
	
	/**
	 * this method test if the assignments are updated in the backend
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void testBUpdateAssignment() throws IOException, Exception {
		
		AssignmentDao ad=AssignmentDao.getInstance();
		int aid=0;
		List<Assignment> assgs=ad.getAllAssignments(1);
		for(Assignment a: assgs) {
			if(a.getAssignmentname().equalsIgnoreCase("testassg")){
				aid=a.getId();
				assignmentid=a.getId();
				courseid=a.getCourseid();
				break;
			}
		}
		
		Assignment a=new Assignment(aid, "testassg", 1, "test");
		
		mockMvc.perform(post("/api/updateAssignment/").contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
				MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8")))
		.content(AssignmentServiceTest.convertObjectToJsonBytes(a)))
        .andExpect(status().isOk());
		
		mockMvc.perform(get("/api/getAssignment/"+aid).contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
				MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"))))
        .andExpect(status().isOk());
		
		
	}
	
	
	/**
	 * this method resets the variable that are set in the testcases
	 * @throws Exception
	 */
	@AfterClass
	public static void doYourOneTimeTeardown() throws Exception {
		mockMvc.perform(delete("/api/deleteAssignment/"+assignmentid).contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
				MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"))))
        .andExpect(status().isOk());
	}

}
