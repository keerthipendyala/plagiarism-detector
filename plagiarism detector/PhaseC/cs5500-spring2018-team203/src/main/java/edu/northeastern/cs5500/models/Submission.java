package edu.northeastern.cs5500.models;
/**
 * @author anju
 * Creats a submission with submission information
 */

public class Submission {

    private int id;
    private int studentid;
    private String submissionName;
    private int courseid;
    private String submissionlink;
    private String submissiondesc;
    private int assignmentid;
    private String studentname;


    /**
     * Constructor for Submission
     * @param id is an integer
     * @param studentid is an integer
     * @param submissionname is a string
     * @param courseid is an integer
     * @param submissionlink is a String
     * @param submissiondesc is a String
     * @param assignmentid is an integer
     */
    public Submission(int id, int studentid, String submissionname, int courseid, String submissionlink, String submissiondesc, int assignmentid) {
        this.id = id;
        this.studentid = studentid;
        this.submissionName = submissionname;
        this.courseid = courseid;
        this.submissionlink = submissionlink;
        this.submissiondesc = submissiondesc;
        this.assignmentid= assignmentid;
    }

    /**
     * Empty constructor for Submission
     */
    public Submission() {
        super();
    }

    /**
     * Getter for id
     * @return an integer
     */
    public int getid() {
        return id;
    }

    /**
     * Getter for Assignment id
     * @return an integer
     */
    public int getAssignmentid() {
        return assignmentid;
    }

    /**
     * Setter for assignment id
     * @param assignmentid is an integer
     */
    public void setAssignmentid(int assignmentid) {
        this.assignmentid = assignmentid;
    }

    /**
     * Setter for id
     * @param id an integer
     */
    public void setid(int id) {
        this.id = id;

    }

    /**
     * Getter for student id
     * @return is integer
     */
    public int getstudentid() {
        return studentid;
    }

    /**
     * Setter for student id
     * @param studentid is an integer
     */
    public void setstudentid(int studentid) {
        this.studentid = studentid;
    }

    /**
     * Getter submission name
     * @return a string
     */
    public String getsubmissionname() {
        return submissionName;
    }

    /**
     * Setter for submission name
     * @param submissionname is a string
     */
    public void setsubmissionname(String submissionname) {
        this.submissionName = submissionname;
    }

    /**
     * Getter for course id
     * @return an integer
     */
    public int getcourseid() {
        return courseid;
    }

    /**
     * Setter for Course id
     * @param courseid is an integer
     */
    public void setcourseid(int courseid) {
        this.courseid = courseid;
    }

    /**
     * Getter for submission link
     * @return a string
     */
    public String getsubmissionlink() {
        return submissionlink;
    }

    /**
     * Setter for submission link
     * @param submissionlink is a string
     */
    public void setsubmissionlink(String submissionlink) {
        this.submissionlink = submissionlink;
    }

    /**
     * Getter for submission desc
     * @return a String
     */
    public String getsubmissiondesc() {
        return submissiondesc;
    }

    /**
     * Setter for submission description
     * @param submissiondesc is a string
     */
    public void setsubmissiondesc(String submissiondesc) {
        this.submissiondesc = submissiondesc;
    }

    /**
     * Getter for student name
     * @return a string
     */
    public String getstudentname() {
        return studentname;
    }

    /**
     * Setter for student name
     * @param studentname is a String
     */
    public void setstudentname(String studentname) {
        this.studentname = studentname;
    }
}
