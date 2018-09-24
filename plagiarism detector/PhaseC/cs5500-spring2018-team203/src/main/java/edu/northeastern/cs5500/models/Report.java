package edu.northeastern.cs5500.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author anju
 * Creates a report with report information
 */
@Entity(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private long reportid;
    private String reportlink;
    private boolean successful;
    private String dummylink;

    /**
     * Comntructor for Report
     * @param id is an integer
     * @param reportId is a long
     * @param reportlink is a string
     * @param successful is a boolean
     */
    public Report(int id, long reportId, String reportlink, boolean successful) {
        this.id = id;
        this.reportid = reportId;
        this.reportlink = reportlink;
        this.successful = successful;
    }

    /**
     * Constructor for Report with report link
     * @param reportid is a long
     */
    public Report(long reportid) {
        this.reportid = reportid;

    }

    /**
     * Getter for id
     * @return an int
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for id
     * @param id is an int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for report id
     * @return a long
     */
    public long getReportId() {
        return reportid;
    }

    /**
     * Setter for report id
     * @param reportId is an int
     */
    public void setReportId(int reportId) {
        this.reportid = reportId;
    }

    /**
     * Getter for report link
     * @return a string
     */
    public String getReportlink() {
        return reportlink;
    }

    /**
     * Setter for report link
     * @param reportlink is a string
     */
    public void setReportlink(String reportlink) {
        this.reportlink = reportlink;
    }

    /**
     * Getter for is successful
     * @return a boolena
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * Setter for set successful
     * @param successful is a boolean
     */
    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    /**
     * Getter for dummy link
     * @return a string
     */
	public String getDummylink() {
		return dummylink;
	}

    /**
     * Setter for Summy link
     * @param dummylink is a string
     */
	public void setDummylink(String dummylink) {
		this.dummylink = dummylink;
	}
}
