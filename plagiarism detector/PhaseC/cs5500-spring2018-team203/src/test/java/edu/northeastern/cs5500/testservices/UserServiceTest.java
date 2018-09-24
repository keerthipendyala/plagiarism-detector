package edu.northeastern.cs5500.testservices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;

import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;

import edu.northeastern.cs5500.Cs5500Spring2018Team203Application;
import edu.northeastern.cs5500.daos.EmailDao;
import edu.northeastern.cs5500.daos.UserDao;
import edu.northeastern.cs5500.models.Professor;
import edu.northeastern.cs5500.models.Student;
import edu.northeastern.cs5500.models.User;
import edu.northeastern.cs5500.service.UserService;



/**
 * this Class is used to UserService methods 
 * @author harip
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes=Cs5500Spring2018Team203Application.class)
@DataJpaTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTest {
	
	private MockMvc mockMvc= MockMvcBuilders.standaloneSetup(new UserService()).build();
	
	/**
	 * JsonString method to retrieve json as string 
	 * @param obj
	 * @return content of Json
	 */
	public static String asJsonString(final Object obj) {
	    try {
	        final ObjectMapper mapper = new ObjectMapper();
	        final String jsonContent = mapper.writeValueAsString(obj);
	        return jsonContent;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}  
	
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
	 * test user by the unique userid
	 * @throws Exception
	 */
	@Test
	public void testfindUserById() throws Exception {

		mockMvc.perform(get("/api/findUser/"+610).contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
						MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"))))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username", is("testme")))
               .andExpect(jsonPath("$.userid", is(610)));
	}

	/**
	 * test user by the unique username and password
	 * @throws Exception
	 */
	@Test
	public void testfindUserUsernameAndPassword() throws Exception {
		
		MockHttpServletRequest req=new MockHttpServletRequest();
		User usr = new User(726,"teststd1", "tt", "teststd1");
		UriComponents ucomponent = ServletUriComponentsBuilder
				.fromContextPath(req).port(8080)
				.path("/api/login")
				.build();
		URI uri=ucomponent.toUri();
		
		MockMvcRequestBuilders.post(uri.toString(), usr);
		mockMvc.perform(post("/api/login/").contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
						MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8")))
			   .content(UserServiceTest.convertObjectToJsonBytes(usr)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username", is("teststd1")));
	}
	/*
	
	/**
	 * This method will contain test that will find user in database by Username
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testfindUserByUsername() throws Exception {
		  
			mockMvc.perform(get("/api/findUsername/"+"testme").contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
					MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"))))
	       .andExpect(status().isOk())
	       .andExpect(jsonPath("$.fullname", is("testmefully")))
	       .andExpect(jsonPath("$.userid", is(610)));
	}
	
	/**
	 * this method test the professors retrieved from the database
	 * @throws Exception
	 */
	@Test
	public void testgetAllProfessors() throws Exception{
	
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/getAllProfessors").accept(
				MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(true,result.getResponse().getContentAsString().contains("ryan"));
	}
	
	
	/**
	 * this method test the students retrieved from the database
	 * @throws Exception
	 */
	@Test
	public void testgetAllStudents() throws Exception{
		
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/getAllStudents").accept(
				MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(true,result.getResponse().getContentAsString().contains("teststd1"));
	}
	

	/**
	 * This test will find the created User 
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testCreateUser() throws Exception{
		MockHttpServletRequest req=new MockHttpServletRequest();
		User u=new User();
		u.setUsername("dummyname");
		u.setPassword("dummypwd");
		u.setFullName("dummyfname");
		u.setEmail("dummyemail");
		UriComponents ucomponent = ServletUriComponentsBuilder
				.fromContextPath(req).port(8080)
				.path("/api/register")
				.build();
		URI uri=ucomponent.toUri();
		
		MockMvcRequestBuilders.post(uri.toString(), u);
		mockMvc.perform(post("/api/register/").contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
						MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8")))
				.content(UserServiceTest.convertObjectToJsonBytes(u)))
               .andExpect(status().isOk());
		
		
		UserDao dao = UserDao.getInstance();
		User u2 = dao.findUserByUserName("dummyname");
		EmailDao edao=EmailDao.getInstance();
		
		edao.getEmailFromUserId(u2.getUserid());
		edao.getEmailFromUserId(123445);
		
		assertThat(u2.getEmail()).isEqualTo("dummyemail");
		assertThat(u2.getFullname()).isEqualTo("dummyfname");
	}

	/**
	 * this method test the user by the unique id and the role
	 * @throws Exception
	 */
	@Test
	public void testfindUserByIdAndRole() throws Exception{

		mockMvc.perform(get("/api/findUserByIdAndRole/"+"610/student").contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
				MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"))))
       .andExpect(status().isOk())
       .andExpect(jsonPath("$.fullname", is("testmefully")))
       .andExpect(jsonPath("$.userid", is(610)))
       .andExpect(jsonPath("$.role", is("student")));
	}
	
	/**
	 * this method test if the student details are getting updated 
	 * @throws Exception
	 */
	@Test
	public void testupdateStudent() throws Exception{
		MockHttpServletRequest req=new MockHttpServletRequest();
		UserDao dao = UserDao.getInstance();
		Student std = (Student) dao.findUserByIdAndRole(610, "student");
		System.out.println();
		std.setUsername("testmeupdate");
		std.setFullName("testmefullyupdate");
		UriComponents ucomponent = ServletUriComponentsBuilder
				.fromContextPath(req).port(8080)
				.path("/api/updateStudent")
				.build();
		URI uri=ucomponent.toUri();
		
		MockMvcRequestBuilders.post(uri.toString(), std);
		mockMvc.perform(post("/api/updateStudent/"+std.getStudentid()).contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
						MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8")))
				.content(UserServiceTest.convertObjectToJsonBytes(std)))
               .andExpect(status().isOk());
		
		User u2 = dao.findUserByUserName("testmeupdate");
		assertThat(u2.getFullname()).isEqualTo(std.getFullname());
	}
	
	/**
	 * this method test if the professor details are getting updated
	 * @throws Exception
	 */
	@Test
	public void testupdateProfessor() throws Exception{
		MockHttpServletRequest req=new MockHttpServletRequest();
		UserDao dao = UserDao.getInstance();
		Professor prf = (Professor) dao.findUserByIdAndRole(612,"professor");

		prf.setUsername("clingerupdate");
		prf.setFullName("clingerfnameupdate");
		UriComponents ucomponent = ServletUriComponentsBuilder
				.fromContextPath(req).port(8080)
				.path("/api/updateProfessor")
				.build();
		URI uri=ucomponent.toUri();
		
		MockMvcRequestBuilders.post(uri.toString(), prf);
		mockMvc.perform(post("/api/updateProfessor/"+prf.getProfessorid()).contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
						MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8")))
				.content(UserServiceTest.convertObjectToJsonBytes(prf)))
               .andExpect(status().isOk());
		
		User u2 = dao.findUserByUserName("clingerupdate");
		assertThat(u2.getFullname()).isEqualTo(prf.getFullname());
	}
	
	/**
	 * this method test if the Admin details are getting updated
	 * @throws Exception
	 */
	@Test
	public void testupdateAdmin() throws Exception{
		MockHttpServletRequest req=new MockHttpServletRequest();
		UserDao dao = UserDao.getInstance();
		User u = dao.findUserByUserName("testadmin");
		u.setUsername("testadminupdate");
		u.setFullName("testadminfnameupdate");
		UriComponents ucomponent = ServletUriComponentsBuilder
				.fromContextPath(req).port(8080)
				.path("/api/updateAdmin")
				.build();
		URI uri=ucomponent.toUri();
		
		MockMvcRequestBuilders.post(uri.toString(), u);
		mockMvc.perform(post("/api/updateAdmin/"+u.getUserid()).contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
						MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8")))
				.content(UserServiceTest.convertObjectToJsonBytes(u)))
               .andExpect(status().isOk());
		
		User u2 = dao.findUserByUserName("testadminupdate");
		assertThat(u2.getFullname()).isEqualTo(u.getFullname());
	}
	
	/**
	 * this method test if the email is sent properly
	 * @throws Exception
	 */
	@Test 
	public void testSendEmail() throws Exception {
		String a="world@gmail.com";
		int i=2446;
		mockMvc.perform(get("/api/sendemail/"+a+"/"+a+"/"+i).contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
				MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"))))
       .andExpect(status().isOk());
		
	}
	
	
	

	/**
	 * this method test if the user is getting deleted in the database
	 * @throws Exception
	 */
	@Test
	public void testdeleteUser() throws Exception {		
		
		mockMvc.perform(post("/api/deleteUser/"+"dummyname").contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
						MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"))))
               .andExpect(status().isOk());
		
	}
	
	
	
	
	/**
	 * this method reset the variables which are changed by the testcases
	 * @throws Exception
	 */
	@AfterClass
	public static void doYourOneTimeTeardown() throws Exception {
		UserDao dao = UserDao.getInstance();
		
		Student std = (Student) dao.findUserByIdAndRole(610,"student");
		std.setUsername("testme");
		std.setFullName("testmefully");
		dao.updateStudent(std);

		Professor prf = (Professor) dao.findUserByIdAndRole(612, "professor");
		prf.setUsername("clinger");
		prf.setFullName("clinger");
		dao.updateProfessor(prf);
		
		User ad = dao.findUserByUserName("testadminupdate");
		ad.setUsername("testadmin");
		ad.setFullName("testadminfnameupdate");
		dao.updateAdmin(ad);	
		
		
	}
}
