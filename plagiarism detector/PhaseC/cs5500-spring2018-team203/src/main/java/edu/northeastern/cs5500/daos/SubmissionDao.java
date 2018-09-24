package edu.northeastern.cs5500.daos;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import edu.northeastern.cs5500.models.Submission;

/**
 * @author anju
 * @desc This DAO will contain Data access objects that will perform various CRUD operations
 */
@Repository
public class SubmissionDao {
	private static String url;
	private static String uname;
	private static String pass;
	private static String driverClassName;
	private static SubmissionDao instance = null;
	Logger logger = Logger.getLogger(SubmissionDao.class.getName());

	/**
	 * Setter method for url
	 *
	 * @param url is a string
	 */
	public static void setUrl(String url) {
		SubmissionDao.url = url;
	}


	/**
	 * Getter method for username
	 *
	 * @return a string
	 */
	public static void setUname(String uname) {
		SubmissionDao.uname = uname;
	}


	/**
	 * Setter method for password
	 *
	 * @param pass is a string
	 */
	public static void setPass(String pass) {
		SubmissionDao.pass = pass;
	}


	/**
	 * Setter method for driver class name
	 *
	 * @param driverClassName is a string
	 */
	public static void setDriverClassName(String driverClassName) {
		SubmissionDao.driverClassName = driverClassName;
	}




	/**
	 * @param url             the url of the database application
	 * @param username        the username of the database
	 * @param password        the password of the database
	 * @param driverClassName the className of the MYSQLdriver
	 */
	@Autowired
	SubmissionDao(@Value("${spring.datasource.url}") String url,
			@Value("${spring.datasource.username}") String username,
			@Value("${spring.datasource.password}") String password,
			@Value("${spring.datasource.driver-class-name}") String driverClassName) {

		SubmissionDao.setUrl(url);
		SubmissionDao.setUname(username);
		SubmissionDao.setPass(password);
		SubmissionDao.setDriverClassName(driverClassName);

	}

	/**
	 * This method will ensure that only one single instance of the DAO is created
	 *
	 * @return single instance of a SubmissionDAO
	 */
	public static SubmissionDao getInstance() {
		if (instance == null) {
			instance = new SubmissionDao();

		}
		return instance;
	}

	/**
	 * Empty constructor for Submission Dao
	 */
	private SubmissionDao() {

	}

