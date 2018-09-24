package edu.northeastern.cs5500.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import edu.northeastern.cs5500.models.SubmissionScore;

import java.util.HashMap;

/**
 * @author anju
 * @desc This DAO will contain Data access objects that will perform various CRUD operations
 */

@Repository
public class SubmissionScoreDao {
	private static String url;
	private static String uname;
	private static String pass;
	private static String driverClassName;
	private static SubmissionScoreDao instance = null;
	Logger logger = Logger.getLogger(SubmissionScoreDao.class.getName());

	/**
	 * Set url
	 *
	 * @param url
	 */
	public static void setUrl(String url) {
		SubmissionScoreDao.url = url;
	}


	/**
	 * Set user name
	 *
	 * @param uname
	 */
	public static void setUname(String uname) {
		SubmissionScoreDao.uname = uname;
	}


	/**
	 * Get pass.
	 *
	 * @param pass
	 */
	public static void setPass(String pass) {
		SubmissionScoreDao.pass = pass;
	}


	/**
	 * Set driver class name.
	 *
	 * @param driverClassName
	 */
	public static void setDriverClassName(String driverClassName) {
		SubmissionScoreDao.driverClassName = driverClassName;
	}


	/**
	 * @param url             the url of the database application
	 * @param username        the username of the database
	 * @param password        the password of the database
	 * @param driverClassName the className of the MYSQLdriver
	 */
	@Autowired
	SubmissionScoreDao(@Value("${spring.datasource.url}") String url,
			@Value("${spring.datasource.username}") String username,
			@Value("${spring.datasource.password}") String password,
			@Value("${spring.datasource.driver-class-name}") String driverClassName) {

		SubmissionScoreDao.setUrl(url);
		SubmissionScoreDao.setUname(username);
		SubmissionScoreDao.setPass(password);
		SubmissionScoreDao.setDriverClassName(driverClassName);

	}


	/**
	 * Private constructor.
	 */
	private SubmissionScoreDao() {

	}

	/**
	 * Method to ensure only one instance of SubmissionScoreDao is created.
	 *
	 * @return SubmissionScoreDao
	 */
	public static SubmissionScoreDao getInstance() {
		if (instance == null) {
			instance = new SubmissionScoreDao();
		}
		return instance;
	}



