package edu.northeastern.cs5500.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import edu.northeastern.cs5500.encryption.PassSecure;
import edu.northeastern.cs5500.models.Professor;
import edu.northeastern.cs5500.models.Student;
import edu.northeastern.cs5500.models.User;

/**
 * User table is the root table which will have all the user information stored.The student 
 * and professor tables are inherited from User table 
 * @author karan sharma
 * @desc This DAO will contain Data access objects that will perform various CRUD operations
 */
@Repository
public class UserDao {

	private static String url;
	private static String uname;
	private static String pass;
	private static String driverClassName;
	private static UserDao instance = null;
	
	Logger logger = Logger.getLogger(UserDao.class.getName());



	/**
	 * setter methods for setting the JDBC connection parameters 
	 * 
	 */
	public static void setDriverClassName(String driverClassName) {
		UserDao.driverClassName = driverClassName;
	}



	public static void setUrl(String url) {
		UserDao.url = url;
	}




	public static void setUsername(String username) {
		UserDao.uname = username;
	}





	public static void setPassword(String password) {
		UserDao.pass = password;
	}


	/**
	 * @param url             the url of the database application
	 * @param username        the username of the database
	 * @param password        the password of the database
	 * @param driverClassName the className of the MYSQLdriver
	 */
	@Autowired
	UserDao(@Value("${spring.datasource.url}") String url,
			@Value("${spring.datasource.username}") String username,
			@Value("${spring.datasource.password}") String password,
			@Value("${spring.datasource.driver-class-name}") String driverClassName) {

		UserDao.setUrl(url);
		UserDao.setUsername(username);
		UserDao.setPassword(password);
		UserDao.setDriverClassName(driverClassName);

	}


	/**
	 * This method will ensure that only one single instance of the DAO is created
	 *
	 * @return single instance of a UserDAO
	 */
	public static UserDao getInstance() {
		if (instance == null) {
			instance = new UserDao();

		}
		return instance;
	}


	private UserDao() {

	}

