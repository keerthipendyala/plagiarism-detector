package edu.northeastern.cs5500.testmodels;

import edu.northeastern.cs5500.models.Assignment;
import org.junit.Assert;
import org.junit.Test;

public class AssignmentTest {

    @Test
    public void getId() {
        Assignment a = new Assignment();
        a.setId(10);
        Assert.assertEquals(10,a.getId());
    }

    @Test
    public void setId() {
        Assignment a = new Assignment();
        a.setId(10);
        Assert.assertEquals(10,a.getId());

    }

    @Test
    public void getAssignmentname() {
        Assignment a = new Assignment();
        a.setAssignmentname("test");
        Assert.assertEquals("test",a.getAssignmentname());

    }

    @Test
    public void setAssignmentname() {
        Assignment a = new Assignment();
        a.setAssignmentname("test");
        Assert.assertEquals("test",a.getAssignmentname());
    }

    @Test
    public void getCourseid() {
        Assignment a = new Assignment();
        a.setCourseid(10);
        Assert.assertEquals(10,a.getCourseid());
    }

    @Test
    public void setCourseid() {
        Assignment a = new Assignment();
        a.setCourseid(10);
        Assert.assertEquals(10,a.getCourseid());
    }

    @Test
    public void getDescription() {
        Assignment a = new Assignment();
        a.setDescription("test");
        Assert.assertEquals("test",a.getDescription());
    }


    @Test
    public void setDescription() {
        Assignment a = new Assignment();
        a.setAssignmentname("test");
        Assert.assertEquals("test",a.getAssignmentname());
    }
    
    @Test
    public void setAssignment() {
    	Assignment a =new Assignment(1,"assignmentname",2,"description");
    	Assert.assertEquals("assignmentname",a.getAssignmentname());
    	Assert.assertEquals("description",a.getDescription());
    	
    	
    	
    	
    }
    
}