package edu.northeastern.cs5500.scanner;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.apache.commons.io.FileUtils;

import edu.northeastern.cs5500.daos.AssignmentDao;
import edu.northeastern.cs5500.daos.CourseDao;
import edu.northeastern.cs5500.daos.ReportDao;
import edu.northeastern.cs5500.daos.SubmissionScoreDao;
import edu.northeastern.cs5500.email.SendEmail;
import edu.northeastern.cs5500.jgit.JavaGit;
import edu.northeastern.cs5500.models.Course;
import edu.northeastern.cs5500.models.Report;
import edu.northeastern.cs5500.models.Submission;
import edu.northeastern.cs5500.models.SubmissionScore;
import edu.northeastern.cs5500.models.SubmissionSummary;
import jplag.ExitException;
import jplag.Program;
import jplag.options.CommandLineOptions;

/**
 * @author anju
 * Scanner that launches scanning using Jplag and
 * generates the report.
 */
public class JPlagServiceScanner implements IScanner {

    private String domainName = "http://%s/api/report/";
    private static String host="";
    public static final String TMP_DIR = System.getProperty("java.io.tmpdir");
    private static String reportPath = FileUtils.getTempDirectoryPath() + "/reports/";
    Logger logger = Logger.getLogger(JPlagServiceScanner.class.getName());
    private boolean runThread = true;
    private final String adminMail = "copycatchteam@gmail.com";

    /**
     * Get the generated report base-directory
     *
     * @return base directory path
     */
    public static String getReportPath() {
        return reportPath;
    }


    /**
     * Generate domain name based on the host name.
     *
     * @param host
     */
    public void setDomainName(String host) {
    	JPlagServiceScanner.host=host;
        domainName = String.format(domainName, host);
    }

    /**
     * Method to scan and generate directories.
     *
     * @param submissions
     */
    @Override
    public void scan(List<Submission> submissions) {
        // run scanning in a separate thread
        if (runThread) {
            Thread thread = new Thread(() -> {
                startScan(submissions, new ArrayList<>());
            });
            // launch separate thread if needed
            thread.start();
        } else {
            startScan(submissions, new ArrayList<>());
        }
    }

    public void incrementalScan(List<Submission> submissions, List<Submission> toSkip) {
        Thread thread = new Thread(() -> {
            startScan(submissions, toSkip);
        });
        thread.start();
    }


