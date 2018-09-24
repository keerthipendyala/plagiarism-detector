package edu.northeastern.cs5500.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.cs5500.daos.AssignmentDao;
import edu.northeastern.cs5500.daos.CourseDao;
import edu.northeastern.cs5500.daos.ReportDao;
import edu.northeastern.cs5500.daos.SubmissionDao;
import edu.northeastern.cs5500.daos.SubmissionScoreDao;
import edu.northeastern.cs5500.models.Submission;
import edu.northeastern.cs5500.models.SubmissionScore;
import edu.northeastern.cs5500.models.SubmissionSummary;
import edu.northeastern.cs5500.models.Url;

/**
 * @author anju
 * @desc Service for Submissions
 */
@RestController
public class SubmissionService {

	/**
	 * Method to get all submission history of a student given student id, course id and assignment id
	 * @return a list of submissions
	 */
	Logger logger = Logger.getLogger(SubmissionService.class.getName());
	@RequestMapping(value = "/api/getAllSubmission/{id}/{cid}/{aid}", method = {RequestMethod.GET})
	public List<Submission> getAllSubmissionHistory(@PathVariable(name = "id") int studentid,
			@PathVariable(name = "cid") int courseid,
			@PathVariable(name = "aid") int assgid) {

		SubmissionDao dao = SubmissionDao.getInstance();
		return dao.viewSubmissionHistory(studentid, courseid, assgid);


	}


	/**
	 * Method to submit an assignment
	 * @param sub is a Submission
	 * @return an int
	 */
	@RequestMapping(value = "/api/submitAssignment", method = {RequestMethod.POST})
	public int submitAssignment(@RequestBody Submission sub) {
		SubmissionDao dao = SubmissionDao.getInstance();
		return dao.submitAssignment(sub, sub.getstudentid(), sub.getcourseid(), sub.getAssignmentid());

	}

	/**
	 * Method for prof, get all the submissions done by all the students for that particular course for that HW
	 *  @param courseid as int
	 *  @param assignmentid as int
	 */
	@RequestMapping(value = "/api/getAllStudentSubmissions/{cid}/{aid}", method = {RequestMethod.GET})
	public List<Submission> getAllSubmissionsByAllStudents(@PathVariable(name = "cid") int courseid,
			@PathVariable(name = "aid") int assignmentid) {

		SubmissionDao dao = SubmissionDao.getInstance();
		return dao.getAllSubmissionsByAllStudents(courseid, assignmentid);


	}

	/**
	 * Method to get all the submissions score
	 * @param subs as Submission array
	 */
	@RequestMapping(value = "/api/getSubmissionScores", method = {RequestMethod.POST})
	public List<SubmissionScore> getAllSubmissionsScores(@RequestBody Submission[] subs) {

		List<Integer> subids=new ArrayList<>();
		for(int i=0;i<subs.length;i++) {
			subids.add(subs[i].getid());
		}
		
		SubmissionScoreDao dao = SubmissionScoreDao.getInstance();
		return dao.getIdSubmissionsScores(subids);

	}

	/**
	 * Method to get all violations summary
	 * @param userid is a integer
	 * @return a list of Submission Summary
	 */
	@RequestMapping(value = "/api/getAllViolationSummary/{userid}", method = {RequestMethod.GET})
	public List<SubmissionSummary> getAllViolationSummary(@PathVariable(name = "userid") int userid) {
		AssignmentDao dao = AssignmentDao.getInstance();
		return dao.getSubmissionSummary(userid);
	}


	/**
	 * Method to get courseids for all sems
	 * @param semid is int
	 * @param cid is int
	 * @param courseno is string
	 * @param crosssection is boolean
	 * @param listofsemids is a int array
	 * @return a list of integers
	 */
	@RequestMapping(value="/api/semesterSubmissions/{semid}/{cid}/{courseno}/{crosssection}",
			method = {RequestMethod.POST})
	public List<Integer> getCourseIdsForAllSems(@PathVariable(name="semid") int semid, 
			@PathVariable(name="cid") int cid,@PathVariable(name="courseno") String courseno,
			@PathVariable(name="crosssection") boolean crosssection,
			@RequestBody int[] listofsemids) {

		CourseDao cdao=CourseDao.getInstance();
		return cdao.findAllCoursesForSectionsAndSems(semid,cid,courseno,crosssection,listofsemids);


	}


	/**
	 * @author karan sharma 
	 * @param reportId the id of the report we want to fetch
	 * @return message whether report can be opened or not 
	 * @throws IOException 
	 * @throws URISyntaxException
	 */
	@RequestMapping(value="/api/getReport/{rid}",method= {RequestMethod.GET}) 
	public Url getReportLink(@PathVariable(name="rid") int reportId){
			ReportDao rdao=ReportDao.getInstance();
			Url url=new Url();
			url.setUrl(rdao.getReportLink(reportId));
			return  url;
	
	
	}


}
