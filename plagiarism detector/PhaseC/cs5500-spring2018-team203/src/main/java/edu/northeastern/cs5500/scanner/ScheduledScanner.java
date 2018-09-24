package edu.northeastern.cs5500.scanner;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import edu.northeastern.cs5500.daos.SubmissionDao;
import edu.northeastern.cs5500.models.Submission;

/**
 * Scheduler that invokes scanning on a schedule
 */
@Component
public class ScheduledScanner {
    private static boolean scanEnabled = false;
    private static String hostName = "team203copycatch.us-east-1.elasticbeanstalk.com";

    @Scheduled(fixedRate = 600000)
    public void initiateScanning() {
        // scan only only machines are been enabled for scanning
        if (!scanEnabled) {
            return;
        }
        List<Submission> toScan = SubmissionDao.getInstance().getLatestUnscannedSubmissions();
        List<Submission> scanned = SubmissionDao.getInstance().getAllScannedSubmissions();
        Set<Integer> assignmentIds = toScan.stream().map(l -> l.getAssignmentid()).collect(Collectors.toSet());
        for (int assignmentId: assignmentIds) {
            List<Submission> scannedSubmissions = scanned.stream().filter(l -> l.getAssignmentid() == assignmentId)
                    .collect(Collectors.toList());
            List<Submission> submissions = SubmissionDao.getInstance().getLatestAssignmentSubmissions(assignmentId);
            JPlagServiceScanner scanner = new JPlagServiceScanner();
            scanner.setDomainName(hostName);
            scanner.incrementalScan(submissions, scannedSubmissions);
        }
    }

    /**
     * Method to check if scanning is enabled
     * @return a boolean
     */
    public static boolean isScanEnabled() {
        return scanEnabled;
    }

    /**
     * Method to set scan enabled
     * @param scanEnabled a boolean
     */
    public static void setScanEnabled(boolean scanEnabled) {
        ScheduledScanner.scanEnabled = scanEnabled;
    }
}
