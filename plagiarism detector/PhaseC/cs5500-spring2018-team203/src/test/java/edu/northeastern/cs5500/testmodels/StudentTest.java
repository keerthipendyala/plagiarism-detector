package edu.northeastern.cs5500.testmodels;

import edu.northeastern.cs5500.models.Student;
import org.junit.Assert;
import org.junit.Test;

public class StudentTest {

    @Test
    public void getUserid() {
        Student s = new Student();
        s.setUserid(1);
        Assert.assertEquals(1, s.getUserid());
    }

    @Test
    public void setUserid() {
        Student s = new Student();
        s.setUserid(1);
        Assert.assertEquals(1, s.getUserid());
    }

    @Test
    public void getStatus() {
        Student s = new Student();
        s.setStatus("enrolled");
        Assert.assertEquals("enrolled", s.getStatus());
    }

    @Test
    public void setStatus() {
        Student s = new Student();
        s.setStatus("enrolled");
        Assert.assertEquals("enrolled", s.getStatus());
    }

    @Test
    public void getDateofgraduation() {
        Student s = new Student();
        s.setDateofgraduation("01/01/2020");
        Assert.assertEquals("01/01/2020", s.getDateofgraduation());
    }

    @Test
    public void setDateofgraduation() {
        Student s = new Student();
        s.setDateofgraduation("01/01/2020");
        Assert.assertEquals("01/01/2020", s.getDateofgraduation());
    }

    @Test
    public void getStudentid() {
        Student s = new Student();
        s.setStudentid(1);
        Assert.assertEquals(1, s.getStudentid());
    }

    @Test
    public void setStudentid() {
        Student s = new Student();
        s.setStudentid(1);
        Assert.assertEquals(1, s.getStudentid());
    }
    
    @Test 
    public void setStudent() {
    	
    	Student s= new Student(1,"uname","password","fullname",1,1,"fulltime","2/2/2018");
    	Assert.assertEquals("password", s.getPassword());
    	Assert.assertEquals("uname", s.getUsername());
    	
    	
    	
    }
    
    
}