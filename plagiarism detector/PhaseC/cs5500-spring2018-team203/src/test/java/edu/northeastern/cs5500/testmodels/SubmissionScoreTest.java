package edu.northeastern.cs5500.testmodels;


import edu.northeastern.cs5500.models.SubmissionScore;

import static org.junit.Assert.*;

import org.junit.Assert;

import org.junit.Test;



/**
 * @author anju
 * Test class for SubmissionScore Model
 */

public class SubmissionScoreTest {

    /**
     * Test to get Average score
     */
    @Test
    public void getAveragescore() {
        SubmissionScore s = new SubmissionScore();
        s.setAveragescore(70.0d);
        Assert.assertEquals(70.0d, s.getAveragescore(), 0);
    }

    /**
     * Test to set Average score
     */
    @Test
    public void setAveragescore() {
        SubmissionScore s = new SubmissionScore();
        s.setAveragescore(70.0d);
        Assert.assertEquals(70.0d, s.getAveragescore(), 0);
    }

    /**
     * Test to get Max score
     */
    @Test
    public void getMaxscore() {
        SubmissionScore s = new SubmissionScore();
        s.setMaxscore(60.0d);
        Assert.assertEquals(60.0d, s.getMaxscore(), 0);
    }

    /**
     * Test to set Max score
     */
    @Test
    public void setMaxscore() {
        SubmissionScore s = new SubmissionScore();
        s.setMaxscore(60.0d);
        Assert.assertEquals(60.0d, s.getMaxscore(), 0);
    }

    /**
     * Test to get course id
     */
    @Test
    public void getCourseid() {
        SubmissionScore s = new SubmissionScore();
        s.setCourseid(3);
        Assert.assertEquals(3, s.getCourseid());
    }

    /**
     * test to set course id
     */
    @Test
    public void setCourseid() {
        SubmissionScore s = new SubmissionScore();
        s.setCourseid(3);
        Assert.assertEquals(3, s.getCourseid());
    }

    /**
     * Test to get assignment id
     */
    @Test
    public void getAssignmentid() {
        SubmissionScore s = new SubmissionScore();
        s.setAssignmentid(200);
        Assert.assertEquals(200, s.getAssignmentid());
    }

    /**
     * Test to set assignment id
     */
    @Test
    public void setAssignmentid() {
        SubmissionScore s = new SubmissionScore();
        s.setAssignmentid(200);
        Assert.assertEquals(200, s.getAssignmentid());
    }

    /**
     * Test to get student id
     */
    @Test
    public void getStudent1id() {
        SubmissionScore s = new SubmissionScore();
        s.setStudent1id(28);
        Assert.assertEquals(28, s.getStudent1id());
    }

    /**
     * Test to set student1 id
     */
    @Test
    public void setStudent1id() {
        SubmissionScore s = new SubmissionScore();
        s.setStudent1id(28);
        Assert.assertEquals(28, s.getStudent1id());
    }

    /**
     * Test to get student2 id
     */
    @Test
    public void getStudent2id() {
        SubmissionScore s = new SubmissionScore();
        s.setStudent2id(28);
        Assert.assertEquals(28, s.getStudent2id());
    }


    /**
     * Test to set student2id
     */
    @Test
    public void setStudent2id() {
        SubmissionScore s = new SubmissionScore();
        s.setStudent2id(28);
        Assert.assertEquals(28, s.getStudent2id());
    }

    /**
     * Test to get submissionid1
     */
    @Test
    public void getSubmissionid1() {
        SubmissionScore s = new SubmissionScore();
        s.setSubmissionid1(2);
        Assert.assertEquals(2, s.getSubmissionid1());

    }

    /**
     * Test to set submissionid1
     */
    @Test
    public void setSubmissionid1() {
        SubmissionScore s = new SubmissionScore();
        s.setSubmissionid1(2);
        Assert.assertEquals(2, s.getSubmissionid1());
    }

    /**
     * Test to set submissionid2
     */
    @Test
    public void setSubmissionid2() {
        SubmissionScore s = new SubmissionScore();
        s.setSubmissionid2(3);
        Assert.assertEquals(3, s.getSubmissionid2());
    }

    /**
     * Test to get Report link
     */
    @Test
    public void getReportlink() {
        SubmissionScore s = new SubmissionScore();
        s.setReportlink("xyz");
        Assert.assertEquals("xyz", s.getReportlink());
    }

    /**
     * Test to set Report link
     */
    @Test
    public void setReportlink() {
        SubmissionScore s = new SubmissionScore();
        s.setReportlink("xyz");
        Assert.assertEquals("xyz", s.getReportlink());
    }

    /**
     * Test to set Report id
     */
    @Test
    public void setReportid() {
        SubmissionScore s = new SubmissionScore();
        s.setReportid(12);
        Assert.assertEquals(12, s.getReportid());
    }


    /**
     * Test to get Report id
     */
    @Test
    public void getReportid() {
        SubmissionScore s = new SubmissionScore();
        s.setReportid(12);
        Assert.assertEquals(12, s.getReportid());
    }
    
    @Test 
    public void createSubmissionScore() {
    	
    	SubmissionScore sub=new SubmissionScore(1,2.3,44.5,3,3,1,2,3,4);
    	Assert.assertEquals(4, sub.getSubmissionid2());
    	Assert.assertEquals(3, sub.getSubmissionid1());
    }

  
	@Test 
    public void testCreateNewSubmission() {
    	
    	SubmissionScore sub=new SubmissionScore(1,2.5,44);
    	sub.setScoreid(3);
    	assertEquals(3,sub.getScoreid());
    	
    }














}
