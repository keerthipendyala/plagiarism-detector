package edu.northeastern.cs5500.scanner;

import java.util.Map;

import edu.northeastern.cs5500.models.Report;
import jplag.ExitException;
import jplag.Program;
import jplag.options.Options;

/**
 * @author anju
 * Class that represents the custom implementation of Jplag Program
 */
public class JplagProgram extends Program {
    private Map<String, String> subStudents;

    /**
     * Constructor for JplagProgram
     * @param options
     * @throws ExitException
     */
    public JplagProgram(Options options) throws ExitException {
        super(options);
        report = new JplagReport(this, get_language());

    }


    /**
     * Set submissionid, studentName
     * @param subStudents
     */
    public void setSubStudents(Map<String, String> subStudents) {
        this.subStudents = subStudents;
        ((JplagReport) report).setSubStudents(subStudents);
    }

    /**
     * Scan report set
     * @param scanReport
     */
    public void setScanReport(Report scanReport) {
        ((JplagReport) report).setScanReport(scanReport);
    }

    /**
     * Override run method for mocking
     * @throws ExitException
     */
    public void run() throws ExitException {
        super.run();
    }
}
