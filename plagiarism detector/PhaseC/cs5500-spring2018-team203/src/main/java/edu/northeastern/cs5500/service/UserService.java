package edu.northeastern.cs5500.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.cs5500.daos.ReportDao;
import edu.northeastern.cs5500.daos.UserDao;
import edu.northeastern.cs5500.email.SendEmail;
import edu.northeastern.cs5500.models.Professor;
import edu.northeastern.cs5500.models.Student;
import edu.northeastern.cs5500.models.User;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

/**
 * @author karan sharma 
 * @desc This service is a rest controller which will delegate REST api calls to its respective daos 
 * 
 *
 */
@RestController
public class UserService {

	HttpSession session;
	Logger logger = Logger.getLogger(UserService.class.getName());

	public UserService() {


	}


	/**
	 * @param userId Contains the user ID that needs to be found 
	 * @return User found in the database 
	 */
	@RequestMapping(value="/api/findUser/{id}", method={ RequestMethod.GET})
	public User findUserById(@PathVariable(name="id") int userId) {
		UserDao dao = UserDao.getInstance();
		return dao.findUserById(userId);
	}


	/**
	 * @param user that we get from the form submitted from UI  
	 * 
	 * @return User found in the database 
	 */
	@RequestMapping(value="/api/login", method={ RequestMethod.POST})
	public User findUserUsernameAndPassword(@RequestBody User user,HttpSession session) {
		UserDao dao = UserDao.getInstance();
		User u=dao.findUser(user);
		if(u!=null){
			session.setAttribute("user", u);
		}
		return u;

	}

	/**
	 * 
	 * @param username the username of the user that we want to find 
	 * @return the user if found 
	 */
	@RequestMapping("/api/findUsername/{username}")
	public User findUserByUsername(@PathVariable(name="username") String username) {
		UserDao dao = UserDao.getInstance();
		/**
		 * find user by username and return the results 
		 * 
		 */
		return dao.findUserByUserName(username);
	}

	@RequestMapping(value="/api/getAllProfessors", method={ RequestMethod.GET})
	public List<User> getAllProfessors() {
		UserDao dao=UserDao.getInstance();
		return dao.getAllProfessors();

	}

	@RequestMapping(value="/api/getAllStudents", method={ RequestMethod.GET})
	public List<User> getAllStudents() {
		UserDao dao=UserDao.getInstance();
		return dao.getAllStudents();

	}

	/**
	 * 
	 * @param user The user that we want to create 
	 * @return 1 if the user is created in DB, 0 if there was an exception to create 
	 */
	@RequestMapping(value = "/api/register", method = RequestMethod.POST)
	public int createUser(@RequestBody User user) {

		UserDao dao = UserDao.getInstance();
		/**
		 * create the user 
		 */
		return dao.createUser(user);
	}

	@RequestMapping(value = "/api/findUserByIdAndRole/{userId}/{role}", method = RequestMethod.GET)
	public User findUserByIdAndRole(@PathVariable(name="userId") int userid,@PathVariable(name="role") String role) {
		UserDao dao=UserDao.getInstance();
		return dao.findUserByIdAndRole(userid, role);

	}


	/**
	 * this method is used to update student details
	 * @param student represents the student object
	 * @return
	 */
	@RequestMapping(value="api/updateStudent/{id}", method = RequestMethod.POST)
	public int updateStudent(@RequestBody Student student) {
		UserDao dao=UserDao.getInstance();
		return dao.updateStudent(student);

	}

	/**
	 * this method is used to update professor details
	 * @param prof represents the professor object
	 * @return
	 */
	@RequestMapping(value="api/updateProfessor/{id}", method = RequestMethod.POST)
	public int updateProfessor(@RequestBody Professor prof) {
		UserDao dao=UserDao.getInstance();
		return dao.updateProfessor(prof);

	}

	/**
	 * @param admin represents the admin object
	 * @return
	 */
	@RequestMapping(value="api/updateAdmin/{id}", method = RequestMethod.POST)
	public int updateAdmin(@RequestBody User admin) {
		UserDao dao=UserDao.getInstance();
		return dao.updateAdmin(admin);

	}

	/**
	 * this method is used to delete an user account from the database
	 * @param name represents the unique username for a user object
	 * @return
	 */
	@RequestMapping(value="api/deleteUser/{name}", method = RequestMethod.POST)
	public int deleteUser(@PathVariable(name="name") String name) {
		UserDao dao=UserDao.getInstance();
		return dao.deleteUser(name);

	}

	@RequestMapping(value="api/logout", method = RequestMethod.GET)
	public int logout(HttpSession session) {
		session.invalidate();
		return 1;
	}


	@RequestMapping(value="/api/isLoggedIn", method=RequestMethod.GET) 
	public User getLoggedIn(HttpSession session) {
		User u=(User)session.getAttribute("user");

		if(u!=null){
			return u;
		}
		User d=new User();
		d.setUserid(0);
		return d;

	}

	@RequestMapping(value="api/isProfessor", method=RequestMethod.GET) 
	public int isProfessor(HttpSession session) {
		User u=(User)session.getAttribute("user");
		if(u!=null && u.getRole().equalsIgnoreCase("professor")){
			return 1; 
		}

		return 0;

	}

	@RequestMapping(value="api/isStudent", method=RequestMethod.GET) 
	public int isStudent(HttpSession session) {
		User u=(User)session.getAttribute("user");
		if(u!=null && u.getRole().equalsIgnoreCase("student")){
			return 1; 
		}
		return 0;

	}

	@RequestMapping(value="api/isAdmin", method=RequestMethod.GET) 
	public int isAdmin(HttpSession session){
		User u=(User)session.getAttribute("user");
		if(u!=null && u.getRole().equalsIgnoreCase("admin")){
			return 1; 
		}
		return 0;

	}



	@RequestMapping(value="api/sendemail/{email1}/{email2}/{reportid}", method=RequestMethod.GET) 
	public int sendEmailToStudents(@PathVariable(name="email1") String email1,@PathVariable(name="email2") String email2
			,@PathVariable(name="reportid") int reportid){

		SendEmail se=SendEmail.getInstance();
		int status=0;
		String[] to={email1,email2};
		try{
			se.sendEmailToStudents(to, "Caught for plagiarism-Details inside", reportid);
			status=1;
		}
		catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());

		}

		return status;

	}


}	 

