package edu.northeastern.cs5500.scanner;

import java.util.List;

import edu.northeastern.cs5500.models.Submission;

/**
 * @author anju
 * Interface to represent a scanner.
 */
public interface IScanner {
    /**
     * Method to initiate scan for a list of submissions
     * @param submissions
     */
    void scan(List<Submission> submissions);
}
