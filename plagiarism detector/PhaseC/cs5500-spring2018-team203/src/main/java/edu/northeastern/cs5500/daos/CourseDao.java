package edu.northeastern.cs5500.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import edu.northeastern.cs5500.models.Course;

/**
 * 
 * @author karan sharma 
 *	This class will contain the Data access objects for course table in the database 
 *
 */

@Repository
public class CourseDao {

	private static String url;
	private static String uname;
	private static String pass;
	private static String driverClassName;
	private static CourseDao instance = null;

	Logger logger = Logger.getLogger(CourseDao.class.getName());


	/**
	 * 
	 * @param driverClassName the driverClassName for the JDBC connection 
	 */
	public static void setDriverClassName(String driverClassName) {
		CourseDao.driverClassName = driverClassName;
	}

	/**
	 * 
	 * @param url  set the JDBC url 
	 */
	public static void setUrl(String url) {
		CourseDao.url = url;
	}


	/**
	 * 
	 * @param username the username for JDBC connection 
	 */
	public static void setUsername(String username) {
		CourseDao.uname = username;
	}


	/**
	 * 
	 * @param password the password for the JDBC connection
	 */
	public static void setPassword(String password) {
		CourseDao.pass = password;
	}


	/**
	 * @param url             the url of the database application
	 * @param username        the username of the database
	 * @param password        the password of the database
	 * @param driverClassName the className of the MYSQLdriver
	 */
	@Autowired
	CourseDao(@Value("${spring.datasource.url}") String url,
			@Value("${spring.datasource.username}") String username,
			@Value("${spring.datasource.password}") String password,
			@Value("${spring.datasource.driver-class-name}") String driverClassName) {

		CourseDao.setUrl(url);
		CourseDao.setUsername(username);
		CourseDao.setPassword(password);
		CourseDao.setDriverClassName(driverClassName);

	}


	/**
	 * This method will ensure that only one single instance of the DAO is created
	 *
	 * @return single instance of a UserDAO
	 */
	public static CourseDao getInstance() {
		if (instance == null) {
			instance = new CourseDao();

		}
		return instance;
	}


	private CourseDao() {

	}


