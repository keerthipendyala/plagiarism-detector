package edu.northeastern.cs5500.testjplag;

import edu.northeastern.cs5500.daos.AssignmentDao;
import edu.northeastern.cs5500.daos.ReportDao;
import edu.northeastern.cs5500.email.SendEmail;
import edu.northeastern.cs5500.jgit.JavaGit;
import edu.northeastern.cs5500.models.Report;
import edu.northeastern.cs5500.models.Submission;
import edu.northeastern.cs5500.scanner.JPlagServiceScanner;
import edu.northeastern.cs5500.scanner.JplagProgram;
import jplag.ExitException;
import jplag.options.CommandLineOptions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import javax.mail.MessagingException;
import java.io.File;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Test JPlagServiceScanner by mocking out calls to dependencies.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AssignmentDao.class, ReportDao.class, SendEmail.class,
        File.class,JavaGit.class,JplagProgram.class,CommandLineOptions.class})
public class JplagScannerTest {
    @Mock
    private ReportDao reportDaoMock;
    @Mock
    private AssignmentDao assignmentDaoMock;
    @Mock
    private SendEmail sendEmailMock;
    @Mock
    private File fileMock;
    @Mock
    private JavaGit javaGitMock;
    @Mock
    private JplagProgram jplagProgramMock;
    @Mock
    private CommandLineOptions commandLineOptions;


    @InjectMocks
    private JPlagServiceScanner jPlagServiceScanner;

    /**
     * Setup
     * @throws Exception
     */

    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        // setup mocks
        Whitebox.setInternalState(ReportDao.class, "instance", reportDaoMock);
        Whitebox.setInternalState(AssignmentDao.class, "instance", assignmentDaoMock);
        Whitebox.setInternalState(SendEmail.class, "instance", sendEmailMock);

        when(assignmentDaoMock.getStudentsFromSubmissionIds(Matchers.anyListOf(Integer.class))).
                thenReturn(new HashMap<>());
        when(reportDaoMock.createReport(Matchers.any(Report.class))).thenReturn(1);
        when(fileMock.mkdir()).thenReturn(true);
        when(assignmentDaoMock.findProfessor(Matchers.anyInt())).thenReturn("professor@neu.edu");
        jPlagServiceScanner.setRunThread(false);

        jPlagServiceScanner = PowerMockito.spy(jPlagServiceScanner);
        try {
            PowerMockito.doReturn(javaGitMock).when(jPlagServiceScanner,"createJavaGit");
            commandLineOptions = PowerMockito.mock(CommandLineOptions.class);
            PowerMockito.doReturn(commandLineOptions).when(jPlagServiceScanner, "createCommandLineOptions", any(String[].class),any());
            jplagProgramMock = PowerMockito.mock(JplagProgram.class);
            PowerMockito.doReturn(jplagProgramMock).when(jPlagServiceScanner,"createJplagProgram", any(CommandLineOptions.class));
        } catch (Exception e) {
            fail("mocking failed");
        }
    }

    /**
     * Test scanner
     */
    
    public void testScanner() {

        // test methods
        List<Submission> submissions = new ArrayList<>();
        submissions.add(new Submission(1, 1, "test1", 1, "http://github.com/test1.git", "testing1", 1));
        submissions.add(new Submission(2, 2, "test2", 1, "http://github.com/test2.git", "testing2", 1));
        jPlagServiceScanner.scan(submissions);

        // verify submissions fetched
        verify(assignmentDaoMock, times(1)).getStudentsFromSubmissionIds(Arrays.asList(1,2));

        // test right report created
        ArgumentCaptor<Report> argument = ArgumentCaptor.forClass(Report.class);
        verify(reportDaoMock).createReport(argument.capture());
        long reportId = argument.getValue().getReportId();

        // verify jgit call
        ArgumentCaptor<String> repoArgs = ArgumentCaptor.forClass(String.class);
        verify(javaGitMock, times(2)).
                    cloneRepository(repoArgs.capture(),Matchers.anyString(),Matchers.anyString());
        argument.getAllValues().contains(submissions.get(0).getsubmissionlink());
        argument.getAllValues().contains(submissions.get(1).getsubmissionlink());

        try {
            verify(jplagProgramMock, times(1)).run();
        } catch (ExitException e) {
            fail("unexpected exception");
        }

        // test report updated correctly
        ArgumentCaptor<Report> updatedReportArg = ArgumentCaptor.forClass(Report.class);
        verify(reportDaoMock).updateReport(updatedReportArg.capture());
        assertEquals(reportId, argument.getValue().getReportId());
        assertEquals(true, updatedReportArg.getValue().isSuccessful());
        assertEquals(true, updatedReportArg.getValue().getReportlink().contains(String.valueOf(reportId)));

        // test emails
        verify(assignmentDaoMock, times(1)).findProfessor(1);

        ArgumentCaptor<StringBuilder> emailMessage = ArgumentCaptor.forClass(StringBuilder.class);
        ArgumentCaptor<String> toEmail = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> emailSubject = ArgumentCaptor.forClass(String.class);
        try {
            verify(sendEmailMock).sendEmail(emailMessage.capture(), toEmail.capture(), emailSubject.capture());
        } catch (MessagingException e) {
            fail("email raised exception");
        }
        assertEquals(true, toEmail.getValue().contentEquals("professor@neu.edu"));
        assertEquals(true, emailSubject.getValue().contentEquals("Scanning Result"));

    }
}
