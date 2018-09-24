package edu.northeastern.cs5500;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @author karan sharma 
 * @desc This class will initialize the spring boot application 
 *
 */
@SpringBootApplication
@Configuration
@ComponentScan
@PropertySources(value = {@PropertySource("classpath:application.properties")})
@EnableAutoConfiguration
@EnableScheduling
public class Cs5500Spring2018Team203Application extends SpringBootServletInitializer{


	/**
	 * This method will configure the SpringBuilder application 
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

	return application.sources(Cs5500Spring2018Team203Application.class);
	}
	
	public static void main(String[] args) {
		// This method will run the application as SpringApplication
		SpringApplicationBuilder builder= new SpringApplicationBuilder(Cs5500Spring2018Team203Application.class);
		/**
		 * headless for future implementation with AWT 
		 */
		builder.headless(false).run(args);
		
	}
	

	
}
