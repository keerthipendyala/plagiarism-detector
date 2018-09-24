package ast;

/**
 * Created by anju on 2/11/18.
 */

/**
 * Interface for AstComparator: The CopyCatch software utilizes code comparison technology for
 * plagiarism detection and efficient evaluation. The interface provide the methods to compare two
 * abstract syntactic structures in same programming language for Plagiarism detection.
 */
public interface AstComparator {

    /**
     * Method signature for comparing Asts. The method compares two asts by implementing several
     * algorithms for finding similarity in the nodes
     */
    void compareAst(Ast ast1, Ast ast2);

    /**
     * Method signature for generating a clone using Abstract syntax trees
     * This generates a clone that has been detected using conventional transformational methods.
     */
    void getAstClone();
}
