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

import edu.northeastern.cs5500.models.Report;

/**
 * @author anju
 * @desc This DAO will contain Data access objects that will perform various CRUD operations
 */

@Repository
public class ReportDao {
	private static String url;
	private static String uname;
	private static String pass;
	private static String driverClassName;
	private static ReportDao instance = null;
	Logger logger = Logger.getLogger(ReportDao.class.getName());


	/**
	 * Setter method for url
	 *
	 * @param url is a string
	 */
	public static void setUrl(String url) {
		ReportDao.url = url;
	}


	/**
	 * Setter method for username
	 *
	 * @param uname is a string
	 */
	public static void setUname(String uname) {
		ReportDao.uname = uname;
	}



	/**
	 * Setter method for password
	 *
	 * @param pass is a string
	 */
	public static void setPass(String pass) {
		ReportDao.pass = pass;
	}


	/**
	 * Setter method for driver class name
	 *
	 * @param driverClassName is a string
	 */
	public static void setDriverClassName(String driverClassName) {
		ReportDao.driverClassName = driverClassName;
	}



	/**
	 * @param url             the url of the database application
	 * @param username        the username of the database
	 * @param password        the password of the database
	 * @param driverClassName the className of the MYSQLdriver
	 */
	@Autowired
	ReportDao(@Value("${spring.datasource.url}") String url,
			@Value("${spring.datasource.username}") String username,
			@Value("${spring.datasource.password}") String password,
			@Value("${spring.datasource.driver-class-name}") String driverClassName) {

		ReportDao.setUrl(url);
		ReportDao.setUname(username);
		ReportDao.setPass(password);
		ReportDao.setDriverClassName(driverClassName);

	}


	/**
	 * Empty constructor for Report Dao
	 */
	private ReportDao() {

	}

	/**
	 * Method to ensure only one instance of Report dao is created.
	 * @return ReportDao
	 */
	public static ReportDao getInstance() {
		if (instance == null) {
			instance = new ReportDao();

		}
		return instance;
	}

	/**
	 * Method to create a report
	 * @param report is a Report
	 * @return int 1 if successful
	 */
	public int createReport(Report report) {

		try {
			Class.forName(driverClassName);

			String insertReportSql = "INSERT INTO reports (reportid) VALUES (?)";
			String getReportSql = "SELECT * from reports where reportId=?";
			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement insertReport = connection.prepareStatement(insertReportSql);
					PreparedStatement getReport = connection.prepareStatement(getReportSql);) {
				insertReport.setLong(1, report.getReportId());
				/**
				 * Execute the insertion query
				 */
				insertReport.executeUpdate();

				/**
				 * Retrieving the report from database
				 */
				getReport.setLong(1, report.getReportId());

				try (ResultSet reportset = getReport.executeQuery();) {
					if (reportset.next()) {
						return reportset.getInt("id");
					}
				}

			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());


		}
		return -1;


	}


	/**
	 * Method to update a report after creation of report.
	 * @param report is a Report
	 * @return 1 if successful else 0
	 */
	public synchronized int updateReport(Report report) {
		int result = 0;

		try {
			Class.forName(driverClassName);
			String updateSql = "Update reports SET reportlink=?, successful=?,dummylink=? where id=?";


			try (Connection connection = DriverManager.getConnection(url, uname, pass);
					PreparedStatement updateReport = connection.prepareStatement(updateSql)) {

				updateReport.setString(1, report.getReportlink());
				updateReport.setBoolean(2, true);
				updateReport.setString(3, report.getDummylink());
				updateReport.setInt(4, report.getId());
				result = updateReport.executeUpdate();
			}

		} catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());
		}
		return result;

	}


	/**
	 * Method to get the report Link
	 * @param reportId the id of the report we want to get 
	 * @return the url of the report returned as String
	 */
	public String getReportLink(int reportId) {

		String getReportSql="Select reportlink from reports where id=?";
		String report="";
		try(Connection connection = DriverManager.getConnection(url, uname, pass);
				PreparedStatement getLink = connection.prepareStatement(getReportSql)){
			Class.forName(driverClassName);
			getLink.setInt(1, reportId);

			try(ResultSet rs=getLink.executeQuery()) {
				while(rs.next())
				report=rs.getString("reportLink");

			}

		}catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());
		}
		return report;

	}
	
	

	/**
	 * Method to get Dummy Link of the report
	 * @param reportId the id of the report we want to get 
	 * @return the url of the report 
	 */
	public String getDummyLink(int reportId) {

		String getReportSql="Select dummylink from reports where id=?";
		String report="";
		try(Connection connection = DriverManager.getConnection(url, uname, pass);
				PreparedStatement getLink = connection.prepareStatement(getReportSql)){
			Class.forName(driverClassName);
			getLink.setInt(1, reportId);

			try(ResultSet rs=getLink.executeQuery()) {
				while(rs.next())
				report=rs.getString("dummylink");

			}

		}catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());
		}
		return report;

	}
	
	
	
}
