package edu.northeastern.cs5500.testdaos;

import edu.northeastern.cs5500.daos.SubmissionScoreDao;
import edu.northeastern.cs5500.models.SubmissionScore;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author anju
 * Test class for SubmissionScoreDao
 */

@RunWith(SpringRunner.class)
@DataJpaTest
public class SubmissionScoreDaoTest {

    /**
     * Test getAllSubmissionScores method
     */
    @Test
    public void testGetAllSubmissionsScores() {
        SubmissionScoreDao scdao = SubmissionScoreDao.getInstance();
        List<SubmissionScore> subs = scdao.getAllSubmissionsScores(578, 2224);
        Assert.assertTrue(subs.size() > 0);

    }

    /**
     * Test get submission score using report id
     */
    @Test
    public void testGetSubmissionScores() {
        SubmissionScoreDao scdao = SubmissionScoreDao.getInstance();
        List<SubmissionScore> subs = scdao.getAllSubmissionsScores(578, 2224);
        Assert.assertTrue(subs.size() > 0);
        int reportId = subs.get(0).getReportid();
        List<SubmissionScore> submissionScores = SubmissionScoreDao.getInstance().getSubmissionScores(reportId);
        Assert.assertTrue(submissionScores.size() > 0);
        Assert.assertEquals(submissionScores.get(0).getReportid(), reportId);

    }
    
    
    

}
