package edu.northeastern.cs5500.testjplag;

import edu.northeastern.cs5500.daos.SubmissionScoreDao;
import edu.northeastern.cs5500.models.*;
import edu.northeastern.cs5500.scanner.JplagProgram;
import edu.northeastern.cs5500.scanner.JplagReport;
import jplag.*;
import jplag.Report;
import jplag.Submission;
import jplag.clustering.Cluster;
import jplag.options.Options;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.io.File;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Junit test for jplag report.
 * @author anju
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({JplagReport.class, SubmissionScoreDao.class, AllMatches.class,
        Report.class, JplagProgram.class, SortedVector.class,
        File.class, Language.class, Cluster.class, Options.class})
public class JplagReportTest {

    @Captor
    private ArgumentCaptor<List<SubmissionScore>> argument;

    /**
     * Test setting student
     */
    @Test
    public void testSetStudent() throws Exception {
        Report reportMock = mock(Report.class);
        PowerMockito.whenNew(Report.class).withAnyArguments().thenReturn(reportMock);
        JplagProgram programMock = mock(JplagProgram.class);
        Language languageMock = mock(Language.class);

        JplagReport report = new JplagReport(programMock, languageMock);
        Map<String, String> students = new HashMap<>();
        students.put("sub1", "student1");
        students.put("sub2", "student2");
        report.setSubStudents(students);

        Object subStudents = Whitebox.getInternalState(report, "subStudents");
        assertNotNull(subStudents);
        assertEquals(subStudents, students);
    }


    /**
     * Test write method
     */
    @Test
    public void testWrite() throws Exception {
        // mock
        Report reportMock = mock(Report.class);
        PowerMockito.whenNew(Report.class).withAnyArguments().thenReturn(reportMock);
        //doNothing().when(reportMock).write(any(),any(),any(),any(),any(),any(),any());
        PowerMockito.suppress(methodsDeclaredIn(Report.class));

        JplagProgram programMock = mock(JplagProgram.class);
        Language languageMock = mock(Language.class);
        JplagReport report = new JplagReport(programMock, languageMock);
        long timeStamp = new Date().getTime();
        edu.northeastern.cs5500.models.Report scanReport =
                new edu.northeastern.cs5500.models.Report(timeStamp);
        report.setScanReport(scanReport);
        // student reports
        Map<String, String> students = new HashMap<>();
        students.put("_1_1_1_1", "student1");
        students.put("_1_1_2_2", "student2");
        report.setSubStudents(students);

        Comparator<AllMatches> comparator = (o1, o2) -> Float.compare(o1.roundedPercent(), o2.roundedPercent());
        SortedVector<AllMatches> testAvgMatches = new SortedVector<>(comparator);
        SortedVector<AllMatches> testMaxMatches = new SortedVector<>(comparator);
        AllMatches allMatches1 = mock(AllMatches.class);
        Submission sub1 = mock(Submission.class);
        Submission sub2 = mock(Submission.class);
        allMatches1.subA = sub1;
        allMatches1.subB = sub2;
        List<String> names = new ArrayList<>(students.keySet());
        sub1.name = names.get(0);
        sub2.name = names.get(1);
        doReturn(90f).when(allMatches1).roundedPercent();
        doReturn(90f).when(allMatches1).roundedPercentMaxAB();
        testAvgMatches.add(allMatches1);
        testMaxMatches.add(allMatches1);

        SubmissionScoreDao daoMock = mock(SubmissionScoreDao.class);
        Whitebox.setInternalState(SubmissionScoreDao.class,"instance", daoMock);

        report.write(mock(File.class), new int[0], testAvgMatches,
                testMaxMatches, new SortedVector<>(comparator),
                mock(Cluster.class), mock(Options.class));

        verify(daoMock).createSubmissionScore(argument.capture());

        SubmissionScore generatedScore = argument.getValue().get(0);
        assertTrue(generatedScore.getMaxscore() == allMatches1.roundedPercentMaxAB());
        assertTrue(generatedScore.getAveragescore() == allMatches1.roundedPercent());
    }

}
