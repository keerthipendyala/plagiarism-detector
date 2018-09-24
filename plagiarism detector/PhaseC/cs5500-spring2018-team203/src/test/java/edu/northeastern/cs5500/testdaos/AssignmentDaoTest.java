package edu.northeastern.cs5500.testdaos;

import edu.northeastern.cs5500.daos.AssignmentDao;
import edu.northeastern.cs5500.models.Assignment;
import edu.northeastern.cs5500.models.SubmissionSummary;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author anju
 * This class contains tests for AssignmentDao
 */

@RunWith(SpringRunner.class)
@DataJpaTest
public class AssignmentDaoTest {

    /**
     * This test will get all assignments given a courseid
     */
    @Test
    public void testGetAllAssignment() {

        AssignmentDao adao = AssignmentDao.getInstance();
        List<Assignment> allAssignments = adao.getAllAssignments(3);
        assertTrue(allAssignments.size() > 0);
        allAssignments.stream().forEach(l -> assertTrue(l.getCourseid() == 3));

    }


    /**
     * Test to find the emails of professor
     */
    @Test
    public void testFindProfessor(){

        //Getting the emails of Professor
        AssignmentDao adao = AssignmentDao.getInstance();
        List<Integer> courseId = new ArrayList<>();
        courseId.add(3);
        String emails = adao.findProfessor(3);
        assertTrue(emails.length() > 0);
    }


    /**
     * Test to get students from all submissionids;
     */
    @Test
    public void testStudentFromSubmissionIds() {
        AssignmentDao adao = AssignmentDao.getInstance();
        List<Integer> submisisonIds = new ArrayList<>();
        submisisonIds.add(28);
        Map<Integer, String> students = adao.getStudentsFromSubmissionIds(submisisonIds);
        assertTrue(students.size() > 0);
    }

    /**
     * Test create assignment
     */

    @Test
    public void testCreateAssignment() {
        AssignmentDao dao = AssignmentDao.getInstance();
        Assignment a = new Assignment();
        String uniqueName = Long.toString(new Date().getTime());
        a.setDescription("test assignment");
        a.setAssignmentname(uniqueName);
        int result = dao.createAssignment(5,a);
        assertTrue(result > 0);
        if (result > 0) {
            // clean up
            dao.deleteAssignment(uniqueName);
        }
    }

    /**
     * Test delete assignment
     */

    @Test
    public void testDeleteAssignment() {
        AssignmentDao dao = AssignmentDao.getInstance();
        Assignment a = new Assignment();
        String uniqueName = Long.toString(new Date().getTime());
        a.setDescription("test assignment");
        a.setAssignmentname(uniqueName);
        int result = dao.createAssignment(5,a);
        if (result == 0) {
            fail("Failed to create new assignment for deletion");
        }
        assertTrue(result > 0);
        int deleted = dao.deleteAssignment(uniqueName);
        assertTrue(deleted > 0);
    }

    /**
     * Test when request fails.
     */
    @Test
    public void testGetAllAssignmentsFailure() {
        String uname = AssignmentDao.getUname();
        AssignmentDao.setUname("");
        assertTrue(AssignmentDao.getInstance().getAllAssignments(1).isEmpty());
        AssignmentDao.setUname(uname);
    }

    /**
     * Test when request fails.
     */
    @Test
    public void createAssignmentFailure() {
        String uname = AssignmentDao.getUname();
        AssignmentDao.setUname("");
        assertTrue(AssignmentDao.getInstance().createAssignment(1, new Assignment()) == 0);
        AssignmentDao.setUname(uname);
    }


    /**
     * Test when request fails.
     */
    @Test
    public void deleteAssignmentFailure() {
        String uname = AssignmentDao.getUname();
        AssignmentDao.setUname("");
        assertTrue(AssignmentDao.getInstance().deleteAssignment(
                Long.toString(new Date().getTime())) == 0);
        AssignmentDao.setUname(uname);
    }

    /**
     * Test when request fails.
     */
    @Test
    public void getStudentsFromSubmissionIdsFailure() {
        String uname = AssignmentDao.getUname();
        AssignmentDao.setUname("");
        assertTrue(AssignmentDao.getInstance().
                getStudentsFromSubmissionIds(Arrays.asList(1)).isEmpty());
        AssignmentDao.setUname(uname);
    }

    /**
     * Test when request fails.
     */
    @Test
    public void findProfessorFailure() {
        String uname = AssignmentDao.getUname();
        AssignmentDao.setUname("");
        assertTrue(AssignmentDao.getInstance().
                findProfessor(1).isEmpty());
        AssignmentDao.setUname(uname);
    }


    @Test
    public void testgetSubmissionsummary() {
        List<Integer> subids = new ArrayList<>();
        subids.add(28);
        AssignmentDao a = AssignmentDao.getInstance();
        List<SubmissionSummary> sumlist = a.getSubmissionSummary(subids);
        Assert.assertTrue(sumlist.size() > 0);
        Assert.assertEquals(sumlist.get(0).getSubmissionid(), 28);
    }

    @Test
    public void testgetSubmissionsummaryforUserId() {
        int userid = 609;
        AssignmentDao a = AssignmentDao.getInstance();
        List<SubmissionSummary> sumlist = a.getSubmissionSummary(userid);
        Assert.assertTrue(sumlist.size() > 0);
    }



    /**
     * Test setter getter
     */
    @Test
    public void getterSetter() {
        String uname = AssignmentDao.getUname();
        String url = AssignmentDao.getUrl();
        String pass = AssignmentDao.getPass();
        String driver = AssignmentDao.getDriverClassName();
        AssignmentDao.setUname("test");
        assertTrue("test".equals(AssignmentDao.getUname()));
        AssignmentDao.setPass("password");
        assertTrue("password".equals(AssignmentDao.getPass()));
        AssignmentDao.setUrl("testneu.edu");
        assertTrue("testneu.edu".equals(AssignmentDao.getUrl()));
        AssignmentDao.setDriverClassName("mysql");
        assertTrue("mysql".equals(AssignmentDao.getDriverClassName()));
        AssignmentDao.setUname(uname);
        AssignmentDao.setPass(pass);
        AssignmentDao.setUrl(url);
        AssignmentDao.setDriverClassName(driver);
    }







}
