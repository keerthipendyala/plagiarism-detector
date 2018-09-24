package edu.northeastern.cs5500.testmodels;

import edu.northeastern.cs5500.models.Submission;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author anju
 * Test class for Submission model
 */
public class SubmissionTest {

    /**
     * Test to getId
     */
    @Test
    public void getid() {
        Submission s = new Submission();
        s.setid(1);
        Assert.assertEquals(1, s.getid());
    }

    /**
     * Test to getAssignmentId
     */
    @Test
    public void getAssignmentid() {
        Submission s = new Submission();
        s.setid(1);
        Assert.assertEquals(1, s.getid());
    }

    /**
     * Test to setAssignmentId
     */
    @Test
    public void setAssignmentid() {
        Submission s = new Submission();
        s.setAssignmentid(1);
        Assert.assertEquals(1, s.getAssignmentid());
    }

    /**
     * Test to set id
     */
    @Test
    public void setid() {
        Submission s = new Submission();
        s.setid(1);
        Assert.assertEquals(1, s.getid());
    }

    /**
     * Test to get studentid
     */
    @Test
    public void getstudentid() {
        Submission s = new Submission();
        s.setstudentid(1);
        Assert.assertEquals(1, s.getstudentid());
    }

    /**
     * Test to set Student id
     */
    @Test
    public void setstudentid() {
        Submission s = new Submission();
        s.setstudentid(1);
        Assert.assertEquals(1, s.getstudentid());
    }

    /**
     * Test to get Submission name
     */
    @Test
    public void getsubmissionname() {
        Submission s = new Submission();
        s.setsubmissionname("proj1");
        Assert.assertEquals("proj1", s.getsubmissionname());
    }

    /**
     * Test to set submission name
     */
    @Test
    public void setsubmissionname() {
        Submission s = new Submission();
        s.setsubmissionname("proj1");
        Assert.assertEquals("proj1", s.getsubmissionname());
    }

    /**
     * Test to get course id
     */
    @Test
    public void getcourseid() {
        Submission s = new Submission();
        s.setcourseid(1);
        Assert.assertEquals(1, s.getcourseid());
    }

    /**
     * Test to set course id
     */
    @Test
    public void setcourseid() {
        Submission s = new Submission();
        s.setcourseid(1);
        Assert.assertEquals(1, s.getcourseid());
    }

    /*
    Test to get submission link
     */
    @Test
    public void getsubmissionlink() {
        Submission s = new Submission();
        s.setsubmissionlink("www.github.com/neu");
        Assert.assertEquals("www.github.com/neu", s.getsubmissionlink());
    }

    /*
    Test to set submission link
     */
    @Test
    public void setsubmissionlink() {
        Submission s = new Submission();
        s.setsubmissionlink("www.github.com/neu");
        Assert.assertEquals("www.github.com/neu", s.getsubmissionlink());
    }

    /**
     * Test to get submission description
     */
    @Test
    public void getsubmissiondesc() {
        Submission s = new Submission();
        s.setsubmissiondesc("proj1");
        Assert.assertEquals("proj1", s.getsubmissiondesc());
    }

    /**
     * Test to set Submission description
     */
    @Test
    public void setsubmissiondesc() {
        Submission s = new Submission();
        s.setsubmissiondesc("proj1");
        Assert.assertEquals("proj1", s.getsubmissiondesc());
    }
    
    @Test 
    public void setSubmission() {

    	 Submission s=new Submission(1,1,"subname",1,"link","subdesc",2);
    	 Assert.assertEquals("subdesc", s.getsubmissiondesc());
    	 Assert.assertEquals("subname", s.getsubmissionname());
    	 
    	
    }
    
}