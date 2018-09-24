package edu.northeastern.cs5500.jgit;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;


/**
 * 
 * @author karan sharma 
 * @desc This class will have JGit functionalities to clone git repos, and delete them 
 *
 */
public class JavaGit {

	private static Logger logger = Logger.getLogger(JavaGit.class.getName());
	
	/**
	 * get the temp directory location for cloning 
	 */
	public static final String TMP_DIR = System.getProperty("java.io.tmpdir");
	
	/**
	 * 
	 * @param repoUri the git clone url  
	 * @param studentstring the string of student which contains all the details in order 
	 * @param assignmentname the name of the assignment for comparison 
	 */
	public void cloneRepository(String repoUri,String studentstring, String assignmentname) {
		
		logger.log(Level.INFO, TMP_DIR);
		
		File clonePath=new File(TMP_DIR+File.separator+"jplag"+File.separator+assignmentname+File.separator+"student"+studentstring);
		try {
		/**
		 * clone the git repository using the clone uri, clone at the directory 
		 * clonepath and as JGit is lazy evaluation, use an action over it which is 
		 * call. Once cloned, we need to close the JGit's object for that repo otherwise 
		 * we wont be able to clone other repositories in the same folder   
		 */
			Git git = Git.cloneRepository()
					  .setURI( repoUri )
					  .setDirectory(clonePath)
					  .call();
		git.getRepository().close();
		git.close();
		
		} catch (GitAPIException e) {
			logger.log(Level.INFO, e.getMessage());
			
		}
		
	}


	/**
	 * 
	 * @param path the path where we need to delete the cloned repositories 
	 */
	public void deleteJplag(String path) {

		try {
			/**
			 * Get the static method of FileUtils for deleteDirectory at the location 
			 * path 
			 */
			FileUtils.deleteDirectory(new File(path));
		} catch (IOException e) {

			logger.log(Level.INFO, e.getMessage());
		}

	}

	
}