	/**
	 * 
	 * @param id the id of the element that we want to fetch 
	 * @param identifier can be submission or student (to fetch submissionname or studentname)
	 * @return the name of the element (either submission name or student name )
	 */
	public String getName(int id, String identifier) {

		String name="";
		/**
		 * if we want to get the submission name 
		 */
		if(identifier.equalsIgnoreCase("submission")) {

			/**
			 * select query to get the name of the submission 
			 */
			String getSubmisionsql="select submissionname from submission where id=?";

			try(Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement getQuery = connection.prepareStatement(getSubmisionsql)) {
				Class.forName(driverClassName);
				getQuery.setInt(1, id);

				try(ResultSet rs=getQuery.executeQuery();) {
					while(rs.next()) {
						name=rs.getString("submissionname");
					}
				}


			}

			catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage());

			}

		}

		else { // if its the student name that we want to retrieve 

			String getStudentsql="select fullname,email from user as u,student as s where u.userid=s.userid and "
					+ "s.studentid=?";

			/**
			 * get the connection paramters for JDBC connection 
			 *
			 */
			try(Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement getQuery = connection.prepareStatement(getStudentsql);) {
				Class.forName(driverClassName);
				getQuery.setInt(1, id);

				try(ResultSet rs=getQuery.executeQuery();){
					while(rs.next()) {
						name=rs.getString("fullname")+"_"+rs.getString("email");
						
					}
				}

			}

			catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage());

			}

		}

		return name;
	}


	/**
	 * Method to map the resultset into SubmissionScore.
	 *
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private SubmissionScore mapResults(ResultSet resultSet) throws SQLException {
		SubmissionScoreDao sdao=SubmissionScoreDao.getInstance();
		SubmissionScore subscore;
		int scoreid = resultSet.getInt("scoreid");
		float averagescore = resultSet.getFloat("averagescore");
		float maxcore = resultSet.getFloat("maxscore");
		int courseid = resultSet.getInt("courseid");
		int assignmentid = resultSet.getInt("assignmentid");
		int student1id = resultSet.getInt("student1id");
		int student2id = resultSet.getInt("student2id");
		int submissionid1 = resultSet.getInt("submissionid1");
		int submissionid2 = resultSet.getInt("submissionid2");
		int reportid = resultSet.getInt("reportid");


		subscore = new SubmissionScore(scoreid, averagescore, maxcore);
		subscore.setCourseid(courseid);
		subscore.setAssignmentid(assignmentid);
		subscore.setStudent1id(student1id);
		subscore.setStudent2id(student2id);
		subscore.setSubmissionid1(submissionid1);
		subscore.setSubmissionid2(submissionid2);
		subscore.setReportid(reportid);
		
		/**
		 * name email pair is sent for a particular student, seperate them by _to get their 
		 * seperate values 
		 */
		String[]nameemailpair1=sdao.getName(resultSet.getInt("student1id"), "student").split("_");
	
		subscore.setStudent1name(nameemailpair1[0]);
		subscore.setStudent1email(nameemailpair1[1]);
		
		String[]nameemailpair2=sdao.getName(resultSet.getInt("student2id"), "student").split("_");
		
		subscore.setStudent2name(nameemailpair2[0]);
		subscore.setStudent2email(nameemailpair2[1]);
		/**
		 * get the submissionname for student1, similarly next line for student2
		 */
		subscore.setSubmission1name(sdao.getName(resultSet.getInt("submissionid1"), "submission"));
		subscore.setSubmission2name(sdao.getName(resultSet.getInt("submissionid2"), "submission"));
		return subscore;

	}

	/**
	 * Method to create/update submission scores
	 *
	 * @param scores is a List of Submission Scores
	 */
	public void createSubmissionScore(List<SubmissionScore> scores) {
		try {
			Class.forName(driverClassName);

			String insertSql = "INSERT INTO submissionscore (averagescore,maxscore,courseid,assignmentid,student1id,student2id,submissionid1,submissionid2,reportid)VALUES (?,?,?,?,?,?,?,?,?)";

			//Execution of Query
			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement insertQuery = connection.prepareStatement(insertSql);) {
				for (SubmissionScore score : scores) {
					insertQuery.setInt(3, score.getCourseid());
					insertQuery.setDouble(1, score.getAveragescore());
					insertQuery.setDouble(2, score.getMaxscore());
					insertQuery.setDouble(4, score.getAssignmentid());
					insertQuery.setDouble(5, score.getStudent1id());
					insertQuery.setDouble(6, score.getStudent2id());
					insertQuery.setInt(7, score.getSubmissionid1());
					insertQuery.setInt(8, score.getSubmissionid2());
					insertQuery.setInt(9, score.getReportid());
					insertQuery.addBatch();
				}
				insertQuery.executeBatch();
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());

		}
	}


	/**
	 * Method to get All Submission Scores
	 * @param courseid the course id for which we need submission scores 
	 * @param assignmentid the assignment id for getting the submission scores 
	 * @return the List of submissionscore 
	 */
	public List<SubmissionScore> getAllSubmissionsScores(int courseid, int assignmentid) {

		List<SubmissionScore> allSubmissions = new ArrayList<>();
		SubmissionScoreDao sdao=SubmissionScoreDao.getInstance();

		/**
		 * Hashmap which will store the name email pair depending on studentid and will 
		 * fetch from hashmap if already present so that frequent calls to db can be avoided 
		 */
		HashMap<Integer,String[]> studentnames=new HashMap<>();
		/**
		 * Hashmap which will store the name  depending on submissionid and will 
		 * fetch from hashmap if already present so that frequent calls to db can be avoided 
		 */
		HashMap<Integer,String> submissionnames=new HashMap<>();
		

		try {
			/**
			 * select query to get the submission score details 
			 */
			String sql = "SELECT sc.*,r.reportlink FROM submissionscore sc inner join reports r on sc.reportid= r.id" +
					" where sc.courseid=? and sc.assignmentid=?";
			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement statement = connection.prepareStatement(sql)) {

				statement.setInt(1, courseid);
				statement.setInt(2, assignmentid);

				try (ResultSet rs = statement.executeQuery();) {

					while (rs.next()) {
						SubmissionScore s = new SubmissionScore();
						s.setAssignmentid(assignmentid);
						s.setCourseid(courseid);
						int student1id=rs.getInt("student1id");
						int student2id=rs.getInt("student2id");
						
						int submissionid1=rs.getInt("submissionid1");
						int submissionid2=rs.getInt("submissionid2");

						s.setStudent1id(student1id);
						s.setStudent2id(student2id);
						s.setSubmissionid1(submissionid1);
						s.setSubmissionid2(submissionid2);
						s.setMaxscore(rs.getFloat("maxscore"));
						s.setReportid(rs.getInt("reportid"));
						s.setAveragescore(rs.getFloat("averagescore"));
						s.setReportlink(rs.getString("reportlink"));
						/**
						 * if student name is present in hashmap, get it 
						 */
						if(studentnames.containsKey(student1id)){
						
							String[] s1name=studentnames.get(student1id);
							s.setStudent1name(s1name[0]);
							s.setStudent1email(s1name[1]);
						}
						/**
						 * its not present in hashmap, fetch from db and put in hashmap 
						 */
						else { 

							String[] s1name=sdao.getName(student1id, "student").split("_");
							s.setStudent1name(s1name[0]);
							s.setStudent1email(s1name[1]);
							studentnames.put(student1id, s1name);
						}

						if(studentnames.containsKey(student2id)){
							
							String[] s2name=studentnames.get(student2id);
							s.setStudent2name(s2name[0]);
							s.setStudent2email(s2name[1]);
							
						}
						else {

							String[] s2name=sdao.getName(student2id, "student").split("_");
							s.setStudent2name(s2name[0]);
							s.setStudent2email(s2name[1]);
							studentnames.put(student2id, s2name);
							
						}
						
						/**
						 * same as for student, if present in hashmap retrieve else put in hashmap 
						 */
						if(submissionnames.containsKey(submissionid1)){
							s.setSubmission1name(submissionnames.get(submissionid1));
						}
						else {

							String s1name=sdao.getName(submissionid1, "submission");
							s.setSubmission1name(s1name);
							submissionnames.put(submissionid1, s1name);
						}
						
						
						if(submissionnames.containsKey(submissionid2)){
							s.setSubmission2name(submissionnames.get(submissionid2));
						}
						else {

							String s2name=sdao.getName(submissionid2, "submission");
							s.setSubmission2name(s2name);
							submissionnames.put(submissionid2, s2name);
						}						
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
	 * This method will return the List of submissionscore values depending on submissionids 
	 * @param subids List of submissionids for which we need the scores 
	 * @return List of submissionscore values 
	 */
	
	public List<SubmissionScore> getIdSubmissionsScores(List<Integer> subids) {

		List<SubmissionScore> allSubmissions = new ArrayList<>();
		SubmissionScoreDao sdao=SubmissionScoreDao.getInstance();

		HashMap<Integer,String[]> studentnames=new HashMap<>();
		HashMap<Integer,String> submissionnames=new HashMap<>();
		StringBuilder sb=new StringBuilder();

		try {
			String sql = "SELECT sc.*,r.reportlink FROM submissionscore sc inner join reports r on sc.reportid= r.id" +
					" where sc.submissionid1 IN # OR sc.submissionid2 IN #";
			try (Connection connection = DriverManager.getConnection(url, uname, pass)) {
				
				if(!subids.isEmpty()) {
			
					int i=0;
					for(Integer id: subids) {
						if(i==0){
							sb.append("("+id);
							i+=1;
						} else {
							sb.append(","+id);
							i+=1;
							
						}
						
					}
					sb.append(")");
					
				}
				else {
					sb.append("(0)");
					
				}
					 

				sql=sql.replaceAll("#", sb.toString());
				
		
				try (PreparedStatement statement = connection.prepareStatement(sql);
						ResultSet rs = statement.executeQuery();) {

					while (rs.next()) {
						SubmissionScore s = new SubmissionScore();
						s.setAssignmentid(rs.getInt("assignmentid"));
						s.setCourseid(rs.getInt("courseid"));
						int student1id=rs.getInt("student1id");
						int student2id=rs.getInt("student2id");
						int submissionid1=rs.getInt("submissionid1");
						int submissionid2=rs.getInt("submissionid2");

						s.setStudent1id(student1id);
						s.setStudent2id(student2id);
						s.setSubmissionid1(submissionid1);
						s.setSubmissionid2(submissionid2);
						s.setMaxscore(rs.getFloat("maxscore"));
						s.setReportid(rs.getInt("reportid"));
						s.setAveragescore(rs.getFloat("averagescore"));
						s.setReportlink(rs.getString("reportlink"));
						/**
						 * fetch the name email pairs for student, else submissionnames for 
						 * submissionids 
						 */
						if(studentnames.containsKey(student1id)){
						
							String[] s1name=studentnames.get(student1id);
							s.setStudent1name(s1name[0]);
							s.setStudent1email(s1name[1]);
						}
						else {

							String[] s1name=sdao.getName(student1id, "student").split("_");
							s.setStudent1name(s1name[0]);
							s.setStudent1email(s1name[1]);
							studentnames.put(student1id, s1name);
						}

						if(studentnames.containsKey(student2id)){
							
							String[] s2name=studentnames.get(student2id);
							s.setStudent2name(s2name[0]);
							s.setStudent2email(s2name[1]);
							
						}
						else {

							String[] s2name=sdao.getName(student2id, "student").split("_");
							s.setStudent2name(s2name[0]);
							s.setStudent2email(s2name[1]);
							studentnames.put(student2id, s2name);
							
						}
						
						
						if(submissionnames.containsKey(submissionid1)){
							s.setSubmission1name(submissionnames.get(submissionid1));
						}
						else {

							String s1name=sdao.getName(submissionid1, "submission");
							s.setSubmission1name(s1name);
							submissionnames.put(submissionid1, s1name);
						}
						
						
						if(submissionnames.containsKey(submissionid2)){
							s.setSubmission2name(submissionnames.get(submissionid2));
						}
						else {

							String s2name=sdao.getName(submissionid2, "submission");
							s.setSubmission2name(s2name);
							submissionnames.put(submissionid2, s2name);
						}						
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
	 * Method to get all submission scores based on a report id
	 * @param reportid
	 * @return
	 */

	public List<SubmissionScore> getSubmissionScores(int reportid) {
		List<SubmissionScore> scorelist = new ArrayList<>();

		String sql = "select * from submissionscore where reportid=?";
		try (Connection connection = DriverManager.getConnection(url, uname, pass);
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, reportid);

			try (ResultSet result = statement.executeQuery();) {
				while (result.next()) {
					SubmissionScore sc = mapResults(result);
					scorelist.add(sc);
				}
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, e.getMessage());
		}

		return scorelist;
	}

}
