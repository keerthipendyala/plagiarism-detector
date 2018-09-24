package edu.northeastern.cs5500.testmodels;

import edu.northeastern.cs5500.email.SendEmail;
import edu.northeastern.cs5500.models.Professor;

import javax.mail.MessagingException;

import org.junit.Assert;
import org.junit.Test;

public class ProfessorTest {

    @Test
    public void getProfessorid() {
        Professor professor = new Professor();
        professor.setProfessorid(1);
        Assert.assertEquals(1, professor.getProfessorid());
    }

    @Test
    public void setProfessorid() {
        Professor professor = new Professor();
        professor.setProfessorid(1);
        Assert.assertEquals(1, professor.getProfessorid());
    }

    @Test
    public void getUserid() {
        Professor professor = new Professor();
        professor.setUserid(1);
        Assert.assertEquals(1, professor.getUserid());
    }

    @Test
    public void setUserid() {
        Professor professor = new Professor();
        professor.setProfessorid(1);
        Assert.assertEquals(1, professor.getProfessorid());
    }

    @Test
    public void getTitle() {
        Professor professor = new Professor();
        professor.setTitle("asst. professor");
        Assert.assertEquals("asst. professor", professor.getTitle());
    }

    @Test
    public void setTitle() {
        Professor professor = new Professor();
        professor.setTitle("asst. professor");
        Assert.assertEquals("asst. professor", professor.getTitle());
    }

    @Test
    public void getDegree() {
        Professor professor = new Professor();
        professor.setDegree("Phd");
        Assert.assertEquals("Phd", professor.getDegree());
    }


    @Test
    public void setDegree() {
        Professor professor = new Professor();
        professor.setDegree("Phd");
        Assert.assertEquals("Phd", professor.getDegree());
    }

    @Test
    public void getDepartment() {
        Professor professor = new Professor();
        professor.setDepartment("cs");
        Assert.assertEquals("cs", professor.getDepartment());
    }

    @Test
    public void setDepartment() {
        Professor professor = new Professor();
        professor.setDepartment("cs");
        Assert.assertEquals("cs", professor.getDepartment());
    }
    
    @Test 
    public void setProf() throws MessagingException {
    
    	Professor p=new Professor(1,"prof","profpass","mr prof ",1,1,"Dr.","PHD","CCIS");
    	Assert.assertEquals("CCIS", p.getDepartment());
    	Assert.assertEquals("prof", p.getUsername());
    	
    	SendEmail email=SendEmail.getInstance();
		email.sendEmail(new StringBuilder("My Test Message,successful"), "copycatchteam@gmail.com", "test");
    	
    	
    }
    
}