package data;

import courses.Submission;

/**
 * @author karan sharma
 * This interface will have methods that will have the functionality to get data from
 * repository and parse its data accordingly for file compare
 */
public interface GrabData {

    /**
     * This method will grabData from a link specified in the submission
     */
    public void grabData();


    /**
     * @param submission Which denotes each submission made by a student
     * @desc This method will fetch the file from the repository link and parse the data so that
     * it can be sent for compare
     */
    public void parseDataFromLinks(Submission submission);

}
