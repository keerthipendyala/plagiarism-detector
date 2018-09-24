package edu.northeastern.cs5500.models;

/**
 * This class will contain PoJo for Course table 
 * @author karan sharma 
 *
 *
 */
public class Course {

	private String courseno;
	private String coursename;
	private String departmentname; 
	private int courseid;
	private int profid;
	private int semid;
	private int capacity;
	/**
	 * threshold value to be used for comparison of assignments for plagiarism 
	 */
	private int thresholdvalue;
	private String proglanguage;

	
	/**
	 * value is set to default 75%
	 */
	public Course() {
		this.thresholdvalue=75;
	}
	
	/**
	 * This method will return the courseno of the course object 
	 * @return String courseno
	 */
	public String getCourseno() {
		return courseno;
	}

	/**
	 * Setter for Course Number
	 * @param courseno is a String
	 */
	public void setCourseno(String courseno) {
		this.courseno = courseno;
	}
	
	/**
	 * This method will return the coursename of the course object 
	 * @return String coursename
	 */
	public String getCoursename() {
		return coursename;
	}

	/**
	 * Setter for Course name
	 * @param coursename is a String
	 */
	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}
	
	/**
	 * This method will return the departmentname of the course object 
	 * @return String departmentname 
	 */
	public String getDepartmentname() {
		return departmentname;
	}

	
	/**
	 * Set the departmentname of the course object 
	 * @param departmentname the name of the department 
	 */
	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}
	
	/**
	 * This method will return the courseid of the course object 
	 * @return int courseid
	 */
	public int getCourseid() {
		return courseid;
	}
	
	/**
	 * Set the courseid of the course object 
	 * @param courseid set the id of the course  
	 */
	public void setCourseid(int courseid) {
		this.courseid = courseid;
	}

	/**
<<<<<<< HEAD
	 * This method will return the professor id of the course object 
	 * @return int professor id who teaches that course 
	 */
	public int getProfid() {
		return profid;
	}

	/**
	 * Set the professor id for the course 
	 * @param profid the id of the professor 
	 */
	public void setProfid(int profid) {
		this.profid = profid;
	}

	/**
	 * Getter for Semester id
	 * @return the semester id for which the course was created 
	 */
	public int getSemid() {
		return semid;
	}

	/**
	 * Set the semester id of the professor 
	 * @param semid the semid 
	 */
	public void setSemid(int semid) {
		this.semid = semid;
	}

	/**
	 * Get the capacity of that course 
	 * @return int capacity 
	 */ 
	public int getCapacity() {
		return capacity;
	}

	/**
	 * set the capacity of students for the course 
	 * @param capacity int 
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * Get the threshold value of the course for comparison 
	 * @return int threshold value 
	 */
	public int getThreshold() {
		return thresholdvalue;
	}

	/**
	 * set the threshold value for the course for comparison 
	 * @param thresholdvalue int 
	 */
	public void setThreshold(int thresholdvalue) {
		this.thresholdvalue = thresholdvalue;
	}

	/**
	 * This method will get the programming language of the course object 
	 * @return String get the prog language of the course 
	 */
	public String getProglanguage() {
		return proglanguage;
	}

	/**
	 * set the programming language for plagiarism detection 
	 * @param proglanguage String 
	 */
	public void setProglanguage(String proglanguage) {
		this.proglanguage = proglanguage;
	}
}
