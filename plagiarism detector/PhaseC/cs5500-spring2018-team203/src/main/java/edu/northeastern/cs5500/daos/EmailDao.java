package edu.northeastern.cs5500.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author karan sharma 
 * This class will contain methods to access the emails for different set of users 
 *
 */
@Repository
public class EmailDao {

	private static String url;
	private static String uname;
	private static String pass;
	private static String driverClassName;
	private static EmailDao instance = null;
	Logger logger = Logger.getLogger(UserDao.class.getName());


	public static void setDriverClassName(String driverClassName) {
		EmailDao.driverClassName = driverClassName;
	}

	public static void setUrl(String url) {
		EmailDao.url = url;
	}

	public static void setUsername(String username) {
		EmailDao.uname = username;
	}


	

	public static void setPassword(String password) {
		EmailDao.pass = password;
	}


	/**
	 * @param url             the url of the database application
	 * @param username        the username of the database
	 * @param password        the password of the database
	 * @param driverClassName the className of the MYSQLdriver
	 */
	@Autowired
	EmailDao(@Value("${spring.datasource.url}") String url,
			@Value("${spring.datasource.username}") String username,
			@Value("${spring.datasource.password}") String password,
			@Value("${spring.datasource.driver-class-name}") String driverClassName) {

		EmailDao.setUrl(url);
		EmailDao.setUsername(username);
		EmailDao.setPassword(password);
		EmailDao.setDriverClassName(driverClassName);

	}


	/**
	 * This method will ensure that only one single instance of the DAO is created
	 *
	 * @return single instance of a UserDAO
	 */
	public static EmailDao getInstance() {
		if (instance == null) {
			instance = new EmailDao();

		}
		return instance;
	}


	private EmailDao() {

	}


	/**
	 * 
	 * @param userid
	 * @return
	 */
	public String getEmailFromUserId(int userid) {


		/**
		 * Email which is blank initially 
		 */
		String email="";
			
		/**
		 * select query for email 
		 */
			String select="select email from user where userid=?";

			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(select)) {

				Class.forName(driverClassName);
				statement.setInt(1, userid);
				try(ResultSet rs=statement.executeQuery();) {

					if(rs.next()) {
							email=rs.getString("email");

					}

				}

			}

		catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());

		}

			/**
			 * return the email obtained 
			 */
		return email;
	}
	
	
}
