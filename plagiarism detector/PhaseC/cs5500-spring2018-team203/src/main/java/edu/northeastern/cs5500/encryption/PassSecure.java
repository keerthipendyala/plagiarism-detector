package edu.northeastern.cs5500.encryption;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.mindrot.jbcrypt.BCrypt;

/**
 * 
 * @author karan sharma 
 * @desc Implementation of encrypting the password using BCrypt
 *
 */
public class PassSecure implements IPassSecure{

	private static PassSecure instance=null;
	/**
	 * set the workload for BCrypt's accuracy 
	 */
	private static int workload=10;
	/**
	 * get the logger for this class 
	 */
	Logger logger = Logger.getLogger(PassSecure.class.getName());
	public static PassSecure getInstance() {
		if(instance==null) {
			instance=new PassSecure();
		}
		return instance;
			
	}
	
	private PassSecure() {
		
	}
	

	/**
	 * encrypt the password using BCrypt
	 */
	@Override
	public String encrypt(String password) {
		logger.log(Level.INFO, "inside encrpt");
		return BCrypt.hashpw(password, BCrypt.gensalt(workload));
		
	}

	/**
	 * check if the hash of the password is same as the password 
	 * 
	 */
	@Override
	public boolean isPass(String password, String hash) {
				return BCrypt.checkpw(password, hash);
	}
	
	

}
