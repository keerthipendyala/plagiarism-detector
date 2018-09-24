package ast;

/**
 * Created by anju on 2/11/18.
 */

/**
 * Interface for Abstract Syntax Tree (AST) which represents the intermediate tree
 * representation of the source code of a programming language.
 */

public interface Ast {
    /**
     * Method for generating abstract syntactic structure for the source code
     * written in a programming language(Java or Python for example)
     */
    void genAst();

}
