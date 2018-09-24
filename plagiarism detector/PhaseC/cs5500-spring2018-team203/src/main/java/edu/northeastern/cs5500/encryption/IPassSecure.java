package edu.northeastern.cs5500.encryption;

/**
 * 
 * @author karan sharma
 * @desc Interface to create PassSecure  
 *
 */
public interface IPassSecure {

	/**
	 * 
	 * @param password the password that we wish to encrpyt
	 * @return send encrypted password 
	 */
	public String encrypt(String password);
	/**
	 * 
	 * @param password get the password sent by the application 
	 * @param hash get the hash from database 
	 * @return true if the password's hash is the hash provided, else false  
	 */
	public boolean isPass(String password,String hash);
	
}
