package edu.northeastern.cs5500.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.cs5500.daos.SemesterDao;
import edu.northeastern.cs5500.models.Semester;

@RestController
public class SemesterService {


	@RequestMapping(value ="/api/getAllSemester", method = {RequestMethod.GET})
    public List<Semester> getAllSems() {
        SemesterDao dao = SemesterDao.getInstance();
        return dao.getAllSemesters();

    }

	
	
}
