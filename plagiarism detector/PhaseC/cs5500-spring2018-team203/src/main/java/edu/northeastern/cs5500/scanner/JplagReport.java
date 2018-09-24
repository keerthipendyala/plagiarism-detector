package edu.northeastern.cs5500.scanner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.cs5500.daos.SubmissionScoreDao;
import edu.northeastern.cs5500.models.SubmissionScore;
import jplag.AllMatches;
import jplag.Language;
import jplag.Program;
import jplag.Report;
import jplag.SortedVector;
import jplag.clustering.Cluster;
import jplag.options.Options;

/**
 * @author anju
 * Class that represtens custom implemetation of Jplag Report
 */
public class JplagReport extends Report {

    private Map<String, String> subStudents;
    private edu.northeastern.cs5500.models.Report scanReport;

    /**
     * Constructor for Jplag
     *
     * @param program
     * @param language
     */
    public JplagReport(Program program, Language language) {
        super(program, language);


    }

    /**
     * Write method from Jplag override.
     *
     * @param f
     * @param dist
     * @param avgmatches
     * @param maxmatches
     * @param minmatches
     * @param clustering
     * @param options
     * @throws jplag.ExitException
     */
    public void write(File f, int[] dist, SortedVector<AllMatches> avgmatches, SortedVector<AllMatches> maxmatches,
                      SortedVector<AllMatches> minmatches, Cluster clustering, Options options)
            throws jplag.ExitException {
        updateLatestSubmissionScore(avgmatches, maxmatches);
        replaceReportStrings(avgmatches);
        replaceReportStrings(maxmatches);
        super.write(f, dist, avgmatches, maxmatches, minmatches, clustering, options);

    }

    /**
     * Set student map.
     * @param subStudents
     */
    public void setSubStudents(Map<String, String> subStudents) {
        this.subStudents = subStudents;
    }

    /**
     * Calculate and update submission scores in the database.
     * @param avgmatches
     * @param maxmatches
     *
     */
    public void updateLatestSubmissionScore(SortedVector<AllMatches> avgmatches,
                                            SortedVector<AllMatches> maxmatches) {
        Map<String, Float> avgScore = new HashMap<>();
        Map<String, Float> maxScore = new HashMap<>();

        updateScoreInMap(avgmatches, avgScore, false);
        updateScoreInMap(maxmatches, maxScore, true);

        List<SubmissionScore> scoreList = new ArrayList<>();
        for (String subId : avgScore.keySet()) {
        	String[] splt=subId.split("_");
            //format crsid_assig_stud1_subid_stud2_sub2id
        	int courseid=Integer.parseInt(splt[0]);
        	int assignmentid=Integer.parseInt(splt[1]);
        	int student1=Integer.parseInt(splt[2]);
        	int subid1=Integer.parseInt(splt[3]);
        	int student2=Integer.parseInt(splt[4]);
        	int subid2=Integer.parseInt(splt[5]);
            SubmissionScore score = new SubmissionScore();
            score.setAssignmentid(assignmentid);
            score.setCourseid(courseid);
            score.setStudent1id(student1);
            score.setStudent2id(student2);
            score.setCourseid(courseid);
            score.setMaxscore(maxScore.get(subId));
            score.setAveragescore(avgScore.get(subId));
            score.setSubmissionid1(subid1);
            score.setSubmissionid2(subid2);
            score.setReportid(scanReport.getId());
            scoreList.add(score);
        }

        SubmissionScoreDao.getInstance().createSubmissionScore(scoreList);
    }

    /**
     *  Method to compute score based on matches
     * @param matches is a sorted vector
     * @param scores is a map
     * @param max is a boolean
     */
    private void updateScoreInMap(SortedVector<AllMatches> matches, Map<String, Float> scores, boolean max) {
        for (AllMatches match : matches) {
            String subId1Str = match.subA.name;
            String[] sub1 = subId1Str.split("_");
            String subId2Str = match.subB.name;
            String[] sub2 = subId2Str.split("_");

            // this key will be crsid_assig_stud1_subid_stud2_sub2id

            String combinedKey = sub1[1] + "_" + sub1[2] + "_" + sub1[3] + "_" + sub1[4] + "_" + sub2[3] + "_" + sub2[4];
            float score = max ? match.roundedPercentMaxAB() : match.roundedPercent();
            scores.putIfAbsent(combinedKey, score);
            if (scores.get(combinedKey) < score) {
                scores.put(combinedKey, score);
            }
        }
        
    }



    /**
     * Method to update report string for more clarity.
     * @param matches is a sorted vector
     */
    private void replaceReportStrings(SortedVector<AllMatches> matches) {
        for (AllMatches match : matches) {
            if (subStudents.get(match.subA.name) != null) {
                match.subA.name = subStudents.get(match.subA.name);
            }
            if (subStudents.get(match.subB.name) != null) {
                match.subB.name = subStudents.get(match.subB.name);
            }
        }
    }

    /**
     * Scan report setter.
     * @param scanReport is report
     */
    public void setScanReport(edu.northeastern.cs5500.models.Report scanReport) {
        this.scanReport = scanReport;
    }
}

