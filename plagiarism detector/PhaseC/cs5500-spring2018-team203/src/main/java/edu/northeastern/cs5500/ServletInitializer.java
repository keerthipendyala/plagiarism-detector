package edu.northeastern.cs5500;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @author karan sharma 
 * @desc This class will have the functionality to initialize the Servlet 
 *
 */
public class ServletInitializer extends SpringBootServletInitializer {

	/**
	 * This method will initialize the SpringApplicationBuilder class 
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Cs5500Spring2018Team203Application.class);
	}

}
