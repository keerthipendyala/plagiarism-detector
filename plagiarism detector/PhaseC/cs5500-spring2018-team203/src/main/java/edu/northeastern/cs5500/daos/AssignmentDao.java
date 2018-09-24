package edu.northeastern.cs5500.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import edu.northeastern.cs5500.models.Assignment;
import edu.northeastern.cs5500.models.SubmissionSummary;

/**
 * @author anju
 * @author karan sharma
 * @desc This DAO will contain Data access objects that will perform various CRUD operations
 */

@Repository
public class AssignmentDao {
    private static String url;
    private static String uname;
    private static String pass;
    private static String driverClassName;
    private static AssignmentDao instance = null;
    Logger logger = Logger.getLogger(AssignmentDao.class.getName());

    /**
     * Getter method for url
     *
     * @return a string
     */
    public static String getUrl() {
        return url;
    }

    /**
     * Setter method for url
     *
     * @param url is a string
     */
    public static void setUrl(String url) {
        AssignmentDao.url = url;
    }

    /**
     * Getter method for username
     *
     * @return a string
     */
    public static String getUname() {
        return uname;
    }

    /**
     * Setter method for username
     *
     * @param uname is a string
     */
    public static void setUname(String uname) {
        AssignmentDao.uname = uname;
    }

    /**
     * Getter method for password
     *
     * @return a string
     */
    public static String getPass() {
        return pass;
    }

    /**
     * Setter method for password
     *
     * @param pass is a string
     */
    public static void setPass(String pass) {
        AssignmentDao.pass = pass;
    }

    /**
     * Getter method for driver class name
     *
     * @return as string
     */
    public static String getDriverClassName() {
        return driverClassName;
    }

    /**
     * Setter method for driver class name
     *
     * @param driverClassName is a string
     */
    public static void setDriverClassName(String driverClassName) {
        AssignmentDao.driverClassName = driverClassName;
    }


    /**
     * @param url             the url of the database application
     * @param username        the username of the database
     * @param password        the password of the database
     * @param driverClassName the className of the MYSQLdriver
     */
    @Autowired
    AssignmentDao(@Value("${spring.datasource.url}") String url,
                  @Value("${spring.datasource.username}") String username,
                  @Value("${spring.datasource.password}") String password,
                  @Value("${spring.datasource.driver-class-name}") String driverClassName) {

        AssignmentDao.setUrl(url);
        AssignmentDao.setUname(username);
        AssignmentDao.setPass(password);
        AssignmentDao.setDriverClassName(driverClassName);

    }

    /**
     * Empty constructor for Assignment Dao
     */
    private AssignmentDao() {

    }

    /**
     * Singleton instance
     *
     * @return an instance of AssignmentDao
     */
    public static AssignmentDao getInstance() {
        if (instance == null) {
            instance = new AssignmentDao();

        }
        return instance;
    }


    /**
     * Get all assignment for course given the course id
     *
     * @param courseid is an integer
     * @return a list of Assignment
     */
    public List<Assignment> getAllAssignments(int courseid) {

        List<Assignment> allAssigns = new ArrayList<>();
        try {
            Class.forName(driverClassName);
            String getAllAssig = "select * from assignment where courseid=?";

            try (Connection connection = DriverManager.getConnection(url, uname, pass);
                 PreparedStatement statement = connection.prepareStatement(getAllAssig)) {

                statement.setInt(1, courseid);

                try (ResultSet rs = statement.executeQuery();) {
                    while (rs.next()) {
                        Assignment a = new Assignment();
                        a.setAssignmentname(rs.getString("assignmentname"));
                        a.setCourseid(rs.getInt("courseid"));
                        a.setDescription(rs.getString("description"));
                        a.setId(rs.getInt("id"));
                        allAssigns.add(a);
                    }
                }

            }
        } catch (Exception e) {
            logger.log(Level.INFO, e.getMessage());

        }
        return allAssigns;

    }


