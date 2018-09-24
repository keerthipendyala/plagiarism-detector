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

import edu.northeastern.cs5500.models.User;


/**
 * 
 * This class will contain Data Access Object methods that will fetch data from PendingTable in the 
 * database.  Pending Table is the staging table for Professors where they will be added in the users 
 * if and only if admin approves their requests
 * @author karan sharma 
 *  *
 */
@Repository
public class PendingTableDao {

	Logger logger = Logger.getLogger(PendingTableDao.class.getName());

	/**
	 * variables that will hold the JDBC connection properties objects 
	 */
	private static String url;
	private static String uname;
	private static String pass;
	private static String driverClassName;
	private static PendingTableDao instance = null;
	
	
	public static void setDriverClassName(String driverClassName) {
		PendingTableDao.driverClassName = driverClassName;
	}

	public static void setUrl(String url) {
		PendingTableDao.url = url;
	}


	public static void setUsername(String username) {
		PendingTableDao.uname = username;
	}


	public static void setPassword(String password) {
		PendingTableDao.pass = password;
	}


	/**
	 * @param url             the url of the database application
	 * @param username        the username of the database
	 * @param password        the password of the database
	 * @param driverClassName the className of the MYSQLdriver
	 */
	@Autowired
	PendingTableDao(@Value("${spring.datasource.url}") String url,
			@Value("${spring.datasource.username}") String username,
			@Value("${spring.datasource.password}") String password,
			@Value("${spring.datasource.driver-class-name}") String driverClassName) {

		PendingTableDao.setDriverClassName(driverClassName);
		PendingTableDao.setUrl(url);
		PendingTableDao.setUsername(username);
		PendingTableDao.setPassword(password);

	}


	/**
	 * This method will ensure that only one single instance of the DAO is created
	 *
	 * @return single instance of a UserDAO
	 */
	public static PendingTableDao getInstance() {
		if (instance == null) {
			instance = new PendingTableDao();

		}
		return instance;
	}


	private PendingTableDao() {

	}


	/**
	 * This method will return all professors whose requests are pending 
	 * @return the list of professors whose requests are pending  
	 */
	public List<User> getAllUsers() {

		List<User> pendingUsers = new ArrayList<>();

		/**
		 * select query to get all professors 
		 */
			String sql = "select * from pendingtable";

			/**
			 * Get the connection object from driver, create a prepared statement from SQL
			 */
			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(sql);
					ResultSet profset = statement.executeQuery()) {

				while (profset.next()) {
						User u = new User();
						u.setUserid(profset.getInt("pendingid"));
						u.setUsername(profset.getString("username"));
						u.setPassword(profset.getString("password"));
						u.setEmail(profset.getString("email"));
						u.setFullName(profset.getString("fullname"));
						u.setRole("professor");
						u.setFlag(profset.getInt("ispending"));
						pendingUsers.add(u);
		
				} 			

			}
			catch (Exception e) {
				logger.log(Level.INFO, e.getMessage());
			}        


		return pendingUsers;


	}


	/**
	 * In this method, the request of the professor will be accepted 
	 * @param id the id of the professor whose request will be accepted 
	 * @return 1 if successful else 0 
	 */
	public int acceptRequest(int id) {

		/**
		 * Request is processed in 3 steps
		 * 1st step is where the record from the pending table is accessed and its values are saved in
		 * user
		 * 2nd step is where the object is added to user table
		 * 3rd step is where the object is added to professor table
		 */
		int result = 0;
		try {

			Class.forName(driverClassName);

			String sql = "select * from pendingtable where pendingid=?";

			/**
			 * Get the connection object from driver, create a prepared statement from SQL
			 */
			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(sql)) {

				/**
				 * Set the id received to the first parameter of the prepared statement
				 */
				statement.setInt(1, id);

				User u = new User();
				UserDao daoobj = UserDao.getInstance();
				try (ResultSet profset = statement.executeQuery()) {


					while (profset.next()) {
						u.setUsername(profset.getString("username"));
						u.setPassword(profset.getString("password"));
						u.setEmail(profset.getString("email"));
						u.setFullName(profset.getString("fullname"));
						u.setRole("professor");

					}

					result = daoobj.createUser(u);

					String insertPendingTrue = "UPDATE pendingtable SET ispending=? WHERE pendingid = ?";

					try (PreparedStatement makePendingTrue = connection.prepareStatement(insertPendingTrue)) {

						/**
						 * Set the id received to the first parameter of the prepared statement
						 */
						makePendingTrue.setInt(1, 0);
						makePendingTrue.setInt(2, id);
						result = makePendingTrue.executeUpdate();

					} 

				}


			} 

		} 
		catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());
		}        
		return result;
	}


	/**
	 * THis method will add the user to the pending table 
	 * @param user the user that will be added to the pending table 
	 * @return 1 if successful, else 0 
	 */
	public int addUserToPendingTable(User user) {


		int result = 0;
		try {

			Class.forName(driverClassName);

			/**
			 * insert query to add the professor to pending table 
			 */
			String sql = "INSERT INTO pendingtable(username,password,email,fullname)VALUES (?,?,?,?)";

			/**
			 * Get the connection object from driver, create a prepared statement from SQL
			 */
			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(sql)) {

				/**
				 * Set the id received to the first parameter of the prepared statement
				 */
				statement.setString(1, user.getUsername());
				statement.setString(2, user.getPassword());
				statement.setString(3, user.getEmail());
				statement.setString(4, user.getFullname());

				/**
				 * Execute the sql query parsed by statement
				 */
				result = statement.executeUpdate();

			}

		} 
		catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());
		}
		return result;
	}

	/**
	 * In this method, the request of professor to be added as professor will be rejected 
	 * @param id the id of the professor whose request will be rejected 
	 * @return 1 if successful, 0 else 
	 */
	public int rejectRequest(int id) {

		int result = 0;

		try {

			Class.forName(driverClassName);

			getEmailForSendingRequest(id);
			String sql = "DELETE FROM pendingtable WHERE pendingid=?";

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
				result = statement.executeUpdate();

			}
			
		} 

		catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());
		}
		return result;

	}

	/**
	 * 
	 * @param id the id of the user whom we want to send email 
	 * @return email id of the user 
	 */
	public String getEmailForSendingRequest(int id) {

		/**
		 * select query for getting the email from pending table 
		 */
		String sql = "select email from pendingtable where pendingid=?";
		String email = "";
		try {

			Class.forName(driverClassName);

			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(sql)) {

				/**
				 * Set the id received to the first parameter of the prepared statement
				 *
				 */

				statement.setInt(1, id);
				/**
				 * Execute the sql query parsed by statement
				 */
				try (ResultSet result = statement.executeQuery()) {
					while (result.next()) {
						email = result.getString("email");
					}

				}

			}


		} catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());
		}

		return email;

	}


	/**
	 * This method will get the username,fullname from the pending table 
	 * @param username username of the user in pending table 
	 * @return the User details 
	 */
	public User getUserFromPendingTable(String username) {


		User u=new User();
		try {
			Class.forName(driverClassName);

			String sql = "SELECT username,pendingid,fullname from pendingtable where username=?";
			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(sql)) {

				statement.setString(1, username);

				try(ResultSet result = statement.executeQuery()) {
					while (result.next()) {
						u.setUserid(result.getInt("pendingid"));
						u.setUsername(result.getString("username"));
					}
				}


			}

		}
		catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());

		}

		return u;


	}

}





