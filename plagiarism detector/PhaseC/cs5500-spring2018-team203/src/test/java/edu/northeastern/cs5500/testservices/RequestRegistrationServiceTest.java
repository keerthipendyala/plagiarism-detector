package edu.northeastern.cs5500.testservices;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;


import edu.northeastern.cs5500.Cs5500Spring2018Team203Application;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.northeastern.cs5500.daos.PendingTableDao;
import edu.northeastern.cs5500.daos.UserDao;
import edu.northeastern.cs5500.models.User;
import edu.northeastern.cs5500.service.RequestRegistrationService;
import org.junit.runners.MethodSorters;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes=Cs5500Spring2018Team203Application.class)
@DataJpaTest

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RequestRegistrationServiceTest {
	
	private static UserDao udao;
	@Before
	public void setup() {
		
			udao=UserDao.getInstance();
		
	}
	
	  public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	        return mapper.writeValueAsBytes(object);
	    }
	
	
	 private MockMvc mockMvc= MockMvcBuilders.standaloneSetup(new RequestRegistrationService()).build();

	@Test
	public void testARequestProfessorRegistration() throws Exception {

		User u=new User();
		u.setUsername("testprofxavier");
		u.setRole("professor");
		u.setFullName("Professor Xavier xmen");
			mockMvc.perform(post("/api/requestProfessorRegistration")
				.contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
						MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8")))
				.content(RequestRegistrationServiceTest.convertObjectToJsonBytes(u)))
               .andExpect(status().isOk());

	}
	
	
	@Test
	public void testBAcceptRequest() throws Exception {

		int userid= PendingTableDao.getInstance()
				.getUserFromPendingTable("testprofxavier")
				.getUserid();
		mockMvc.perform(get("/api/acceptRequest/"+userid)
				.contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
						MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8")))).andExpect(status().isOk());

	}
	

	@Test
	public void testCRejectRequest() throws Exception {

		int userid= PendingTableDao.getInstance()
				.getUserFromPendingTable("testprofxavier")
				.getUserid();
		mockMvc.perform(get("/api/rejectRequest/"+userid)
				.contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
						MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8")))).andExpect(status().isOk());

	}
	
	@AfterClass
	public static void afterTeardown() {
		
		udao.deleteUser("testprofxavier");
		
	}


}
