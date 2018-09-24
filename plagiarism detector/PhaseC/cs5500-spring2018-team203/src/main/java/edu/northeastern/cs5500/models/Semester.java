package edu.northeastern.cs5500.models;

/**
 * This class will consist of PoJo for accessing semester table values 
 * @author karan sharma 
 * 
 *
 */
public class Semester {
	
	private int semesterid; 
	private String semestername;
	private String startdate;
	private String enddate;
	
	public Semester() {
		/**
		 * empty constructor 
		 */
	}
	
	/**
	 * get the id of semester 
	 * @return id 
	 */
	public int getSemesterid() {
		return semesterid;
	}
	
	/**
	 * set the id of semester 
	 * @param semesterid new semesterid 
	 */
	public void setSemesterid(int semesterid) {
		this.semesterid = semesterid;
	}
	
	/**
	 * get semester name 
	 * @return semestername 
	 */
	public String getSemestername() {
		return semestername;
	}
	
	/**
	 * set the semester name 
	 * @param semestername new Semestername 
	 */
	public void setSemestername(String semestername) {
		this.semestername = semestername;
	}
	
	/**
	 * get the start date of semester  
	 * @return start date (String) as MM/DD/YYYY format 
	 */
	public String getStartdate() {
		return startdate;
	}
	
	/**
	 * set startdate of the semester
	 * @param startdate in DD/MM/YYYY format 
	 */
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	/**
	 * get the enddate of the semester 
	 * @return String enddate 
	 */
	public String getEnddate() {
		return enddate;
	}
	
	/**
	 * set the end date of the semester 
	 * @param enddate new enddate in DD/MM/YYYY
	 */
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	
	
	
	

}