    /**
     * Create new assignment for course.
     *
     * @param courseid is an integer
     * @param newassg  is an Assignment
     * @return an integer
     */
    public int createAssignment(int courseid, Assignment newassg) {

        int result = 0;
        try {

            Class.forName(driverClassName);
            String sql = "INSERT INTO assignment(assignmentname,courseid,description)VALUES (?,?,?)";
            try (Connection connection = DriverManager.getConnection(url, uname, pass);
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, newassg.getAssignmentname());
                statement.setInt(2, courseid);
                statement.setString(3, newassg.getDescription());
                result = statement.executeUpdate();

            }

        } catch (Exception e) {
            logger.log(Level.INFO, e.getMessage());

        }
        return result;
    }


    /**
     * Method to get the professor email id
     *
     * @param courseid is an integer
     * @return email as string
     */
    public String findProfessor(int courseid) {
        String emailIds = "";

        try {
            Class.forName(driverClassName);
            String sql = "select u.email from user as u inner join (select * from courses as c where c.courseid=?)"
                    + "as p, professor as pr where p.profid=pr.profid and pr.userid=u.userid";

            try (Connection connection = DriverManager.getConnection(url, uname, pass);
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, courseid);

                try (ResultSet result = statement.executeQuery();) {
                    while (result.next()) {
                        emailIds = result.getString("email");
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.INFO, e.getMessage());

        }
        return emailIds;
    }


    /**
     * Method to get the student details given a list of submission ids.
     *
     * @param submissionIds is a list of integers
     * @return a map of student id as key and student name as value
     */
    public Map<Integer, String> getStudentsFromSubmissionIds(List<Integer> submissionIds) {
        HashMap<Integer, String> students = new HashMap<>();
        try {
            Class.forName(driverClassName);
            String sql = String.format(("SELECT sub.id, u.fullname FROM user u inner join student stu on " +
                    "u.userid = stu.userid inner join submission sub on sub.studentid = stu.studentid " +
                    "where sub.id IN (%s)"), String.join(",", Collections.nCopies(submissionIds.size(), "?")));

            try (Connection connection = DriverManager.getConnection(url, uname, pass);
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                for (int i = 0; i < submissionIds.size(); i++) {
                    statement.setObject(i + 1, submissionIds.get(i));
                }

                try (ResultSet result = statement.executeQuery();) {
                    while (result.next()) {
                        students.put(result.getInt("id"), result.getString("fullname"));
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.INFO, e.getMessage());

        }
        return students;
    }

    /**
     * Delete assignment with given name
     *
     * @param assignmentname is a string
     * @return an integer
     */
    public int deleteAssignment(String assignmentname) {

        int result = 0;
        try {
            Class.forName(driverClassName);
            String sql = "DELETE from assignment where assignmentname=?";
            try (Connection connection = DriverManager.getConnection(url, uname, pass);
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, assignmentname);
                result = statement.executeUpdate();
            }

        } catch (Exception e) {
            logger.log(Level.INFO, e.getMessage());
        }

        return result;
    }


    /**
     * Method to delete assignment for a course
     *
     * @param courseid the course where assignment exists
     * @param aid      the id of assignment to delete
     * @return 1 if delete is successful, 0 if not
     */
    public int deleteAssignmentForACourse(int courseid, int aid) {

        int result = 0;
        try {
            Class.forName(driverClassName);
            String delSql = "DELETE from assignment where id=?";
            try (Connection connection = DriverManager.getConnection(url, uname, pass);
                 PreparedStatement statement = connection.prepareStatement(delSql)) {

                statement.setInt(1, aid);

                result = statement.executeUpdate();
            }

        } catch (Exception e) {
            logger.log(Level.INFO, e.getMessage());
        }

        return result;

    }


    /**
     * Method to update assignment
     *
     * @param assg         is an Assignment
     * @param assignmentid is an integer
     * @return an intger to show the status of success
     */
    public int updateAssignment(Assignment assg, int assignmentid) {

        int result = 0;
        try {
            Class.forName(driverClassName);
            String upSql = "UPDATE assignment SET assignmentname =?,courseid = ?,description = ? WHERE id = ?";
            try (Connection connection = DriverManager.getConnection(url, uname, pass);
                 PreparedStatement statement = connection.prepareStatement(upSql)) {


                statement.setString(1, assg.getAssignmentname());
                statement.setString(3, assg.getDescription());
                statement.setInt(2, assg.getCourseid());
                statement.setInt(4, assignmentid);
                result = statement.executeUpdate();
            }

        } catch (Exception e) {
            logger.log(Level.INFO, e.getMessage());
        }

        return result;

    }


    /**
     * Method to get the Assignment given the assignment id
     *
     * @param aid is the assignment id as an int
     * @return Assignment
     */
    public Assignment getAssignment(int aid) {

        Assignment a = new Assignment();
        try {
            Class.forName(driverClassName);
            String getAssignment = "Select * from assignment where id=?";
            try (Connection connection = DriverManager.getConnection(url, uname, pass);
                 PreparedStatement statement = connection.prepareStatement(getAssignment)) {

                statement.setInt(1, aid);
                try (ResultSet rs = statement.executeQuery()) {

                    while (rs.next()) {
                        a.setAssignmentname(rs.getString("assignmentname"));
                        a.setCourseid(rs.getInt("courseid"));
                        a.setDescription(rs.getString("description"));
                        a.setId(rs.getInt("id"));

                    }


                }

            }


        } catch (Exception e) {
            logger.log(Level.INFO, e.getMessage());
        }

        return a;

    }

    /**
     * Submission summary for a given set of submissions
     *
     * @param submissionIds are List of Integers
     * @return a List of Submission Summary
     */
    public List<SubmissionSummary> getSubmissionSummary(List<Integer> submissionIds) {
        List<SubmissionSummary> submissionSummaries = new ArrayList<>();
        try {
            Class.forName(driverClassName);
            String sql = String.format(("select  u.fullname as studentname, coursename, courseno, " +
                            "semestername , assignmentname, c.courseid, " +
                            " a.id assignmentid, st.studentid, s.id submissionid from  submission s " +
                            " inner join courses c on s.courseid = c.courseid " +
                            "  inner join  student st ON s.studentid = st.studentid " +
                            "  inner join user u on st.userid = u.userid " +
                            "  inner join semester sem on c.semid= sem.semesterid " +
                            "  inner join assignment a on a.id = s.assignmentid where s.id in (%s)"),
                    String.join(",", Collections.nCopies(submissionIds.size(), "?")));
            try (Connection connection = DriverManager.getConnection(url, uname, pass);
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                for (int i = 0; i < submissionIds.size(); i++) {
                    statement.setObject(i + 1, submissionIds.get(i));
                }

                try (ResultSet result = statement.executeQuery();) {
                    while (result.next()) {
                        submissionSummaries.add(mapSubmissionSummary(result));
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return submissionSummaries;
    }

    /**
     * Submission summary for a given set of submissions
     *
     * @param userid is an integer
     * @return a List of Submission Summary
     */
    public List<SubmissionSummary> getSubmissionSummary(int userid) {
        List<SubmissionSummary> submissionSummaries = new ArrayList<>();
        try {
            Class.forName(driverClassName);
            String sql = "select u.fullname as studentname, coursename, courseno, semestername , assignmentname, " +
                    "  c.courseid, a.id assignmentid, st.studentid, s.id submissionid, " +
                    "  score.maxscore,score.averagescore,r.reportlink, r.id from  submission s " +
                    "  inner join courses c on s.courseid = c.courseid " +
                    "  inner join  student st ON s.studentid = st.studentid " +
                    "  inner join semester sem on c.semid= sem.semesterid " +
                    "  inner join assignment a on a.id = s.assignmentid " +
                    "  inner join user u on st.userid = u.userid " +
                    "  INNER join submissionscore score on score.scoreid = " +
                    " (select sc.scoreid from submissionscore sc where " +
                    "      (sc.submissionid1 = s.id or sc.submissionid2 = s.id) " +
                    "   and sc.maxscore >= c.thresholdvalue order by maxscore desc limit 1) " +
                    "  INNER join reports r on r.id = score.reportid" +
                    " inner join professor p ON c.profid = p.profid " +
                    " where p.userid = ? order by score.scoreid desc";
            try (Connection connection = DriverManager.getConnection(url, uname, pass);
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, userid);

                try (ResultSet result = statement.executeQuery();) {
                    while (result.next()) {
                        SubmissionSummary summary = mapSubmissionSummary(result);
                        summary.setMaxscore(result.getFloat("maxscore"));
                        summary.setAvgscore(result.getFloat("averagescore"));
                        summary.setReportlink(result.getString("reportlink"));
                        submissionSummaries.add(summary);
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return submissionSummaries;
    }

    /**
     * Method to map Submission Summary
     * @param result is a result set
     * @return Submission Summary
     * @throws SQLException if fails
     */
    private SubmissionSummary mapSubmissionSummary(ResultSet result) throws SQLException {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setStudentname(result.getString("studentname"));
        summary.setCoursename(result.getString("coursename"));
        summary.setCourseno(result.getString("courseno"));
        summary.setSemestername(result.getString("semestername"));
        summary.setAssignmentname(result.getString("assignmentname"));
        summary.setCourseid(result.getInt("courseid"));
        summary.setAssignmentid(result.getInt("assignmentid"));
        summary.setStudentid(result.getInt("studentid"));
        summary.setSubmissionid(result.getInt("submissionid"));
        return summary;
    }

}
