package edu.northeastern.cs5500.models;

/**
 * @author anju
 * Creates a SubmissionScore class with submission and score information
 */


public class SubmissionScore {

    private int scoreid;
    private double averagescore;
    private double maxscore;
    private int courseid;
    private int assignmentid;
    private int student1id;
    private int student2id;
    private int submissionid1;
    private int submissionid2;
    private String reportlink;
    private int reportid;
    private String student1name;
    private String student2name;
    private String submission1name;
    private String submission2name;
    private String student1email;
    private String student2email;
    
    

    /**
     * Constuctor for SubmissionScore
     * @param submissionid
     * @param averagescore
     * @param maxscore
     * @param courseid
     * @param assignmentid
     * @param student1id
     * @param student2id
     * @param submissionid1
     * @param submissionid2
     */
    public SubmissionScore(int submissionid, double averagescore, double maxscore, int courseid, int assignmentid,
                           int student1id, int student2id, int submissionid1, int submissionid2) {
        super();
        this.scoreid = submissionid;
        this.averagescore = averagescore;
        this.maxscore = maxscore;
        this.courseid = courseid;
        this.assignmentid = assignmentid;
        this.student1id = student1id;
        this.student2id = student2id;
        this.submissionid1 = submissionid1;
        this.submissionid2 = submissionid2;
    }

    /**
     * Constructor for Submission Score
     * @param submissionid
     * @param averagescore
     * @param maxscore
     */
    public SubmissionScore(int submissionid, double averagescore, double maxscore) {
        this.scoreid = submissionid;
        this.averagescore = averagescore;
        this.maxscore = maxscore;
    }


    /**
     * Empty constructor for submission Score
     */
    public SubmissionScore() {

    }

    /**
     * Getter for average score
     * @return a double
     */
    public double getAveragescore() {
        return averagescore;
    }

    /**
     * Setter for average score
     * @param averagescore is a double
     */
    public void setAveragescore(double averagescore) {
        this.averagescore = averagescore;
    }

    /**
     * Getter for Maxscore
     * @return a double
     */
    public double getMaxscore() {
        return maxscore;
    }

    /**
     * Setter for Maxscore
     * @param maxscore is a double
     */
    public void setMaxscore(double maxscore) {
        this.maxscore = maxscore;
    }

    /**
     * Getter for Courseid
     * @return an int
     */
    public int getCourseid() {
        return courseid;
    }


    /**
     * Setter for Courseid
     * @param courseid is a int
     */
    public void setCourseid(int courseid) {
        this.courseid = courseid;
    }

    /**
     * Getter for Assignmentid
     * @return an integer
     */
    public int getAssignmentid() {
        return assignmentid;
    }


    /**
     * Setter for Assignmentid
     * @param assignmentid is a int
     */
    public void setAssignmentid(int assignmentid) {
        this.assignmentid = assignmentid;
    }

    /**
     * Getter for Student1id
     * @return a int
     */
    public int getStudent1id() {
        return student1id;
    }


    /**
     * Setter for Student1id
     * @param student1id is a int
     */
    public void setStudent1id(int student1id) {
        this.student1id = student1id;
    }

    /**
     * Getter for Student2id
     * @return an int
     */
    public int getStudent2id() {
        return student2id;
    }


    /**
     * Setter for Student2id
     * @param student2id is a int
     */
    public void setStudent2id(int student2id) {
        this.student2id = student2id;
    }
    /**
     * Getter for Submissionid1
     * @return a int
     */
    public int getSubmissionid1() {
        return submissionid1;
    }

    /**
     * Setter for Submissionid1
     * @param submissionid1 is a int
     */
    public void setSubmissionid1(int submissionid1) {
        this.submissionid1 = submissionid1;
    }

    /**
     * Getter for Submissionid2
     * @return a int
     */
    public int getSubmissionid2() {
        return submissionid2;
    }
    /**
     * Setter for Submissionid2
     * @param submissionid2 is a int
     */
    public void setSubmissionid2(int submissionid2) {
        this.submissionid2 = submissionid2;
    }

    /**
     * Getter for Reportlink
     * @return a string
     */
    public String getReportlink() {
        return reportlink;
    }

    /**
     * Setter for Reportlink
     * @param reportlink is a string
     */
    public void setReportlink(String reportlink) {
        this.reportlink = reportlink;
    }

    /**
     * Getter for Reportid
     * @return a int
     */
    public int getReportid() {
        return reportid;
    }

    /**
     * Setter for Reportid
     * @param reportid is a int
     */
    public void setReportid(int reportid) {
        this.reportid = reportid;
    }
    /**
     * Getter for Scoreid
     * @return a int
     */
	public int getScoreid() {
		return scoreid;
	}

    /**
     * Setter for Scoreid
     * @param scoreid is a int
     */
	public void setScoreid(int scoreid) {
		this.scoreid = scoreid;
	}

    /**
     * Getter for Student1name
     * @return a string
     */
	public String getStudent1name() {
		return student1name;
	}

    /**
     * Setter for Student1name
     * @param student1name is a string
     */
	public void setStudent1name(String student1name) {
		this.student1name = student1name;
	}

    /**
     * Getter for Student2name
     * @return a string
     */
	public String getStudent2name() {
		return student2name;
	}

    /**
     * Setter for Student2name
     * @param student2name is a string
     */
	public void setStudent2name(String student2name) {
		this.student2name = student2name;
	}
    /**
     * Getter for Submission1name
     * @return a string
     */
	public String getSubmission1name() {
		return submission1name;
	}

    /**
     * Setter for Submission1name
     * @param submission1name is a string
     */
	public void setSubmission1name(String submission1name) {
		this.submission1name = submission1name;
	}
    /**
     * Getter for Submission2name
     * @return a string
     */
	public String getSubmission2name() {
		return submission2name;
	}

    /**
     * Setter for Submission2name
     * @param submission2name is a string
     */
	public void setSubmission2name(String submission2name) {
		this.submission2name = submission2name;
	}
    /**
     * Getter for Student1email
     * @return a string
     */
	public String getStudent1email() {
		return student1email;
	}

    /**
     * Setter for Student1email
     * @param student1email is a string
     */
	public void setStudent1email(String student1email) {
		this.student1email = student1email;
	}
    /**
     * Getter for Student2email
     * @return a string
     */
	public String getStudent2email() {
		return student2email;
	}

    /**
     * Setter for Student2email
     * @param student2email is string
     */
	public void setStudent2email(String student2email) {
		this.student2email = student2email;
	}
}