    /**
     * Start scanning by invoking jplag
     *
     * @param submissions
     */
    private synchronized void startScan(List<Submission> submissions, List<Submission> toSkip) {
        JavaGit jg = createJavaGit();
        List<Integer> subIds = submissions.stream().map(s -> s.getid()).
                collect(Collectors.toList());
        List<SubmissionSummary> submissionSummary = AssignmentDao.getInstance().
                getSubmissionSummary(subIds);
        Map<String, String> subStudentMap = mapSubmissionSummary(submissionSummary);

        final long timeStamp = new Date().getTime();
        Report report = new Report(timeStamp);

        int id = ReportDao.getInstance().createReport(report);
        //Report Creation failed
        if (id == -1) {
            logger.log(Level.SEVERE, "Cannot create a report.");
            return;
        } else {
            report.setId(id);
        }
        String assignmentName = "";
        String scanInputPath = null;
        // create result directory
        File resultFolder = new File(reportPath + timeStamp);
        resultFolder.mkdir();
        try {
            List<String> warnings = new ArrayList<>();


            for (Submission s : submissions) {
                if (s.getsubmissionlink() == null) {
                    logger.log(Level.SEVERE, "submission with id - folder missing" + s.getid());
                    warnings.add("The submission folder has been missing for the student with the id: " + s.getstudentid());
                    continue;
                }

                assignmentName = "Assignment"+ timeStamp;
                jg.cloneRepository(s.getsubmissionlink(), "_" + s.getcourseid() + "_" + s.getAssignmentid() + "_" + s.getstudentid() + "_" + s.getid(), assignmentName);

            }

            //scan input path
            scanInputPath = TMP_DIR + File.separator + "jplag" + File.separator + assignmentName;
            //Start scanning
            boolean scanningFailed = false;
            boolean unsupportedLanguage = false;
            int courseIds = 0;
            for (Submission s : submissions) {
                courseIds = s.getcourseid();
            }

            Course c = CourseDao.getInstance().getCourse(courseIds);
            if (c.getProglanguage() == null || (!c.getProglanguage().equals("python3") && !c.getProglanguage().equals("c/c++"))) {
                scanningFailed = true;
                unsupportedLanguage = true;
            } else {
                try {

                    String scanOptions = "-l " + c.getProglanguage() + " -r %s -s %s -t 3";
                    scanOptions = String.format(scanOptions, resultFolder.getAbsolutePath(), scanInputPath);
                    CommandLineOptions options = createCommandLineOptions(scanOptions.split(" "), null);
                    boolean incremental = toSkip.size() > 0;
                    Program program;
                    if (incremental == true) {
                        program = createJplagProgram(options, true);
                        ((JplagIncrementalScanner) program).setSkipSubmissions(toSkip);
                        ((JplagIncrementalScanner) program).setSubStudents(subStudentMap);
                        ((JplagIncrementalScanner) program).setScanReport(report);
                    } else {
                        program = createJplagProgram(options, false);
                        ((JplagProgram) program).setSubStudents(subStudentMap);
                        ((JplagProgram) program).setScanReport(report);
                    }
                    logger.log(Level.INFO, timeStamp + " scan initialize ok");
                    program.run();

                } catch (ExitException ex) {
                    scanningFailed = true;
                    logger.log(Level.SEVERE, timeStamp + " Scan Error: " + ex.getReport());
                    warnings.add("Scan error " + ex.getReport());
                }
            }

            // Send emails to corresponding professors with the report link once the scanning is over.
            StringBuilder message = new StringBuilder();

            // copy report to static folder.
            if (!scanningFailed) {
            	String reportlink = domainName + timeStamp + "/index.html";
                String dummyLink="http://"+JPlagServiceScanner.host+"/project/index.html#/getreport/"+report.getId();
                message.append("Request to scanning has been completed. You can view the report at " +
                		dummyLink);
                report.setReportlink(reportlink);
                report.setDummylink(dummyLink);
                report.setSuccessful(true);
                //Updating the report after scanning.
                ReportDao.getInstance().updateReport(report);
            }


            // notify on scanning failure
            if (scanningFailed && unsupportedLanguage) {
                message.append("Scanning called for an unsupported language.");
            } else if (scanningFailed) {
                message.append("Scanning failed with errors. Please contact your system administrator.");
            }

            if (warnings.size() > 0) {
                message.append("\n ****** Scanner Warnings ****** :\n");
                message.append(String.join("\n", warnings));
            }


            if (scanningFailed == false) {

                List<SubmissionScore> subscores = SubmissionScoreDao.getInstance().getSubmissionScores(report.getId());
                boolean hasViolations = false;
                for (SubmissionScore s : subscores) {
                    if (s.getMaxscore() >= c.getThreshold()) {
                        hasViolations = true;
                        break;
                    }
                }
                // Mail to professor on finding a violation
                if (hasViolations == true) {

                    String toEmailAddresses = AssignmentDao.getInstance().findProfessor(courseIds);

                    try {
                        SendEmail.getInstance().sendEmail(message, toEmailAddresses,
                                "Scanning Result: Plagiarism Detected");
                    } catch (MessagingException e) {
                        logger.log(Level.SEVERE, "Sending out email failed. Reason : " + e.getMessage());
                    }

                }
            } else {
                try {
                    SendEmail.getInstance().sendEmail(message, adminMail,
                            "Scanning Failed!");
                } catch (MessagingException e) {
                    logger.log(Level.SEVERE, "Sending out email failed. Reason : " + e.getMessage());
                }
            }


        } finally {
            //To cleanup directory after scanning
            if (scanInputPath != null) {
                jg.deleteJplag(scanInputPath);
            }
        }
    }

    /**
     * Method to map the submission summary
     * @param submissionSummaries is a list of Submission summary
     * @return a Map
     */
    private Map<String, String> mapSubmissionSummary(
            List<SubmissionSummary> submissionSummaries) {
        Map<String, String> map = new HashMap<>();
        for (SubmissionSummary summary : submissionSummaries) {

            String key = "student_" + summary.getCourseid() + "_" + summary.getAssignmentid()
                    + "_" + summary.getStudentid() + "_" + summary.getSubmissionid();
            String label = summary.getStudentname() + "[" + summary.getCourseno() + "/" +
                    summary.getSemestername() + "/" + summary.getAssignmentname() + "/" + summary.getSubmissionid() + "]";
            map.put(key, label);
        }
        return map;
    }

    /**
     * Get java git
     *
     * @return a JavaGit
     */
    protected JavaGit createJavaGit() {
        return new JavaGit();
    }

    /**
     * Get jplag program
     *
     * @param options are command line options
     * @return a Program
     * @throws ExitException if fails
     */
    protected Program createJplagProgram(CommandLineOptions options, boolean incremental) throws ExitException {
        return incremental ? new JplagIncrementalScanner(options) : new JplagProgram(options);
    }

    /**
     * Create jplag command options
     *
     * @param options  are command line options
     * @param cmdInString is a string
     * @return a command line option
     * @throws ExitException if fails
     */
    protected CommandLineOptions createCommandLineOptions(String[] options,
                                                          String cmdInString) throws ExitException {
        return new CommandLineOptions(options, cmdInString);
    }

    /**
     * Run as a different thread
     *
     * @return a boolean
     */
    public boolean isRunThread() {
        return runThread;
    }

    /**
     * Run in same thread
     *
     * @param runThread is a boolean
     */
    public void setRunThread(boolean runThread) {
        this.runThread = runThread;
    }
}

