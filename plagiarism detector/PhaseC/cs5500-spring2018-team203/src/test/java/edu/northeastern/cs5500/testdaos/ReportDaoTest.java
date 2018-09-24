package edu.northeastern.cs5500.testdaos;


import edu.northeastern.cs5500.daos.ReportDao;
import edu.northeastern.cs5500.models.Report;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author anju
 * This class contains tests for AssignmentDao
 */

@RunWith(SpringRunner.class)
@DataJpaTest
public class ReportDaoTest {

    /**
     * This method tests createReport method.
     */
    @Test
    public void testCreateReport() {
        ReportDao rdao = ReportDao.getInstance();
        Date d = new Date();
        long id = d.getTime();
        Report r = new Report(id);
        int result = rdao.createReport(r);
        Assert.assertTrue(result != -1);
    }


    @Test
    public void testUpdateReport() {
        ReportDao rdao = ReportDao.getInstance();
        Date d = new Date();
        long id = d.getTime();
        Report r = new Report(id);
        int result = rdao.updateReport(r);
        Assert.assertTrue(result != -1);
    }
    
    @Test
    public void testGetReportLink() {
    	 ReportDao rdao = ReportDao.getInstance();
    	 Assert.assertTrue(rdao.getReportLink(2134).length()>=0);
    	 
    }
    
}