	/**
	 * 
	 * @param profid the id of the professor 
	 * @param semid the semester id 
	 * @param courseno the courseno 
	 * @return true if the course with that courseno exists for that prof in that sem 
	 */
	public boolean isCourseForProfExists(int profid, int semid, String courseno, String coursename) {

		boolean present=false;

		/**
		 * sql query to check if the prof already has a particular course with courseno  
		 */
		String checkCourse="Select courseno from courses where semid=? and profid=? and courseno=? and coursename=?";

		/**
		 * Get the JDBC connection using DriverManager and create preparedStatement for the 
		 * query 
		 */
		try (Connection connection = DriverManager.getConnection(url, uname, pass);
				PreparedStatement statement = connection.prepareStatement(checkCourse)) {
			Class.forName(driverClassName);
			/**
			 * set the params for the statement object 
			 */
			statement.setInt(1, semid);
			statement.setInt(2, profid);
			statement.setString(3, courseno);
			statement.setString(4, coursename);

			
			try(ResultSet rs=statement.executeQuery())  {
				while(rs.next()) {
					/**
					 * if found a value, make present true 
					 */
					present=true;
				}
			}

		}
		catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());

		}
		
		return present;
	}



	/**
	 * 
	 * @param semid the semester id for which the course needs to be created 
	 * @param course the course that needs to be created 
	 * @return 1 if the creation is successful , 0 if its not 
	 */
	public int createCourse(int semid, Course course) {


		int result=0;
		/**
		 * if the course does not exist for the professor, then only add it, else dont add 
		 * 
		 */
		if(!isCourseForProfExists(course.getProfid(),semid,course.getCourseno(),course.getCoursename())){

			/**
			 * Create course query for inserting the values in the courses    
			 */
			String createCourse="INSERT INTO `courses` (`coursename`,"
					+ "`departmentname`,`capacity`,`courseno`,`profid`,semid, thresholdvalue, proglanguage) "
					+ "VALUES(?,?,?,?,?,?,?,?)";



			/**
			 * Get the JDBC connection using DriverManager and create preparedStatement for the 
			 * query 
			 */
			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(createCourse)) {

				Class.forName(driverClassName);
				statement.setString(1, course.getCoursename());
				statement.setString(2, course.getDepartmentname());
				statement.setInt(3,course.getCapacity());
				statement.setString(4, course.getCourseno());
				statement.setInt(5, course.getProfid());
				statement.setInt(6, semid);
				statement.setInt(7, course.getThreshold());
				statement.setString(8, course.getProglanguage());

				/**
				 * execute the query in statement 
				 */
				result=statement.executeUpdate();

			}
			catch (Exception e) {
				logger.log(Level.INFO, e.getMessage());

			}
		}
		return result;
	}


	/**
	 * THis method will delete the course using its courseid  
	 * @param courseid the id of the course we want to delete 
	 * @return 1 if the delete is successful 
	 */
	public int deleteCourse(int courseid) {

		int result=0;
		/**
		 * query to delete course 
		 */
		String deleteCourse="Delete from courses where courseid=?"; 
		try (Connection connection = DriverManager.getConnection(url, uname, pass);
				PreparedStatement statement = connection.prepareStatement(deleteCourse)) {

			Class.forName(driverClassName);
			statement.setInt(1, courseid);
			result=statement.executeUpdate();

		}
		catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());

		}

		return result; 

	}


	/**
	 * This method will update the course details 
	 * @param semid the id of the semester 
	 * @param course the course object we want to update 
	 * @return 1 if the update is successful, 0 if its not 
	 */
	public int updateCourse(int semid, Course course) {


		int result=0;
		/**
		 * Update query for updating courses 
		 */
		String updateCourse="UPDATE courses SET coursename=?, "
				+ "departmentname=?,capacity=?,"
				+ "courseno=?, thresholdvalue=?, proglanguage=? " +
				"Where profid=? and semid=? and courseid=?";
		try (Connection connection = DriverManager.getConnection(url, uname, pass);
				PreparedStatement statement = connection.prepareStatement(updateCourse)) {

			Class.forName(driverClassName);
			statement.setString(1, course.getCoursename());
			statement.setString(2, course.getDepartmentname());
			statement.setInt(3,course.getCapacity());
			statement.setString(4, course.getCourseno());
			statement.setInt(5, course.getThreshold());
			statement.setString(6, course.getProglanguage());
			statement.setInt(7, course.getProfid());
			statement.setInt(8, semid);
			statement.setInt(9, course.getCourseid());

			result=statement.executeUpdate();

		}


		catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());

		}

		return result;
	}

	/**
	 * This method will get the course details for a particular course in a particular semester 
	 * @param semid the semester id 
	 * @param profid the professor id 
	 * @param courseid the id of the course 
	 * @return Course with that courseid 
	 */
	public Course getCourse(int semid, int profid,int courseid) {

		Course course=new Course();


		String getCourse="select * from courses where courseid=? and profid=? and semid=?";
		try (Connection connection = DriverManager.getConnection(url, uname, pass);
				PreparedStatement statement = connection.prepareStatement(getCourse)) {

			Class.forName(driverClassName);
			statement.setInt(1, courseid);
			statement.setInt(2, profid);
			statement.setInt(3, semid);

			try(ResultSet rs=statement.executeQuery()){
				while(rs.next()) {
					course.setCapacity(rs.getInt("capacity"));
					course.setCourseid(rs.getInt("courseid"));
					course.setCoursename(rs.getString("coursename"));
					course.setCourseno(rs.getString("courseno"));
					course.setSemid(rs.getInt("semid"));
					course.setDepartmentname(rs.getString("departmentname"));
					course.setProfid(rs.getInt("profid"));
					course.setThreshold(rs.getInt("thresholdvalue"));
					course.setProglanguage(rs.getString("proglanguage"));
				}

			}
		}

		catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());

		}

		return course;
	}


	/**
	 * This method will get all the courses for a particular prof for particular sem
	 * @param profid the id of the professor 
	 * @param semid the semester id 
	 * @return List of courses for a particular professor 
	 */
	public List<Course> getAllCoursesForProf(int profid,int semid) {
		List<Course> courses=new ArrayList<>();

		String getCourses="select * from courses where profid=? and semid=?";

		try (Connection connection = DriverManager.getConnection(url, uname, pass);
				PreparedStatement statement = connection.prepareStatement(getCourses)) {

			Class.forName(driverClassName);
			statement.setInt(1, profid);
			statement.setInt(2, semid);
			try(ResultSet rs=statement.executeQuery()){

				while(rs.next()) {

					Course course=new Course();
					course.setCapacity(rs.getInt("capacity"));
					course.setCourseid(rs.getInt("courseid"));
					course.setCoursename(rs.getString("coursename"));
					course.setCourseno(rs.getString("courseno"));
					course.setSemid(rs.getInt("semid"));
					course.setDepartmentname(rs.getString("departmentname"));
					course.setProfid(rs.getInt("profid"));
					course.setThreshold(rs.getInt("thresholdvalue"));
					course.setProglanguage(rs.getString("proglanguage"));
					courses.add(course);
				}
			}
		}

		catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());

		}

		return courses;

	}

	/**
	 * This method will retrieve the course depending on its courseID
	 * @param courseId the courseId of the course we want to retrieve 
	 * @return the Course that we want to get 
	 */
	public Course getCourse(int courseId) {
		Course c = null;
		try {
			Class.forName(driverClassName);
			/**
			 * select query for courses where courseid is given 
			 */
			String getAssignment="Select * from courses where courseid=?";
			try(Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(getAssignment)){

				/**
				 * set the courseid using statement to prevent sql injection errors 
				 */
				statement.setInt(1, courseId);
				try(ResultSet rs=statement.executeQuery()){

					while(rs.next()) {
						c = new Course();
						c.setCourseid(rs.getInt("courseid"));
						c.setCoursename(rs.getString("coursename"));
						c.setCoursename(rs.getString("coursename"));
						c.setDepartmentname(rs.getString("departmentname"));
						c.setCourseid(rs.getInt("capacity"));
						c.setCourseno(rs.getString("courseno"));
						c.setProfid(rs.getInt("profid"));
						c.setSemid(rs.getInt("semid"));
						c.setThreshold(rs.getInt("thresholdvalue"));
						c.setProglanguage(rs.getString("proglanguage"));
					}

				}
			}
		}
		catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());
		}

		return c;
	}

	/**
	 * This method will get all the courses for a student where the student is already registered 
	 * @param id the student id 
	 * @param semid the semester id  
	 * @return list of courses that student is already registered 
	 */
	public List<Course> getAllCoursesForStudents(int id, int semid) {
		List<Course> allCourses=new ArrayList<>();

		String sql= "SELECT c.courseid,c.coursename,c.departmentname,c.courseno,c.semid FROM courses as c,studentincourse as sc,student as st where sc.studentid=st.studentid "
				+ "	and sc.studentid=? and c.courseid=sc.courseid and c.semid=?";
		try (Connection connection = DriverManager.getConnection(url, uname, pass);
				PreparedStatement statement = connection.prepareStatement(sql)) {
			Class.forName(driverClassName);
			statement.setInt(1, id);
			statement.setInt(2, semid);
			try(ResultSet rs=statement.executeQuery()){
				while(rs.next()) {
					Course c=new Course();
					c.setCourseid(rs.getInt("courseid"));
					c.setCoursename(rs.getString("coursename"));
					c.setCourseno(rs.getString("courseno"));
					c.setDepartmentname(rs.getString("departmentname"));
					c.setSemid(rs.getInt("semid"));
					allCourses.add(c);
				}
			}

		}
		catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());

		}

		return allCourses;



	}


	/**
	 * This method will return the courses for a particular sem which student hasnt registered 
	 * @param stid the id of the student 
	 * @param semid the semester id 
	 * @return List of courses that a student can register (currently unregistered) 
	 */
	public List<Course> courseStudentCanRegister(int stid, int semid) {

		List<Course> unregisteredCourses=new ArrayList<>();

		/**
		 * Get all the courses which are not in studentincourses table for that table 
		 */
		String getUnregisteredCourseSql="select * from courses as c where c.semid=? and c.courseid not in (select sc.courseid from studentincourse as sc where sc.studentid=?)";
		try (Connection connection = DriverManager.getConnection(url, uname, pass);
				PreparedStatement statement = connection.prepareStatement(getUnregisteredCourseSql)) {
			Class.forName(driverClassName);
			statement.setInt(1, semid);
			statement.setInt(2, stid);

			try(ResultSet rs=statement.executeQuery()){
				while(rs.next()) {
					Course c=new Course();
					c.setCourseid(rs.getInt("courseid"));
					c.setCoursename(rs.getString("coursename"));
					c.setCourseno(rs.getString("courseno"));
					c.setDepartmentname(rs.getString("departmentname"));
					c.setSemid(rs.getInt("semid"));
					unregisteredCourses.add(c);
				}
			}

		}
		catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());

		}

		return unregisteredCourses;



	}


	/**
	 * 
	 * @param stid the id of the student 
	 * @param courseid the courseid 
	 * @return 1 if the add is successful 
	 */
	public int addCourseForStudent(int stid, int courseid) {

		int result=0;

		String insertSql="INSERT INTO studentincourse(studentid,courseid) VALUES(?,?)";
		try (Connection connection = DriverManager.getConnection(url, uname, pass);
				PreparedStatement statement = connection.prepareStatement(insertSql)) {
			Class.forName(driverClassName);
			statement.setInt(1, stid);
			statement.setInt(2, courseid);
			result=statement.executeUpdate();

		}
		catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());

		}
		return result;

	}

	/**
	 * 
	 * @param semid the current sem id 
	 * @param cid the current course id 
	 * @param courseno the current course number 
	 * @param crosssection true if crosssection comparison is allowed 
	 * @param listofsemids listofsemids that we want to compare with 
	 * @return List of courses for a particular section and particular semester 
	 */
	public List<Integer> findAllCoursesForSectionsAndSems(int semid, int cid, String courseno, boolean crosssection,int[] listofsemids) {

		/**
		 * select query for getting courses in a particular semester where courseId is not the current course 
		 * For example get all the courses from the given semesters where course no is CS5500 but dont give current course 
		 */
		String courseWithSemFalse="SELECT * FROM courses where semid IN (#) and courseid!=? and courseno=?";
		/**
		 * get the CS5500's all the sections from the given semesters where the courseId is not the current course 
		 */
		String courseWithSection="SELECT * FROM courses where semid IN (#) and courseid!=? and courseno=?";
		List<Integer> listOfCourseIds=new ArrayList<>();
		List<Integer> uniqueSemIds=Arrays.stream(listofsemids).boxed().collect(Collectors.toList());
		/**
		 * to take unique semester ids 
		 */
		uniqueSemIds.removeIf(s -> s.equals(semid));


		/**
		 * if crossection courses are needed for example CS5500 has 2 Sections 
		 */

		if(crosssection) {
			uniqueSemIds.add(semid);
			StringBuilder qmark=new StringBuilder();
			int i=0;
			/**
			 * set the semids 
			 */
			for(int usems: uniqueSemIds) {
				if(i==0) {
					qmark.append("?");
				}else {
					qmark.append(",?");
				}
				i+=1;

			}

			/**
			 * update the string 
			 */
			String newcourseWithSemTrue= courseWithSection.replaceAll("#", qmark.toString());

			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(newcourseWithSemTrue)) {

				int j=1;

				for(int input=0;input<uniqueSemIds.size();input++) {
					statement.setInt(j, uniqueSemIds.get(input));
					j+=1;
				}

				statement.setInt(j, cid);
				j+=1;
				statement.setString(j, courseno);

				try(ResultSet rs=statement.executeQuery()){
					while(rs.next()) {
						listOfCourseIds.add(rs.getInt("courseid"));

					}
				}
			}

			catch (Exception e) {
				logger.log(Level.INFO, e.getMessage());

			}


		}// if we do not want cross section courses, just semesters where CS5500 is taught (no crossection whatsoever) 
		else {
			// take the semids, get the course id where courseno="CS"
			StringBuilder qmark=new StringBuilder();
			int i=0;

			for(int usems: uniqueSemIds) {
				if(i==0) {
					qmark.append("?");
				}else {
					qmark.append(",?");
				}
				i+=1;

			}

			String newcourseWithSemFalse= courseWithSemFalse.replaceAll("#", qmark.toString());
			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(newcourseWithSemFalse)) {



				int j=1;
				for(int input=0;input<uniqueSemIds.size();input++) {
					statement.setInt(j, uniqueSemIds.get(input));
					j+=1;
				}

				statement.setInt(j, cid);
				j+=1;
				statement.setString(j, courseno);

				try(ResultSet rs=statement.executeQuery()){
					while(rs.next()) {
						listOfCourseIds.add(rs.getInt("courseid"));

					}
				}

			}
			catch (Exception e) {
				logger.log(Level.INFO, e.getMessage());

			}

		}

		/**
		 * return the list of courseIds 
		 */
		return listOfCourseIds;
	}




}





