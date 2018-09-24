package edu.northeastern.cs5500.models;

/**
 * Class used to represent submission summary
 */
public class SubmissionSummary {
    private String studentname;
    private String coursename;
    private String assignmentname;
    private String submissionname;
    private String courseno;
    private String semestername;
    private int submissionid;
    private int courseid;
    private int assignmentid;
    private int studentid;
    private float maxscore;
    private float avgscore;
    private String reportlink;

    /**
     * Getter for student name
     * @return String
     */
    public String getStudentname() {
        return studentname;
    }

    /**
     * Setter for student name
     * @param studentname as string
     */
    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    /**
     * Getter for course name
     * @return a string
     */
    public String getCoursename() {
        return coursename;
    }

    /**
     * Setter for course name
     * @param coursename is a string
     */
    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    /**
     * Getter for assignment name
     * @return a string
     */
    public String getAssignmentname() {
        return assignmentname;
    }

    /**
     * Setter for assignment name
     * @param assignmentname is a string
     */
    public void setAssignmentname(String assignmentname) {
        this.assignmentname = assignmentname;
    }

    /**
     * Setter for submission name
     * @return a string
     */
    public String getSubmissionname() {
        return submissionname;
    }

    /**
     * Setter for submission name
     * @param submissionname is a string
     */
    public void setSubmissionname(String submissionname) {
        this.submissionname = submissionname;
    }

    /**
     * Setter for course no
     * @return a string
     */
    public String getCourseno() {
        return courseno;
    }

    /**
     * Setter for course no
     * @param courseno a string
     */
    public void setCourseno(String courseno) {
        this.courseno = courseno;
    }

    /**
     * Getter for semester name
     * @return is a string
     */
    public String getSemestername() {
        return semestername;
    }

    /**
     * Setter for semester name
     * @param semestername is a string
     */
    public void setSemestername(String semestername) {
        this.semestername = semestername;
    }

    /**
     * Getter for submission id
     * @return a submission id as int
     */
    public int getSubmissionid() {
        return submissionid;
    }

    /**
     * Setter for submission id
     * @param submissionid a int
     */
    public void setSubmissionid(int submissionid) {
        this.submissionid = submissionid;
    }

    /**
     * Getter for course id
     * @return an int
     */
    public int getCourseid() {
        return courseid;
    }

    /**
     * Setter for course id
     * @param courseid is an int
     */
    public void setCourseid(int courseid) {
        this.courseid = courseid;
    }

    /**
     * Getter for assignment id
     * @return an int
     */
    public int getAssignmentid() {
        return assignmentid;
    }

    /**
     * Setter for assignment id
     * @param assignmentid is an int
     */
    public void setAssignmentid(int assignmentid) {
        this.assignmentid = assignmentid;
    }

    /**
     * Getter for student id
     * @return is int
     */
    public int getStudentid() {
        return studentid;
    }

    /**
     * Setter for student id
     * @param studentid is int
     */
    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    /**
     * Getter for max score
     * @return a float
     */
    public float getMaxscore() {
        return maxscore;
    }

    /**
     * Setter for maxscore
     * @param maxscore is a float
     */
    public void setMaxscore(float maxscore) {
        this.maxscore = maxscore;
    }

    /**
     * Getter for avg score
     * @return a float
     */
    public float getAvgscore() {
        return avgscore;
    }

    /**
     * Setter for avg score
     * @param avgscore is a float
     */
    public void setAvgscore(float avgscore) {
        this.avgscore = avgscore;
    }

    /**
     * Getter for report link
     * @return a string
     */
    public String getReportlink() {
        return reportlink;
    }

    /**
     * Setter for report link
     * @param reportlink is a string
     */
    public void setReportlink(String reportlink) {
        this.reportlink = reportlink;
    }
}