	/**
	 * Method to find a list of Submission given an array of submission ids as input
	 *
	 * @param submissions an array of integers
	 * @return a list of submissions
	 */
	public List<Submission> findSubmissions(Integer[] submissions) {
		List<Submission> subs = new ArrayList<Submission>();
		try {
			Class.forName(driverClassName);
			String sql = String.format(("SELECT * from submission where id IN (%s)"),
					String.join(",", Collections.nCopies(submissions.length, "?")));

			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(sql)) {

				for (int i = 0; i < submissions.length; i++) {
					statement.setObject(i + 1, submissions[i]);
				}

				try (ResultSet results = statement.executeQuery();) {
					while (results.next()) {

						subs.add(mapResults(results));

					}
				}
			}
		} catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());
		}
		return subs;
	}


	/**
	 * Method to find the list of submissions under an  Assignment id.
	 *
	 * @param id is the assignment id
	 * @return a list of Submission
	 */
	public List<Submission> findSubmissionsByAssignmentId(int id) {
		List<Submission> subs = new ArrayList<Submission>();

		try {
			Class.forName(driverClassName);
			String sql = "SELECT * from submission where assignmentid=?";

			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setInt(1, id);


				 //Execution of the sql query

				try (ResultSet result = statement.executeQuery();) {
					while (result.next()) {
						subs.add(mapResults(result));
					}
				}
			}

		} catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());
		}
		return subs;
	}


	/**
	 * Method to view the Submission History of a student
	 * @param studentid is an integer
	 * @param courseid is an integer
	 * @param assgid is an integer
	 * @return a List of Submission
	 */
	public List<Submission> viewSubmissionHistory(int studentid, int courseid, int assgid) {

		List<Submission> submissionHistory = new ArrayList<>();

		String sql = "Select * from submission where studentid=? and courseid=? and assignmentid=?";

		try (Connection connection = DriverManager.getConnection(url, uname, pass);
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, studentid);
			statement.setInt(2, courseid);
			statement.setInt(3, assgid);

			//Execution of the sql query
			try (ResultSet rs = statement.executeQuery();) {
				while (rs.next()) {
					Submission a = new Submission();
					a.setAssignmentid(rs.getInt("assignmentid"));
					a.setcourseid(rs.getInt("courseid"));
					a.setsubmissiondesc(rs.getString("submissiondesc"));
					a.setsubmissionlink(rs.getString("submissionlink"));
					a.setsubmissionname(rs.getString("submissionname"));
					a.setstudentid(rs.getInt("studentid"));
					submissionHistory.add(a);
				}
				
			} 
		} catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());

		}

		return submissionHistory;

	}

	/**
	 * Method to Submit an Assignment
	 * @param s is the Submission
	 * @param studentid is an integer
	 * @param courseid is an integer
	 * @param assigid is an integer
	 * @return an int 1 if successful else 0
	 */
	public int submitAssignment(Submission s, int studentid, int courseid, int assigid) {

		
		int result=0;
		try {
			Class.forName(driverClassName);

			String insertSql = "INSERT INTO submission (studentid,submissionname,courseid,submissionlink,submissiondesc,assignmentid,studentname)"
					+ " VALUES (?,?,?,?,?,?,?)";

			//Execution of the sql query
			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(insertSql)) {

				statement.setInt(1, studentid);
				statement.setString(2, s.getsubmissionname());
				statement.setInt(3, courseid);
				statement.setString(4, s.getsubmissionlink());
				statement.setString(5, s.getsubmissiondesc());
				statement.setInt(6, assigid);
				statement.setString(7, s.getstudentname());
				result = statement.executeUpdate();

			}

		} catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());
		}
		return result;

	}

	/**
	 * Method to get all Submissions by all students for an assignment
	 * @param courseid is an integer
	 * @param assignmentid is an integer
	 * @return a List of Submissions
	 */
	public List<Submission> getAllSubmissionsByAllStudents(int courseid, int assignmentid) {

		List<Submission> allSubmissions = new ArrayList<>();

		try {
			String sql = "Select * from submission where courseid=? and assignmentid=?";
			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(sql)) {

				statement.setInt(1, courseid);
				statement.setInt(2, assignmentid);

				//Execution of the sql query
				try (ResultSet rs = statement.executeQuery();) {

					while (rs.next()) {
						Submission s = new Submission();
						s.setAssignmentid(assignmentid);
						s.setcourseid(courseid);
						s.setstudentid(rs.getInt("studentid"));
						s.setsubmissionname(rs.getString("submissionname"));
						s.setsubmissiondesc(rs.getString("submissiondesc"));
						s.setsubmissionlink(rs.getString("submissionlink"));
						s.setid(rs.getInt("id"));
						s.setstudentname(rs.getString("studentname"));
						allSubmissions.add(s);
					}

				} 
				
			} 
			
		} catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());

		}
		return allSubmissions;
	}


	/**
	 * Get latest submissions for students that are to be scanned.
	 * @return a List of Submissions
	 */
	public List<Submission> getLatestUnscannedSubmissions() {
		List<Submission> allSubmissions = new ArrayList<>();
		String sql = "select * from submission where id in " +
				"(select max(id) From submission s where not exists " +
				"(select 1 from submissionscore sc where sc.assignmentid=s.assignmentid " +
				"and (s.id <= sc.submissionid1 or s.id <= sc.submissionid2))" +
				"GROUP BY courseid,assignmentid,studentid)";

		//Execution of the sql query
		try (Connection connection = DriverManager.getConnection(url, uname, pass);
			 PreparedStatement statement = connection.prepareStatement(sql);
			 ResultSet rs = statement.executeQuery()) {
			while (rs.next()) {
				allSubmissions.add(mapResults(rs));
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());

		}
		return allSubmissions;
	}

	/**
	 * Method to get all Latest Assignment Submissions
	 * @param assignmentId is an integer
	 * @return a list of Submissions
	 */
	public List<Submission> getLatestAssignmentSubmissions(int assignmentId) {
		List<Submission> submissions = new ArrayList<>();
		String sql = "select * From submission where id in " +
				"(select max(id) from submission where assignmentid=? GROUP BY studentid)";

		//Execution of the sql query
		try (Connection connection = DriverManager.getConnection(url, uname, pass);
			 PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, assignmentId);
			try (ResultSet rs = statement.executeQuery()) {
				while (rs.next()) {
					submissions.add(mapResults(rs));
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());

		}
		return submissions;
	}

	/**
	 * Get all already scanned submissions.
	 * @return a list of Submissions
	 */
	public List<Submission> getAllScannedSubmissions() {
		List<Submission> allSubmissions = new ArrayList<>();
		String sql = "select * from submission where " +
				"  id in (select submissionid1 from submissionscore) " +
				"  or id in (select submissionid2 from submissionscore)";

		//Execution of the sql query
		try (Connection connection = DriverManager.getConnection(url, uname, pass);
			 PreparedStatement statement = connection.prepareStatement(sql);
			 ResultSet rs = statement.executeQuery()) {
			while (rs.next()) {
				allSubmissions.add(mapResults(rs));
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());

		}
		return allSubmissions;
	}



	/**
	 * Returns submissions within an assignment having violation score > threshold.
	 *
	 * @param assignmentId is an integer
	 * @param thresholdPercent is an integer
	 * @return submissions as a list
	 */
	public List<Submission> getSubmissionsWithViolations(int assignmentId, int thresholdPercent) {
		List<Submission> subs = new ArrayList<Submission>();

		try {
			Class.forName(driverClassName);
			String sql = "SELECT DISTINCT s.* from assignment a inner join submission s on a.id=s.assignmentid " +
					" inner join submissionscore sc on s.id=sc.submissionid1 or s.id=sc.submissionid2" +
					" where a.id=? and sc.maxscore >= ? ";

			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setInt(1, assignmentId);
				statement.setInt(2, thresholdPercent);

				 // Excecution of the sql query

				try (ResultSet result = statement.executeQuery();) {
					while (result.next()) {
						subs.add(mapResults(result));
					}
				}
			}

		} catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());
		}
		return subs;
	}

	/**
	 * Method to remove a Submission
	 * @param studentid the studentid whose submission we need to remove 
	 * @param courseid the course student is in 
	 * @param assignmentid the assignment for which we need to delete the submission 
	 * @return 1 if the remove is succcessfull 
	 */
	public int removeSubmission(int studentid , int courseid,int assignmentid) {

		int result=0;
		try {
			Class.forName(driverClassName);

			String deletesql="Delete from submission where studentid=? and courseid=? "
					+ "and assignmentid=?";

			// Excecution of the sql query
			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(deletesql)) {

				statement.setInt(1, studentid);
				statement.setInt(2, courseid);
				statement.setInt(3, assignmentid);

				result=statement.executeUpdate();

			}

		} catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());
		}
		return result;
	}




	/**
	 * Method to map the result set into a Submission
	 *
	 * @param resultSet is the Result set
	 * @return Submission
	 * @throws SQLException if fails
	 */
	private Submission mapResults(ResultSet resultSet) throws SQLException {
		Submission submission;
		int id = resultSet.getInt("id");
		int studentid = resultSet.getInt("studentid");
		String submissionname = resultSet.getString("submissionname");
		int courseid = resultSet.getInt("courseid");
		String link = resultSet.getString("submissionLink");
		String submissiondesc = resultSet.getString("submissiondesc");
		int assignmentid = resultSet.getInt("assignmentid");

		submission = new Submission(id, studentid, submissionname, courseid, link, submissiondesc, assignmentid);
		return submission;
	}




}








