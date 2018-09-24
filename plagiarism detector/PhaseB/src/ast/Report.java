package ast;

/**
 * This interface has the methods and properties that initiates programming assignment comparison that has same course assignment number
 * the comparison can be done on one to one or across multiple submissions
 * and it also has a method to view the generated reports outcome in percentage
 *
 * @author harip
 */
public interface Report {

    /**
     * This method initiates the comparison of two programming assignments that has same assignment number
     * that are submitted in the CopyCatch WebSite
     */
    void generateReport();

    /**
     * This method returns the score that after
     *
     * @return : a double value which represents the percentage of similarity between the two programming assignments
     */
    double getScore();
}
