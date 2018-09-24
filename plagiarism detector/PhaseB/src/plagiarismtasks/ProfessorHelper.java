package plagiarismtasks;

/**
 * Created by anju on 2/11/18.
 */

/**
 * Interface for functions that assist the user(Professor client of the software application)
 * to check for plagiarism.
 */
public interface ProfessorHelper {

    /*
    Method signature for comparing files that has been chosen by the user
     */
    void compareFiles();

    /*
    Method signature for getting results of plagiarism check.
     */
    void getResults();

}
