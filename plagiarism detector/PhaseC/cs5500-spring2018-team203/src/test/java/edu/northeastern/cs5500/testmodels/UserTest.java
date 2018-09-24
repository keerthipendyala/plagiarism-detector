package edu.northeastern.cs5500.testmodels;

import edu.northeastern.cs5500.models.User;
import org.junit.Assert;
import org.junit.Test;

public class UserTest {

    @Test
    public void getUsername() {
        User u = new User();
        u.setUsername("tes");
        Assert.assertEquals("tes", u.getUsername());
    }

    @Test
    public void setUsername() {
        User u = new User();
        u.setUsername("tes");
        Assert.assertEquals("tes", u.getUsername());
    }

    @Test
    public void getPassword() {
        User u = new User();
        u.setPassword("tes");
        Assert.assertEquals("tes", u.getPassword());
    }

    @Test
    public void setPassword() {
        User u = new User();
        u.setPassword("tes");
        Assert.assertEquals("tes", u.getPassword());
    }

    @Test
    public void getFullname() {
        User u = new User();
        u.setFullName("tes");
        Assert.assertEquals("tes", u.getFullname());
    }

    @Test
    public void setFullName() {
        User u = new User();
        u.setFullName("tes");
        Assert.assertEquals("tes", u.getFullname());
    }

    @Test
    public void getUserid() {
        User u = new User();
        u.setUserid(1);
        Assert.assertEquals(1, u.getUserid());
    }

    @Test
    public void setUserid() {
        User u = new User();
        u.setUserid(1);
        Assert.assertEquals(1, u.getUserid());
    }

    @Test
    public void getDateofBirth() {
        User u = new User();
        u.setPassword("01/01/1990");
        Assert.assertEquals("01/01/1990", u.getPassword());
    }

    @Test
    public void setDateofBirth() {
        User u = new User();
        u.setPassword("01/01/1990");
        Assert.assertEquals("01/01/1990", u.getPassword());
    }

    @Test
    public void getRole() {
        User u = new User();
        u.setRole("student");
        Assert.assertEquals("student", u.getRole());
    }

    @Test
    public void setRole() {
        User u = new User();
        u.setRole("student");
        Assert.assertEquals("student", u.getRole());
    }

    @Test
    public void getEmail() {
        User u = new User();
        u.setEmail("student@gmail.com");
        Assert.assertEquals("student@gmail.com", u.getEmail());
    }

    @Test
    public void setEmail() {
        User u = new User();
        u.setEmail("student@gmail.com");
        Assert.assertEquals("student@gmail.com", u.getEmail());
    }

    @Test
    public void getFlag() {
        User u = new User();
        u.setFlag(1);
        Assert.assertEquals(1, u.getFlag());
    }

    @Test
    public void setFlag() {
        User u = new User();
        u.setFlag(1);
        Assert.assertEquals(1, u.getFlag());
    }

    @Test
    public void getAddress() {
        User u = new User();
        u.setAddress("boston,ma");
        Assert.assertEquals("boston,ma", u.getAddress());
    }

    @Test
    public void setAddress() {
        User u = new User();
        u.setAddress("boston,ma");
        Assert.assertEquals("boston,ma", u.getAddress());
    }

    @Test
    public void getPhone() {
        User u = new User();
        u.setPhone("123456789");
        Assert.assertEquals("123456789", u.getPhone());
    }

    @Test
    public void setPhone() {
        User u = new User();
        u.setPhone("123456789");
        Assert.assertEquals("123456789", u.getPhone());
    }
  
	@Test 
	public void testUserCreate() {
		User u=new User(1,"uname1","pass1","username fullname");
		u.setDateofBirth("2/2/1992");
		Assert.assertEquals("2/2/1992", u.getDateofBirth());
		Assert.assertEquals("uname1", u.getUsername());
		
	}
	
	@Test
	public void createUser2() {
		
		User u=new User(1,"uname1","pass1","username fullname","professor");
		Assert.assertEquals("professor", u.getRole());
		
	}
    

	@Test
	public void createUser3() {
		
		User u=new User("uname1","username fullname",1);
		Assert.assertEquals(1, u.getUserid());
		
	}
    
	
    
    
    
    
}