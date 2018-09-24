package ast;

/**
 * Created by anju on 2/11/18.
 */

/**
 * Interface for Generating tokens from Abstract syntax tree structures
 */
public interface GenerateTokens {

    /**
     * Method signature for generating tokens.
     * The method will take in two Abstract syntax trees and will generate a
     * list of tokens.
     *
     * @param ast1 is an Ast
     * @param ast2 is an Ast
     */
    void generateTokens(Ast ast1, Ast ast2);
}
