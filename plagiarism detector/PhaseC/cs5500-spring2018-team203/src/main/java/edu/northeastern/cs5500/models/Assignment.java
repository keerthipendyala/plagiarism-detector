package edu.northeastern.cs5500.models;

/**
 * @author anju
 * Creates an assignment with assignment information.
 *
 */

public class Assignment {

	private int id;
	private String assignmentname;
	private int courseid;
	private String description;

	/**
	 * Constructor for Assignment Model
	 * @param id is an integer
	 * @param assignmentname is a String
	 * @param courseid is an integer
	 * @param description is a String
	 */
	public Assignment(int id, String assignmentname, int courseid,String description) {
		this.id = id;
		this.assignmentname = assignmentname;
		this.courseid = courseid;
		this.description=description;
	}

	/**
	 * Empty constructor for Assignment
	 */
	public Assignment() {

		
	}

	/**
	 * Getter method for id
	 * @return an integer as id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setter method for id
	 * @param id ia an integer
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Getter for assignment name
	 * @return a String
	 */
	public String getAssignmentname() {
		return assignmentname;
	}

	/**
	 * Setter for Assignment Name
	 * @param assignmentname is a string
	 */
	public void setAssignmentname(String assignmentname) {
		this.assignmentname = assignmentname;
	}

	/**
	 * Getter for Course id
	 * @return an integer
	 */
	public int getCourseid() {
		return courseid;
	}

	/**
	 * Setter for Course id
	 * @param courseid is an integer
	 */
	public void setCourseid(int courseid) {
		this.courseid = courseid;
	}

	/**
	 * Getter for Description
	 * @return a String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for Description
	 * @param description is a String
	 */
	public void setDescription(String description) {
		this.description = description;
	}






}


