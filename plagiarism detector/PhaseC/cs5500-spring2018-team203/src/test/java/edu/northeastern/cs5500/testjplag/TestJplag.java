package edu.northeastern.cs5500.testjplag;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.northeastern.cs5500.models.Submission;
import edu.northeastern.cs5500.models.User;
import edu.northeastern.cs5500.service.ComparisionService;

/**
 * @author anju
 * Test for Jplag
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class TestJplag {

	/**
	 * Test jplag to scan assignments
	 */
	@Test
	public void jplag() {
		ComparisionService cs=new ComparisionService();
		Submission sub1= new Submission();
		sub1.setAssignmentid(1);
		sub1.setcourseid(1);
		sub1.setstudentid(1);
		sub1.setsubmissionlink("https://github.com/keerthipendyala/msd-project-test.git");
		sub1.setsubmissionname("Assignment1");
		sub1.setsubmissiondesc("desc");
		
		Submission sub2= new Submission();
		sub2.setAssignmentid(1);
		sub2.setcourseid(1);
		sub2.setstudentid(2);
		sub2.setsubmissionlink("https://github.com/Karan1909/pythonrepo.git");
		List<Submission> sublist=new ArrayList<>();
		sublist.add(sub2);
		sublist.add(sub1);
		
		MockHttpServletRequest req=new MockHttpServletRequest();
		req.setServerName("localhost");
		req.setLocalPort(8080);
		
		
		UriComponents ucomponent =
	            ServletUriComponentsBuilder
	                .fromContextPath(req).port(8080)
	                .path("/api/scanassignment")
	                .build();
		URI uri=ucomponent.toUri();
		
		/*
		req.setRequestURI("/api/scanassignment");*/
		System.out.println(uri.toString());
		HttpUriRequest request=new HttpGet(req.toString());
		try {
			HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
			HttpEntity entity = httpResponse.getEntity();
			
			String rp=EntityUtils.toString(entity);
			ObjectMapper mapper = new ObjectMapper();
			User u=mapper.readValue(rp, User.class);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cs.scanSubmissionInJplagAssignment(sublist, req);
		
	
	}

	
	
}
