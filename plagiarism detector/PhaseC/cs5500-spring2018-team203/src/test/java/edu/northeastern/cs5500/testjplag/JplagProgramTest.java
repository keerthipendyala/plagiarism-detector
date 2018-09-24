package edu.northeastern.cs5500.testjplag;

import edu.northeastern.cs5500.scanner.JplagProgram;
import edu.northeastern.cs5500.scanner.JplagReport;
import jplag.Language;
import jplag.Program;
import jplag.Report;
import jplag.options.Options;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.support.membermodification.MemberMatcher.constructor;
import static org.powermock.api.support.membermodification.MemberMatcher.methodsDeclaredIn;

/**
 * Class to test Jplag program
 * @author anju
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({JplagProgram.class, Program.class, JplagReport.class, Report.class, Options.class, Language.class})
public class JplagProgramTest {
    private JplagProgram jplagProgram;

    @Before
    public void setup() throws Exception {
        Report reportMock = mock(Report.class);
        Program programMock = mock(Program.class);
        PowerMockito.whenNew(Report.class).withAnyArguments().thenReturn(reportMock);
        PowerMockito.whenNew(Program.class).withAnyArguments().thenReturn(programMock);
        PowerMockito.suppress(methodsDeclaredIn(Program.class));
        PowerMockito.suppress(constructor(Program.class));
        doReturn(mock(Language.class)).when(programMock).get_language();
        jplagProgram = new JplagProgram(mock(Options.class));
    }

    /**
     * Test setting students
     */
    @Test
    public void testSetStudents() {
        long id = new Date().getTime();
        edu.northeastern.cs5500.models.Report scanReport = new edu.northeastern.cs5500.models.Report(id);
        Map<String, String> students = new HashMap<>();
        students.put("sub1", "student1");
        students.put("sub2", "student2");
        jplagProgram.setSubStudents(students);
        Object subStudent = Whitebox.getInternalState(jplagProgram, "subStudents");
        assertNotNull(subStudent);
        assertEquals(subStudent,students);
    }

    /**
     * Test scan report setting.
     */
    @Test
    public void testScanReport() {
        edu.northeastern.cs5500.models.Report scanReport =
                new edu.northeastern.cs5500.models.Report(new Date().getTime());
        jplagProgram.setScanReport(scanReport);
        Object report = Whitebox.getInternalState((JplagReport) jplagProgram.report, "scanReport");
        assertNotNull(report);
        assertEquals(scanReport.getReportId(), ((edu.northeastern.cs5500.models.Report) report).getReportId());
    }
}
