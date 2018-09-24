package edu.northeastern.cs5500.testservices;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.Cs5500Spring2018Team203Application;
import edu.northeastern.cs5500.daos.CourseDao;
import edu.northeastern.cs5500.models.Course;
import edu.northeastern.cs5500.service.CourseService;

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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author Keerthi Pendyala
 * @version 1.0
 * @date 3/30/18 9:48 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Cs5500Spring2018Team203Application.class)
@DataJpaTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CourseServiceTest {

    /**
     * Helper method to create a JSON String
     *
     * @param obj Java Object
     * @return JSON String
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
     * Helper method to convert Object to JSON bytes
     *
     * @param object input
     * @return JSON bytes
     */
    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }


    private static MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new CourseService()).build();


    
    /**
     * New Tests for courses 
     * @throws Exception 
     * @throws IOException 
     * 
     * 
     */
    @Test
    public void testACreateCourse() throws IOException, Exception {
    	// 34 1 
    	Course c=new Course();
    	c.setCapacity(30);
    	c.setCoursename("Human Computer Interaction");
    	c.setSemid(1);
    	c.setProfid(34);
    	c.setCourseno("CS5340");
    	
    	
    	mockMvc.perform(post("/api/createCourse")
                .contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                        MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8")))
                .content(edu.northeastern.cs5500.testservices.CourseServiceTest.convertObjectToJsonBytes(c)))
                .andExpect(status().isOk());
    	
    	
    	
    }
    
    
    @Test
    public void testGetCourseForASemAProf() throws IOException, Exception {
    
    	MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/getCourse/"+ 1 + "/"+ 1 +"/" + 33).accept(
				MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		System.out.println(result.getResponse().getContentAsString());
		assertEquals(true,result.getResponse().getContentAsString().contains("MSD"));
    	
    }
    
    
    @Test
    public void testGetAllCoursesForASem() throws Exception {
    	
    	MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/getAllCourses/"+ 1 + "/" + 33).accept(
				MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		System.out.println("get all courses"+result.getResponse().getContentAsString());
		assertEquals(true,result.getResponse().getContentAsString().contains("MSD"));
    	
    }
    
    
    @Test
    public void testXUpdateCourse() throws Exception {
    	
    	Course c=new Course();
    	c.setCapacity(40);
    	c.setCoursename("Human Computer Interaction");
    	c.setSemid(1);
    	c.setProfid(33);
    	c.setDepartmentname("CCIS");
    	c.setCourseno("CS5341");
    	c.setThreshold(85);
    	c.setCourseid(53);
    	CourseDao cdao=CourseDao.getInstance();
    	Course crs= cdao.getCourse(53);
    
    	mockMvc.perform(post("/api/updateCourse")
                .contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                        MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8")))
                .content(edu.northeastern.cs5500.testservices.CourseServiceTest.convertObjectToJsonBytes(c)))
                .andExpect(status().isOk());
    	
    	
    }
    
    @Test
    public void testGetRegisteredCoursesForStudent() throws Exception {
    	
    	MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/getAllStudentCourses/"+ 27 + "/" + 1).accept(
				MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(true,result.getResponse().getContentAsString().contains("DBMS"));
		
    	
    }
    
    
    @Test
    public void testUnRegisteredCoursesForStudent() throws Exception {
    	
    	MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/getUnregisteredCourses/"+ 27 + "/" + 1).accept(
				MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(true,result.getResponse().getContentAsString().contains("MSD"));
		
    	
    }
    
    @Test
    public void addCourse() throws Exception {
    	
    	mockMvc.perform(get("/api/addCourse/"+ 27 + "/" + 94)
                .contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                        MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"))))
                .andExpect(status().isOk());
    	
    	
    	
    }
    
    
    
    
    
    
    
    @Test
    public void testdeleteCourse() throws Exception {
    	
    	CourseDao cdao=CourseDao.getInstance();
    	int coursedelete=0;
    	List<Course> courses=cdao.getAllCoursesForProf(34, 1);
    	for(Course c: courses) {
    	
    		if(c.getCourseno().equalsIgnoreCase("CS5340") ){
    			coursedelete=c.getCourseid();
    			System.out.println("delete"+coursedelete);
    			break;
    		}
    	}
    	
    	mockMvc.perform(delete("/api/deleteCourse/"+coursedelete)
				.contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), 
						MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8")))).andExpect(status().isOk());
    }
    
    
    
}
