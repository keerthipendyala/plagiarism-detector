package edu.northeastern.cs5500.testservices;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.AfterClass;
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

import edu.northeastern.cs5500.Cs5500Spring2018Team203Application;
import edu.northeastern.cs5500.daos.UserDao;
import edu.northeastern.cs5500.models.User;
import edu.northeastern.cs5500.service.AdminService;
import edu.northeastern.cs5500.service.UserService;

/**
 * This class has methods that test the AdminService functionality
 * @author harip
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes=Cs5500Spring2018Team203Application.class)
@DataJpaTest
public class AdminServiceTest {

	private MockMvc mockMvc= MockMvcBuilders.standaloneSetup(new AdminService()).build();

	/**
	 * this method test the if the professor can make registration request
	 * @throws Exception
	 */
	@Test
	public void testARequestProfessorRegistration() throws Exception {

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/getAllUsers").accept(
				MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(true,result.getResponse().getContentAsString().contains("ryan"));

	}
	
	/**
	 * this method test if the admin can change role for the users
	 * @throws Exception
	 */
	@Test
	public void testBchangeRole() throws Exception {
		User u=new User();
		UserDao ud = UserDao.getInstance();
		u.setUsername("testadservice");
		u.setPassword("pwd");
		u.setFullName("fname");
		u.setRole("student");
		UserService uService = new UserService();
		uService.createUser(u);
		
		// null - when no user is initialized
				mockMvc.perform(post("/api/changeRole/"+ u.getUserid() +"/student").contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
						MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"))))
		       .andExpect(status().isOk());
		
		u = ud.findUserByUserName("testadservice");
		System.out.println(u.getUserid());
		System.out.println(u.getUsername());
		
		// change to existing role - no action will be performed
		mockMvc.perform(post("/api/changeRole/"+ u.getUserid() +"/student").contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
				MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"))))
       .andExpect(status().isOk());

		// change to new role
		mockMvc.perform(post("/api/changeRole/"+ u.getUserid() +"/professor").contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
				MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"))))
       .andExpect(status().isOk());
	
		uService.deleteUser(u.getUsername());
	}


}
