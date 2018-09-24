package edu.northeastern.cs5500.models;

/**
 * 
 * This class will contain PoJo for inserting records to User-Professor
 * @author karan sharma 
 *
 */
public class Professor extends User{

	/**
	 * default serial id 
	 */
	private static final long serialVersionUID = 1L;

	private int profid;
	private int userid;
	private String title; 
	private String degree; 
	private String department;
	
	/**
	 * 
	 * @param userid the id of the user to be created 
	 * @param username the username of the user 
	 * @param password password of the user 
	 * @param fullname fullname of the user 
	 * @param professorid id of the professor 
	 * @param userid2 a foreign key userid in professor table 
	 * @param title of professor 
	 * @param degree of professor 
	 * @param department of professor 
	 */
	public Professor(int userid, String username, String password, String fullname, int professorid, int userid2,
			String title, String degree, String department) {
		super(userid, username, password, fullname);
		this.profid = professorid;
		userid = userid2;
		this.title = title;
		this.degree = degree;
		this.department = department;
	}

	/**
	 * Empty constructor for professor class
	 */
	public Professor() {
		
	}

	/**
	 * get id of professor 
	 * @return id 
	 */
	public int getProfessorid() {
		return profid;
	}
	
	/**
	 * set id of professor 
	 * @param professorid newid 
	 */
	public void setProfessorid(int professorid) {
		this.profid = professorid;
	}
	
	/**
	 * get userid of the professor 
	 */
	@Override
	public int getUserid() {
		return userid;
	}
	
	/**
	 * set userid of the professor 
	 */
	@Override
	public void setUserid(int userid) {
		this.userid = userid;
	}
	
	/**
	 * Getter for tilte
	 * @return a string
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * set title of professor 
	 * @param title new title 
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * get degree of professr 
	 * @return degree
	 */
	public String getDegree() {
		return degree;
	}

	/**
	 * set degree of professor 
	 * @param degree new degree 
	 */
	public void setDegree(String degree) {
		this.degree = degree;
	}
	
	/**
	 * get department of professor 
	 * @return department 
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * set department of professor 
	 * @param department new department 
	 */
	public void setDepartment(String department) {
		this.department = department;
	} 
	
	
}

