package edu.northeastern.cs5500.testmodels;

import edu.northeastern.cs5500.models.Report;
import org.junit.Assert;
import org.junit.Test;

public class ReportTest {

    @Test
    public void getId() {
        Report r = new Report(100);
        r.setId(100);
        Assert.assertEquals(100, r.getId());
    }

    @Test
    public void setId() {
        Report r = new Report(100);
        r.setId(100);
        Assert.assertEquals(100, r.getId());
    }

    @Test
    public void getReportId() {
        Report r = new Report(100);
        Assert.assertEquals(100, r.getReportId());
    }

    @Test
    public void setReportId() {
        Report r = new Report(100);
        Assert.assertEquals(100, r.getReportId());
    }

    @Test
    public void getReportlink() {
        Report r = new Report(100);
        r.setReportlink("localhost:8080/report.html");
        Assert.assertEquals("localhost:8080/report.html", r.getReportlink());
    }

    @Test
    public void setReportlink() {
        Report r = new Report(100);
        r.setReportlink("localhost:8080/report.html");
        Assert.assertEquals("localhost:8080/report.html", r.getReportlink());
    }

    @Test
    public void isSuccessful() {
        Report r = new Report(100);
        r.setSuccessful(true);
        Assert.assertEquals(true, r.isSuccessful());
    }

    @Test
    public void setSuccessful() {
        Report r = new Report(100);
        r.setSuccessful(true);
        Assert.assertEquals(true, r.isSuccessful());
    }
    
    @Test 
    public void createReport() {
    	
    	Report rep=new Report(1,2,"replink",false);
    	rep.setReportId(2);
    	Assert.assertEquals(false, rep.isSuccessful());
    	Assert.assertEquals("replink", rep.getReportlink());
    	Assert.assertEquals(2, rep.getReportId());

    	
    }
}