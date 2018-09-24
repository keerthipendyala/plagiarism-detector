package edu.northeastern.cs5500.models;

public class Student extends User {
	
	/**
	 * default serializable id 
	 */
	private static final long serialVersionUID = 1L;

	private int studentid;
	
	private int userid; 
	private String status; 
	private String dateofgraduation;
	
	public Student(int userid, String username, String password, String fullname, int studentid, int userid2,
			String status, String dateofgraduation) {
		
		super(userid, username, password, fullname);
		this.studentid = studentid;
		userid = userid2;
		this.status = status;
		this.dateofgraduation = dateofgraduation;
	}
	
	public Student() {
	
	}

	@Override
	public int getUserid() {
		return userid;
	}
	@Override
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDateofgraduation() {
		return dateofgraduation;
	}
	public void setDateofgraduation(String dateofgraduation) {
		this.dateofgraduation = dateofgraduation;
	}

	public int getStudentid() {
		return studentid;
	}

	public void setStudentid(int studentid) {
		this.studentid = studentid;
	} 

}
