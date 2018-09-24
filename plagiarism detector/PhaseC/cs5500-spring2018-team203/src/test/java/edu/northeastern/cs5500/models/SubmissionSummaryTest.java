package edu.northeastern.cs5500.models;

import org.junit.Assert;
import org.junit.Test;

public class SubmissionSummaryTest {

    @Test
    public void getStudentname() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setStudentname("test");
        Assert.assertEquals("test", summary.getStudentname());
    }

    @Test
    public void setStudentname() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setStudentname("test");
        Assert.assertEquals("test", summary.getStudentname());
    }

    @Test
    public void getCoursename() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setCoursename("test");
        Assert.assertEquals("test", summary.getCoursename());
    }

    @Test
    public void setCoursename() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setCoursename("test");
        Assert.assertEquals("test", summary.getCoursename());
    }

    @Test
    public void getAssignmentname() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setAssignmentname("test");
        Assert.assertEquals("test", summary.getAssignmentname());
    }

    @Test
    public void setAssignmentname() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setAssignmentname("test");
        Assert.assertEquals("test", summary.getAssignmentname());
    }

    @Test
    public void getSubmissionname() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setSubmissionname("test");
        Assert.assertEquals("test", summary.getSubmissionname());
    }

    @Test
    public void setSubmissionname() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setSubmissionname("test");
        Assert.assertEquals("test", summary.getSubmissionname());
    }

    @Test
    public void getCourseno() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setCourseno("test");
        Assert.assertEquals("test", summary.getCourseno());
    }

    @Test
    public void setCourseno() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setCourseno("test");
        Assert.assertEquals("test", summary.getCourseno());
    }

    @Test
    public void getSemestername() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setSemestername("test");
        Assert.assertEquals("test", summary.getSemestername());
    }

    @Test
    public void setSemestername() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setSemestername("test");
        Assert.assertEquals("test", summary.getSemestername());
    }

    @Test
    public void getSubmissionid() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setSubmissionid(1);
        Assert.assertEquals(1, summary.getSubmissionid());
    }

    @Test
    public void setSubmissionid() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setSubmissionid(1);
        Assert.assertEquals(1, summary.getSubmissionid());
    }

    @Test
    public void getCourseid() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setCourseid(1);
        Assert.assertEquals(1, summary.getCourseid());
    }

    @Test
    public void setCourseid() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setCourseid(1);
        Assert.assertEquals(1, summary.getCourseid());
    }

    @Test
    public void getAssignmentid() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setAssignmentid(1);
        Assert.assertEquals(1, summary.getAssignmentid());
    }

    @Test
    public void setAssignmentid() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setAssignmentid(1);
        Assert.assertEquals(1, summary.getAssignmentid());
    }

    @Test
    public void getStudentid() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setStudentid(1);
        Assert.assertEquals(1, summary.getStudentid());
    }

    @Test
    public void setStudentid() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setStudentid(1);
        Assert.assertEquals(1, summary.getStudentid());
    }

    @Test
    public void getMaxscore() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setMaxscore(90f);
        Assert.assertEquals(90, summary.getMaxscore(), 0.1);
    }

    @Test
    public void setMaxscore() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setMaxscore(90f);
        Assert.assertEquals(90, summary.getMaxscore(), 0.1);
    }

    @Test
    public void getAvgscore() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setAvgscore(90f);
        Assert.assertEquals(90, summary.getAvgscore(), 0.1);
    }

    @Test
    public void setAvgscore() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setAvgscore(90f);
        Assert.assertEquals(90, summary.getAvgscore(), 0.1);
    }

    @Test
    public void getReportlink() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setReportlink("test");
        Assert.assertEquals("test", summary.getReportlink());
    }

    @Test
    public void setReportlink() {
        SubmissionSummary summary = new SubmissionSummary();
        summary.setReportlink("test");
        Assert.assertEquals("test", summary.getReportlink());
    }
}