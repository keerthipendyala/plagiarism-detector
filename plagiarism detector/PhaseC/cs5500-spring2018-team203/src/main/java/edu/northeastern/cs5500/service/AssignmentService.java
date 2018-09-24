package edu.northeastern.cs5500.service;


import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.cs5500.daos.AssignmentDao;
import edu.northeastern.cs5500.models.Assignment;

/**
 * 
 * @author karan sharma 
 * THis Service contains methods that will Access the Daos
 */

@RestController
public class AssignmentService {

	public AssignmentService() {


	}

	@RequestMapping(value="/api/getAllAssignments/{courseid}", method={ RequestMethod.GET})
	public List<Assignment> getAllAssignments(@PathVariable(name="courseid") int courseid) {
		AssignmentDao dao = AssignmentDao.getInstance();
		return dao.getAllAssignments(courseid);

	}

	@RequestMapping(value="/api/createAssignment/", method={ RequestMethod.POST})
	public int createAssignment(@RequestBody Assignment a) {
		AssignmentDao dao = AssignmentDao.getInstance();
		return dao.createAssignment(a.getCourseid(),a);
	}
	
	@RequestMapping(value="/api/deleteAssignment/{aid}", method={ RequestMethod.DELETE})
	public int deleteAssignment(@PathVariable(name="aid") int aid) {
		AssignmentDao dao = AssignmentDao.getInstance();
		return dao.deleteAssignmentForACourse(0, aid);
	}
	
	
	@RequestMapping(value="/api/updateAssignment", method={ RequestMethod.POST})
	public int updateAssignment(@RequestBody Assignment a) {
		AssignmentDao dao = AssignmentDao.getInstance();
		return dao.updateAssignment(a, a.getId());
	}
	
	
	@RequestMapping(value="/api/getAssignment/{aid}", method={ RequestMethod.GET})
	public Assignment getAssignment(@PathVariable(name="aid") int aid) {
		AssignmentDao dao = AssignmentDao.getInstance();
		return dao.getAssignment(aid);
	}
	
	

}
