package edu.northeastern.cs5500.service;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.cs5500.daos.PendingTableDao;
import edu.northeastern.cs5500.daos.UserDao;
import edu.northeastern.cs5500.models.User;

/**
 * this Class is used to handle all admin related functionalities
 * @author harip
 *
 */
@RestController
public class AdminService {
	
	/**
	 * this method retrieves all the user from the database
	 * @return collection of user objects
	 */
	@RequestMapping(value="/api/getAllUsers", method={ RequestMethod.GET})
	public List<User> getAllUsers() {
		PendingTableDao dao=PendingTableDao.getInstance();
		return dao.getAllUsers();
		
	}

	/**
	 * this method is for changing the roles of the user by admin
	 * @param userid is the unique id of the user
	 * @param newrole is the new role for the user
	 * @return
	 */
	@RequestMapping(value="/api/changeRole/{userId}/{newrole}", method={ RequestMethod.POST})
	public int changeRole(@PathVariable(name="userId") int userid, @PathVariable(name="newrole") String newrole) {
		UserDao dao=UserDao.getInstance();
		return dao.changeRole(userid, newrole);
	}
	
	
}
