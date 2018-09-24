package edu.northeastern.cs5500.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.cs5500.models.Submission;
import edu.northeastern.cs5500.scanner.JPlagServiceScanner;

/**
 * @author anju
 * @desc This service is a rest controller for different comparison strategies.
 */
@RestController
public class ComparisionService {

    /**
     * Launch scan using jplag service.
     * @param submissions
     * @param host
     */
    public void launchScan(List<Submission> submissions, String host) {
        JPlagServiceScanner scanner = new JPlagServiceScanner();
        scanner.setDomainName(host);
        scanner.scan(submissions);
    }

    /**
     * @author karan sharma 
     * Method for scanning given list of submissions.
     *
     * @param sublist list of submissions
     * @return
     */
    @RequestMapping(value = "/api/scanassignment", method = {RequestMethod.POST})
    public int scanSubmissionInJplagAssignment(@RequestBody List<Submission> sublist,
                                             HttpServletRequest request) {
        launchScan(sublist, request.getHeader("host"));
        return 1;
    }
    

    
}

