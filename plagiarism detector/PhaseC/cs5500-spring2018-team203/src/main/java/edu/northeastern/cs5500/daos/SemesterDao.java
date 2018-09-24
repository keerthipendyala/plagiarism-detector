package edu.northeastern.cs5500.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import edu.northeastern.cs5500.models.Semester;

/**
 * This class will contain Data Access Objects for accessing the Semester Table.
 * Each entry in the semester table will contain a single semester that was or is in operation 
 * @author karan sharma 
 *
 */
@Repository
public class SemesterDao {

	private static String url;
	private static String uname;
	private static String pass;
	private static String driverClassName;
	private static SemesterDao instance = null;

	Logger logger = Logger.getLogger(SemesterDao.class.getName());
	
	/**
	 * Setter methods for setting up the JDBC connection parameters 
	 * 
	 */
	public static void setDriverClassName(String driverClassName) {
		SemesterDao.driverClassName = driverClassName;
	}

	public static void setUrl(String url) {
		SemesterDao.url = url;
	}


	public static void setUsername(String username) {
		SemesterDao.uname = username;
	}


	public static void setPassword(String password) {
		SemesterDao.pass = password;
	}


	@Autowired
	SemesterDao(@Value("${spring.datasource.url}") String url,
			@Value("${spring.datasource.username}") String username,
			@Value("${spring.datasource.password}") String password,
			@Value("${spring.datasource.driver-class-name}") String driverClassName) {

		SemesterDao.setUrl(url);
		SemesterDao.setUsername(username);
		SemesterDao.setPassword(password);
		SemesterDao.setDriverClassName(driverClassName);


	}


	/**
	 * This method will ensure that only one single instance of the DAO is created
	 *
	 * @return single instance of a UserDAO
	 */
	public static SemesterDao getInstance() {
		if (instance == null) {
			instance = new SemesterDao();

		}
		return instance;
	}


	private SemesterDao() {

	}

	
	/**
	 * This method will get all the semesters added in the system 
	 * @return List of semester objects 
	 */

	public List<Semester> getAllSemesters() {


		List<Semester> sems=new ArrayList<>();
		try {

			/**
			 * format of the date of operation of the semester data 
			 */
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");


			/**
			 * get all semester query from semester table 
			 */
			String getSem="select * from semester";

			Class.forName(driverClassName);

			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(getSem)) {

				try(ResultSet rs=statement.executeQuery()){
					while(rs.next()) {
						Semester s=new Semester();
						s.setSemesterid(rs.getInt("semesterid"));
						s.setSemestername(rs.getString("semestername"));
						s.setStartdate(rs.getString("startdate"));
						s.setEnddate(rs.getString("enddate"));
						sems.add(s);
					}


				}

				/**
				 * we need to sort the semesters in reverse chronological order. This we 
				 * will do by comparing their startdates and returning the one which is the 
				 * most recent 
				 */
					Collections.sort(sems, (s1, s2) -> {
				try {
					return (formatter.parse(s1.getStartdate()).compareTo(formatter.parse(s2.getStartdate())))*-1;
				} catch (ParseException e) {

					logger.log(Level.SEVERE, e.getMessage());
				}
				return 0;
			});

					

			}

		}
		catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());


		}

		return sems;


	}

}	
