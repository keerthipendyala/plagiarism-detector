package edu.northeastern.cs5500.testmodels;

import edu.northeastern.cs5500.models.Course;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test
 */
public class CourseTest {

    @Test
    public void getCourseno() {
        Course c = new Course();
        c.setCourseno("cs5500");
        Assert.assertEquals("cs5500", c.getCourseno());
    }

    @Test
    public void setCourseno() {
        Course c = new Course();
        c.setCourseno("cs5500");
        Assert.assertEquals("cs5500", c.getCourseno());
    }

    @Test
    public void getCoursename() {
        Course c = new Course();
        c.setCoursename("cs5500");
        Assert.assertEquals("cs5500", c.getCoursename());
    }

    @Test
    public void setCoursename() {
        Course c = new Course();
        c.setCoursename("cs5500");
        Assert.assertEquals("cs5500", c.getCoursename());
    }

    @Test
    public void getDepartmentname() {
        Course c = new Course();
        c.setDepartmentname("cs");
        Assert.assertEquals("cs", c.getDepartmentname());

    }

    @Test
    public void setDepartmentname() {
        Course c = new Course();
        c.setDepartmentname("cs");
        Assert.assertEquals("cs", c.getDepartmentname());
    }

    @Test
    public void getCourseid() {
        Course c = new Course();
        c.setCourseid(1);
        Assert.assertEquals(1, c.getCourseid());

    }

    @Test
    public void setCourseid() {
        Course c = new Course();
        c.setCourseid(1);
        Assert.assertEquals(1, c.getCourseid());
    }

    @Test
    public void getProfid() {
        Course c = new Course();
        c.setProfid(1);
        Assert.assertEquals(1, c.getProfid());
    }

    @Test
    public void setProfid() {
        Course c = new Course();
        c.setProfid(1);
        Assert.assertEquals(1, c.getProfid());
    }

    @Test
    public void getProgLanguge() {
        Course c = new Course();
        c.setProglanguage("python3");
        Assert.assertEquals(c.getProglanguage(),"python3");
    }

    @Test
    public void setProgLanguge() {
        Course c = new Course();
        c.setProglanguage("python3");
        Assert.assertEquals(c.getProglanguage(),"python3");
    }
}