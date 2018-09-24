package edu.northeastern.cs5500.service;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.cs5500.daos.CourseDao;
import edu.northeastern.cs5500.models.Course;


@RestController
public class CourseService {

	public CourseService() {
		
	}
	
	/**
	 * New Sprint 3 functionality 
	 * @param course
	 * @return
	 */
	
	@RequestMapping(value="/api/createCourse", method={ RequestMethod.POST})
	public int createCourse(@RequestBody Course course) {
		CourseDao dao = CourseDao.getInstance();
		System.out.println(course);
		return dao.createCourse(course.getSemid(), course);
	}
	
	@RequestMapping(value="/api/deleteCourse/{cid}", method={ RequestMethod.DELETE})
	public int deleteCourse(@PathVariable(name="cid") int courseid) {
		CourseDao dao = CourseDao.getInstance();
		return dao.deleteCourse(courseid);
	}
	
	@RequestMapping(value="/api/updateCourse", method={ RequestMethod.POST})
	public int updateCourse(@RequestBody Course course) {
		CourseDao dao = CourseDao.getInstance();
		return dao.updateCourse(course.getSemid(), course);
		
	}
	
	
	@RequestMapping(value="/api/getCourse/{sid}/{cid}/{pid}", method={ RequestMethod.GET})
	public Course getCourse(@PathVariable(name="pid") int profid,@PathVariable(name="sid") int semid,
			@PathVariable(name="cid") int cid) {
		CourseDao dao = CourseDao.getInstance();
		return dao.getCourse(semid, profid, cid);
		
	}
	
	
	@RequestMapping(value="/api/getAllCourses/{sid}/{pid}", method={ RequestMethod.GET})
	public List<Course> getAllCourses(@PathVariable(name="pid") int profid,@PathVariable(name="sid") int semid) {
		CourseDao dao = CourseDao.getInstance();
		return dao.getAllCoursesForProf(profid, semid);
		
	}
	
	
	@RequestMapping(value="/api/getAllStudentCourses/{sid}/{semid}", method={ RequestMethod.GET})
	public List<Course> getAllStudentCourses(@PathVariable(name="sid") int studid,@PathVariable(name="semid") int semid) {
		CourseDao dao = CourseDao.getInstance();
		return dao.getAllCoursesForStudents(studid, semid);
		
	}
	
	@RequestMapping(value="/api/getUnregisteredCourses/{sid}/{semid}", method={ RequestMethod.GET})
	public List<Course> getUnregisteredCourses(@PathVariable(name="sid") int studid,@PathVariable(name="semid") int semid) {
		CourseDao dao = CourseDao.getInstance();
		return dao.courseStudentCanRegister(studid, semid);
		
	}
	
	
	@RequestMapping(value="/api/addCourse/{sid}/{courseid}", method={ RequestMethod.GET})
	public int addCourse(@PathVariable(name="sid") int studid,@PathVariable(name="courseid") int courseid) {
		CourseDao dao = CourseDao.getInstance();
		return dao.addCourseForStudent(studid, courseid);
		
	}
	
	
	
	
}
