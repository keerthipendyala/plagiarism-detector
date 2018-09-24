package edu.northeastern.cs5500.testdaos;

import edu.northeastern.cs5500.daos.SubmissionDao;
import edu.northeastern.cs5500.models.Submission;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author anju
 * This class contaisn tests for submission daos
 */

@RunWith(SpringRunner.class)
@DataJpaTest
public class SubmissionDaoTest {

    /**
     * This class contains the test for findSubmissions method.
     */
    @Test
    public void testFindSubmissions() {
        SubmissionDao sdao = SubmissionDao.getInstance();
        Integer[] subids = new Integer[1];
        subids[0] = 28;
        List<Submission> subs = sdao.findSubmissions(subids);
        Assert.assertTrue(subs.size() > 0);


    }

    /**
     * This class tests findSubmissionsByAssignmentId method
     */
    @Test
    public void testSubmissionsByAssignmentId() {
        SubmissionDao sdao = SubmissionDao.getInstance();
        List<Submission> subs = sdao.findSubmissionsByAssignmentId(200);
        Assert.assertTrue(subs.size() > 0);
    }


    /**
     * This class tests viewSubmissionHistory method
     */
    @Test
    public void testviewSubmissionHistory() {
        SubmissionDao sdao = SubmissionDao.getInstance();
        List<Submission> subs = sdao.viewSubmissionHistory(28, 3, 200);
        Assert.assertTrue(subs.size() > 0);
        Assert.assertEquals(subs.get(0).getsubmissionname(), "Student1-DB-A1");

    }


    /**
     * This class tests getAllSubmissionsByAllStudents method.
     */
    @Test
    public void testGetAllSubmissionsByAllStudents() {
        SubmissionDao sdao = SubmissionDao.getInstance();
        List<Submission> allSubmissions = sdao.getAllSubmissionsByAllStudents(3, 200);
        Assert.assertTrue(allSubmissions.size() > 0);
    }


    /**
     * This class tests getSubmissionsWithViolations method.
     */
    @Test
    public void testGetSubmissionsWithViolations() {
        SubmissionDao sdao = SubmissionDao.getInstance();
        List<Submission> subs = sdao.getSubmissionsWithViolations(200, 75);
        Assert.assertTrue(subs.size() > 0);
    }


    @Test
    public void testGetLatestAssignmentSubmissions() {
        SubmissionDao sdao = SubmissionDao.getInstance();
        List<Submission> subs = sdao.getLatestAssignmentSubmissions(1702);
        Assert.assertTrue(subs.size() >= 0);
    }


    @Test
    public void testGetAllScannedSubmissions() {
        SubmissionDao sdao = SubmissionDao.getInstance();
        List<Submission> subs = sdao.getAllScannedSubmissions();
        Assert.assertTrue(subs.size() >= 0);
    }


    @Test
    public void testGetLatestUnscannedSubmissions() {
        SubmissionDao sdao = SubmissionDao.getInstance();
        List<Submission> subs = sdao.getLatestUnscannedSubmissions();
        Assert.assertTrue(subs.size() >= 0);
    }

}