	/**
	 * @param id id of the user to be found
	 * @return send the user found
	 */
	public User findUserById(int id) {
		User user = null;

		try {

			Class.forName(driverClassName);

			String sql = "SELECT username,fullname,role from user where userid=?";

			/**
			 * Get the connection object from driver, create a prepared statement from SQL
			 */
			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(sql)) {

				/**
				 * Set the id received to the first parameter of the prepared statement
				 */
				statement.setInt(1, id);

				/**
				 * Execute the sql query parsed by statement
				 */
				try (ResultSet results = statement.executeQuery();) {
					if (results.next()) {
						// get the username column value from the resultset
						String uname = results.getString("username");
						// get the full name column value from the resultset
						String fullname = results.getString("fullname");
						// set those values to a new user object
						user = new User(uname, fullname, id);
						user.setRole(results.getString("role"));

					}
				}
			}

		} catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());

		}
		// return the created user
		return user;

	}

	/**
	 * @param username the username of the user we want to find
	 * @return the User if found else null
	 */
	public User findUserByUserName(String username) {
		User user = null;
		try {
			Class.forName(driverClassName);


			/**
			 * SQL query to select the user
			 */
			String sql = "SELECT fullname,userid,password,email from user where username=?";

			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(sql)) {

				/**
				 * Set the username in the prepared statement
				 */
				statement.setString(1, username);
				try (ResultSet results = statement.executeQuery();) {
					if (results.next()) {
						/**
						 * get the fullname from the user found
						 */
						String fullname = results.getString("fullname");
						int userid = results.getInt("userid");
						String pass = results.getString("password");
						/**
						 * create a user object and return it
						 */
						user = new User(userid, username, pass, fullname);
						user.setEmail(results.getString("email"));
					}
				}
			}

		} catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());

		}
		return user;

	}


	/**
	 * @param recievedUser is the user recieved from UI form
	 * @return User returned
	 */

	public User findUser(User recievedUser) {

		PassSecure ps = PassSecure.getInstance();
		User user = null;
		try {
			Class.forName(driverClassName);
			String sql = "SELECT username,fullname,abc,userid,role from user where username=?";
			sql = sql.replaceAll("abc", "password");

			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setString(1, recievedUser.getUsername());

				/**
				 * Execute the sql query parsed by statement
				 *
				 */

				try (ResultSet results = statement.executeQuery();) {
					if (results.next() &&
							ps.isPass(recievedUser.getPassword(), results.getString("password"))) {
						// get the username column value from the resultset
						String uname = results.getString("username");
						// get the fullname column value from the resultset
						String fullname = results.getString("fullname");
						// get the password column value from the resultset
						String pass = results.getString("password");
						// get the id column value from the resultset
						int userid = results.getInt("userid");
						// create a new user object
						user = new User(userid, uname, pass, fullname);
						user.setRole(results.getString("role"));
					}
				}
			}
		} catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());
		}
		// return the user found
		return user;
	}

	/**
	 * @param user The user that we want to create
	 * @return 1 if the user is successfully added , 0 if the user is not added
	 */
	public int createUser(User user) {
		int result = 0;
		try {
			Class.forName(driverClassName);
			/**
			 * Insert query to add user into the database
			 */

			PassSecure ps = PassSecure.getInstance();
			String role = user.getRole();
			String sqlToInsertUser = "INSERT INTO user (username,password,phone,address,dateofbirth,fullname,role,email) VALUES (?,?,?,?,?,?,?,?)";
			String sqlToRetrieveUser = "Select * from user where username=?";
			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement insertUser = connection.prepareStatement(sqlToInsertUser);
					PreparedStatement retrieveUser = connection.prepareStatement(sqlToRetrieveUser);) {
				insertUser.setString(1, user.getUsername());
				insertUser.setString(2, ps.encrypt(user.getPassword()));
				insertUser.setString(3, user.getPhone());
				insertUser.setString(4, user.getAddress());
				insertUser.setString(5, user.getDateofBirth());
				insertUser.setString(6, user.getFullname());
				insertUser.setString(7, user.getRole());
				insertUser.setString(8, user.getEmail());

				/**
				 * Execute the insert query
				 */
				result = insertUser.executeUpdate();

				/**
				 * Now retrieve the user that was inserted in database
				 *
				 */

				retrieveUser.setString(1, user.getUsername());

				int userid = 0;
				try (ResultSet studentset = retrieveUser.executeQuery();) {
					while (studentset.next()) {
						userid = studentset.getInt("userid");
					}
				}

				if (role.equalsIgnoreCase("student")) {
					String sqlToInsertStudent = "INSERT INTO student (userid) VALUES (?)";
					try (PreparedStatement insertStudent = connection.prepareStatement(sqlToInsertStudent);) {

						/**
						 * Perform insert in the student table
						 */
						insertStudent.setInt(1, userid);
						result = insertStudent.executeUpdate();

					}

				} else if (role.equalsIgnoreCase("professor")) {

					String sqlToInsertProfessor = "INSERT INTO professor (userid) VALUES (?)";
					try (PreparedStatement insertProfessor = connection.prepareStatement(sqlToInsertProfessor);) {

						/**
						 *
						 * Perform insert in the student table
						 */
						insertProfessor.setInt(1, userid);
						result = insertProfessor.executeUpdate();

					}

				}

			} 
		} 

		catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());
		}
		return result;

	}


	public User findUserByIdAndRole(int id, String role) {

		try {

			Class.forName(driverClassName);

			if (role.equalsIgnoreCase("student")) {

				User s = new Student();

				/*Select p.id,p.firstname,p.lastName,p.username,p.password,p.email,p.dob,d.developerKey from person as p inner join developer as d where p.id=d.person*/
				String sql = "SELECT u.username,u.password,u.phone,u.address,u.dateofbirth,u.fullname,u.email,s.status,s.studentid,s.dateofgraduation from user as u inner join student as s where u.userid=s.userid and s.userid=?";


				/**
				 * Get the connection object from driver, create a prepared statement from SQL
				 */

				try (Connection connection = DriverManager.getConnection(url, uname, pass);
						PreparedStatement statement = connection.prepareStatement(sql)) {

					/**
					 * Set the id received to the first parameter of the prepared statement
					 */

					statement.setInt(1, id);

					/**
					 * Execute the sql query parsed by statement
					 */

					try (ResultSet results = statement.executeQuery();) {
						if (results.next()) {
							s.setUserid(id);
							s.setUsername(results.getString("username"));
							s.setFullName(results.getString("fullname"));
							s.setAddress(results.getString("address"));
							s.setPhone(results.getString("phone"));
							s.setPassword(results.getString("password"));
							s.setDateofBirth(results.getString("dateofbirth"));
							s.setEmail(results.getString("email"));
							s.setRole(role);
							((Student) s).setStudentid(results.getInt("studentid"));
							((Student) s).setStatus(results.getString("status"));
							((Student) s).setDateofgraduation(results.getString("dateofgraduation"));

						}
					}

					return s;

				} catch (Exception e) {
					logger.log(Level.INFO, e.getMessage());

				}

			} else if (role.equalsIgnoreCase("professor")) {
				String sql = "SELECT u.username,u.password,u.phone,u.address,u.dateofbirth,u.fullname,u.email,p.title,p.degree,p.department,p.profid from user as u inner join professor as p where u.userid=p.userid  and p.userid=?";

				User prof = new Professor();
				try (Connection connection = DriverManager.getConnection(url, uname, pass);
						PreparedStatement statement = connection.prepareStatement(sql)) {

					/**
					 * Set the id received to the first parameter of the prepared statement
					 */

					statement.setInt(1, id);

					/**
					 * Execute the sql query parsed by statement
					 */

					try (ResultSet results = statement.executeQuery();) {
						if (results.next()) {
							prof.setUserid(id);
							prof.setUsername(results.getString("username"));
							prof.setFullName(results.getString("fullname"));
							prof.setPassword(results.getString("password"));
							prof.setPhone(results.getString("phone"));
							prof.setAddress(results.getString("address"));
							prof.setDateofBirth(results.getString("dateofbirth"));
							prof.setRole(role);
							prof.setEmail(results.getString("email"));
							((Professor) prof).setDegree(results.getString("degree"));
							((Professor) prof).setProfessorid(results.getInt("profid"));
							((Professor) prof).setDepartment(results.getString("department"));
							((Professor) prof).setTitle(results.getString("title"));
							// set those values to a new user object

						}

					}

					return prof;

				} catch (Exception e) {
					logger.log(Level.INFO, e.getMessage());

				}
			} else if (role.equalsIgnoreCase("admin")) {
				String adminSql = "SELECT * from user where userid=?";

				User admin = new User();
				try (Connection connection = DriverManager.getConnection(url, uname, pass);
						PreparedStatement statement = connection.prepareStatement(adminSql)) {

					/**
					 * Set the id received to the first parameter of the prepared statement
					 */

					statement.setInt(1, id);

					/**
					 * Execute the sql query parsed by statement
					 */

					try (ResultSet results = statement.executeQuery();) {
						if (results.next()) {
							admin.setUserid(id);
							admin.setUsername(results.getString("username"));
							admin.setFullName(results.getString("fullname"));
							admin.setPassword(results.getString("password"));
							admin.setPhone(results.getString("phone"));
							admin.setAddress(results.getString("address"));
							admin.setDateofBirth(results.getString("dateofbirth"));
							admin.setRole(role);
							admin.setEmail(results.getString("email"));
						}

					}

					return admin;

				} 			

			}

		} 
		catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());


		}
		return new User();

	}

	/**
	 * this method updated the Student entry with the given Student object
	 * @param admin is the Student object that contains updated details
	 * @return the status of the update action
	 */
	public int updateStudent(Student s) {

		String updateSql = "UPDATE user as u,student as s SET u.username = ?,u.password = ?,u.phone = ?," +
				"u.address = ?,u.dateofbirth = ?,u.fullname = ?,u.role = ?,u.email = ?," +
				"s.status=?,s.dateofgraduation=? WHERE u.userid = s.userid and s.userid=?";
		int result = 0;

		PassSecure ps = PassSecure.getInstance();
		try {

			Class.forName(driverClassName);
			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(updateSql)) {


				statement.setString(1, s.getUsername());
				if(!s.getPassword().startsWith("$2a$")){
					statement.setString(2, ps.encrypt(s.getPassword()));
				}
				else {
					statement.setString(2, s.getPassword());
				}

				statement.setString(3, s.getPhone());
				statement.setString(4, s.getAddress());
				statement.setString(5, s.getDateofBirth());
				statement.setString(6, s.getFullname());
				statement.setString(7, s.getRole());
				statement.setString(8, s.getEmail());
				statement.setString(9, s.getStatus());
				statement.setString(10, s.getDateofgraduation());
				statement.setInt(11, s.getUserid());
				result = statement.executeUpdate();

			}
		} catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());

		}

		return result;
	}


	/**
	 * this method updated the admin entry with the given admin object
	 * @param admin is the admin object that contains updated details
	 * @return the status of the update action
	 */
	public int updateProfessor(Professor p) {

		String updateSql = "UPDATE user as u,professor as p SET u.username = ?," +
				"u.password = ?,u.phone = ?,u.address = ?,u.dateofbirth = ?,u.fullname = ?," +
				"u.role = ?,u.email = ?,p.title=?,p.degree=?,p.department=? WHERE u.userid = p.userid and " +
				"p.userid=?";

		int result = 0;
		PassSecure ps = PassSecure.getInstance();

		try {

			Class.forName(driverClassName);
			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(updateSql)) {


				statement.setString(1, p.getUsername());
				if(!p.getPassword().startsWith("$2a$")){
				
					statement.setString(2, ps.encrypt(p.getPassword()));
				}
				else 
				{
					statement.setString(2,p.getPassword());
				}
				
				statement.setString(3, p.getPhone());
				statement.setString(4, p.getAddress());
				statement.setString(5, p.getDateofBirth());
				statement.setString(6, p.getFullname());
				statement.setString(7, p.getRole());
				statement.setString(8, p.getEmail());
				statement.setString(9, p.getTitle());
				statement.setString(10, p.getDegree());
				statement.setString(11, p.getDepartment());
				statement.setInt(12, p.getUserid());
				result = statement.executeUpdate();

			}
		} catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());

		}

		return result;
	}

	/**
	 * this method updated the admin entry with the given admin object
	 * @param admin is the admin object that contains updated details
	 * @return the status of the update action
	 */
	public int updateAdmin(User admin) {

		String updateSql = "UPDATE user SET username = ?,password = ?," +
				"phone = ?,address = ?,dateofbirth = ?," +
				"fullname = ?,role = ?,email = ? WHERE userid = ?";
		int result = 0;
		PassSecure ps = PassSecure.getInstance();


		try {

			Class.forName(driverClassName);
			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(updateSql)) {


				statement.setString(1, admin.getUsername());
				statement.setString(2, ps.encrypt(admin.getPassword()));
				statement.setString(3, admin.getPhone());
				statement.setString(4, admin.getAddress());
				statement.setString(5, admin.getDateofBirth());
				statement.setString(6, admin.getFullname());
				statement.setString(7, admin.getRole());
				statement.setString(8, admin.getEmail());
				statement.setInt(9, admin.getUserid());
				result = statement.executeUpdate();

			}
		} catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());

		}

		return result;
	}

	public List<User> getAllProfessors() {

		List<User> profs = new ArrayList<>();

		try {

			Class.forName(driverClassName);

			String sql = "select * from user where role='professor'";


			/**
			 * Get the connection object from driver, create a prepared statement from SQL
			 */
			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(sql)) {

				/**
				 * Set the id received to the first parameter of the prepared statement
				 */

				try (ResultSet profset = statement.executeQuery()) {


					while (profset.next()) {
						User u = new User();
						u.setUserid(profset.getInt("userid"));
						u.setUsername(profset.getString("username"));
						u.setPassword(profset.getString("password"));
						u.setEmail(profset.getString("email"));
						u.setFullName(profset.getString("fullname"));
						u.setRole("professor");
						profs.add(u);

					}
				} 			
			} 
		} catch (Exception e) {

			logger.log(Level.INFO, e.getMessage());
		}

		return profs;


	}

	public List<User> getAllStudents() {

		List<User> students = new ArrayList<>();

		try {

			Class.forName(driverClassName);

			String sql = "select * from user where role='student'";


			/**
			 * Get the connection object from driver, create a prepared statement from SQL
			 */
			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(sql)) {

				/**
				 * Set the id received to the first parameter of the prepared statement
				 */

				try (ResultSet studentset = statement.executeQuery()) {


					while (studentset.next()) {
						User u = new User();
						u.setUserid(studentset.getInt("userid"));
						u.setUsername(studentset.getString("username"));
						u.setPassword(studentset.getString("password"));
						u.setEmail(studentset.getString("email"));
						u.setFullName(studentset.getString("fullname"));
						u.setRole("student");
						students.add(u);

					}
				} 			
			}
		}
		catch (Exception e) {

			logger.log(Level.INFO, e.getMessage());
		}

		return students;

	}

	/**
	 * this method deletes the user entry from the database
	 * @param username represents the unique username for a user
	 * @return the status of result action
	 */
	public int deleteUser(String username) {

		int result = 0;
		try {

			Class.forName(driverClassName);
			String sql = "DELETE from user where username=?";
			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setString(1, username);
				result = statement.executeUpdate();

			}

		} catch (Exception e) {

			logger.log(Level.INFO, e.getMessage());
		}


		return result;

	}


	/**
	 * this method retrieves the user data from the database
	 * @param userid is the unique id of a user
	 * @return the user object if retrieved properly
	 */
	public User getUserDetails(int userid) {
		User user = null;
		try {

			Class.forName(driverClassName); 
			String sql = "SELECT username,password,phone,address,dateofbirth,fullname,role,email from user where userid=?";

			/**
			 * Get the connection object from driver, create a prepared statement from SQL
			 */
			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(sql)) {

				/**
				 * Set the id received to the first parameter of the prepared statement
				 */
				statement.setInt(1, userid);

				/**
				 * Execute the sql query parsed by statement
				 */
				try (ResultSet results = statement.executeQuery();) {
					if (results.next()) {
						user = new User();
						user.setUsername(results.getString("username"));
						user.setRole(results.getString("role"));
						user.setPhone(results.getString("phone"));
						user.setPassword(results.getString("password"));
						user.setFullName(results.getString("fullname"));
						user.setEmail(results.getString("email"));
						user.setDateofBirth(results.getString("dateofBirth"));
						user.setAddress(results.getString("address"));
					}
				}
			}

		} catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());

		}
		// return the user details
		return user;

	}
	
	
	/**
	 * this method updates the role of the user in the database
	 * @param userid is the unique id of a user
	 * @param newrole is the new role for the provided user
	 * @return the status of the update action
	 */
	public int changeRole(int userid, String newrole) {
		User usr = this.getUserDetails(userid);
		if(usr ==  null)
			return 0;
		else if(usr.getRole().equals(newrole))
			return -1;
		else {
			if(this.deleteUser(usr.getUsername())==1) {
				usr.setRole(newrole);
				this.createUser(usr);
				return 1;
			}
		}
		return 0;
	}

}

