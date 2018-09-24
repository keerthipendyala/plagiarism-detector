package edu.northeastern.cs5500.models;

import java.io.Serializable;

/**
 * @author karan sharma 
 * @desc Creates a user object which contains user information 
 *
 */

public class User implements Serializable{
	
	/**
	 * default serial id 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Professor and student extended classes not integrated yet 
	 */
		private int userid; 
		
		private String username;
		private String password; 
		private String fullname;
		private String address; 
		private String dateofbirth;
		private String email; 
		private String phone;
		private String role; 
		private int flag;
		
		/**
		 * 
		 * @param userid userid of the user 
		 * @param username username of the user 
		 * @param password password of the user 
		 * @param fullname fullname of the user 
		 */
		public User(int userid, String username, String password, String fullname) {
			super();
			this.userid = userid;
			this.username = username;
			this.password = password;
			this.fullname = fullname;
			
		}
		
		/**
		 * 
		 * @param userid userid of the user 
		 * @param username username of the user 
		 * @param password password of the user 
		 * @param fullname fullname of the user 
		 * @param role role of the user 
		 */
		public User(int userid, String username, String password,String fullname,String role) {
			
			super();
			this.userid = userid;
			this.username = username;
			this.password = password;
			this.fullname = fullname;
			this.role=role;
		}
		
		/**
		 * 
		 * @param username of the user 
		 * @param fullname fullname of the user 
		 * @param userid userid of the user 
		 */
		public User(String username, String fullname,int userid) {
			this.username = username;
			this.fullname = fullname;
			this.userid=userid;
			
		}

		
		public User() {
			super();
		}

		/**
		 * Get the username from userobject 
		 * @return username of the user object 
		 */
		public String getUsername() {
			return username;
		}

		/**
		 *  Set the username of the user object 
		 * @param username set the username of the user 
		 */
		public void setUsername(String username) {
			this.username = username;
		}

		/**
		 * get the password of user object 
		 * @return get password of user object 
		 */
		public String getPassword() {
			return password;
		}

		/**
		 * set the password of user object 
		 * @param password
		 */
		public void setPassword(String password) {
			this.password = password;
		}

		/**
		 * get the fullname of the userobject 
		 * @return fullname 
		 */
		public String getFullname() {
			return fullname;
		}

		/**
		 * set fullname 
		 * @param fullname new fullname of user 
		 */
		public void setFullName(String fullname) {
			this.fullname = fullname;
		}

		/**
		 * get userid of the user object 
		 * @return userid 
		 */
		public int getUserid() {
			return userid;
		}
		
		/**
		 * new user id 
		 * @param userid int 
		 */
		public void setUserid(int userid ) {
			this.userid=userid;
		}
		
		/**
		 * get dateof birth of user 
		 * @return String 
		 */

		public String getDateofBirth() {
			return dateofbirth;
		}

		/**
		 * set date of birth of user 
		 * @param dateofBirth string 
		 */

		public void setDateofBirth(String dateofBirth) {
			this.dateofbirth = dateofBirth;
		}


		public String getRole() {
			return role;
		}


		public void setRole(String role) {
			this.role = role;
		}


		public String getEmail() {
			return email;
		}


		public void setEmail(String email) {
			this.email = email;
		}


		public int getFlag() {
			return flag;
		}


		public void setFlag(int flag) {
			this.flag = flag;
		}


		public String getAddress() {
			return address;
		}


		public void setAddress(String address) {
			this.address = address;
		}


		public String getPhone() {
			return phone;
		}


		public void setPhone(String phone) {
			this.phone = phone;
		}


		
		
	
}
