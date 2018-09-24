package edu.northeastern.cs5500.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.cs5500.daos.PendingTableDao;
import edu.northeastern.cs5500.models.User;


@RestController
public class RequestRegistrationService {

    public RequestRegistrationService() {


    }


    @RequestMapping(value = "/api/requestProfessorRegistration", method = {RequestMethod.POST})
    public int requestProfessorRegistration(@RequestBody User user) {
        PendingTableDao dao = PendingTableDao.getInstance();
        return dao.addUserToPendingTable(user);

    }


    @RequestMapping(value = "/api/acceptRequest/{id}", method = {RequestMethod.GET})
    public int acceptRequest(@PathVariable(name = "id") int id) {
        PendingTableDao dao = PendingTableDao.getInstance();
        return dao.acceptRequest(id);

    }

    @RequestMapping(value = "/api/rejectRequest/{id}", method = {RequestMethod.GET})
    public int rejectRequest(@PathVariable(name = "id") int id) {
        PendingTableDao dao = PendingTableDao.getInstance();
        return dao.rejectRequest(id);

    }
}
