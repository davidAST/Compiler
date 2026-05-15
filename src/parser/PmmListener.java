// Generated from C:/Users/david/Desktop/Uniovi/3º/2º Cuatrimestre/DLP/Project/src/parser/Pmm.g4 by ANTLR 4.13.2
package parser;

    import ast.*;
    import ast.definitions.*;
    import ast.expressions.*;
    import ast.statements.*;
    import ast.types.*;
    import ast.expressions.literals.*;
    import java.util.Set;
    import java.util.HashSet;
    import errorhandler.ErrorHandler;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PmmParser}.
 */
public interface PmmListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PmmParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(PmmParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link PmmParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(PmmParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link PmmParser#variables}.
	 * @param ctx the parse tree
	 */
	void enterVariables(PmmParser.VariablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PmmParser#variables}.
	 * @param ctx the parse tree
	 */
	void exitVariables(PmmParser.VariablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PmmParser#varDefinition}.
	 * @param ctx the parse tree
	 */
	void enterVarDefinition(PmmParser.VarDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PmmParser#varDefinition}.
	 * @param ctx the parse tree
	 */
	void exitVarDefinition(PmmParser.VarDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PmmParser#params}.
	 * @param ctx the parse tree
	 */
	void enterParams(PmmParser.ParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PmmParser#params}.
	 * @param ctx the parse tree
	 */
	void exitParams(PmmParser.ParamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PmmParser#functionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDefinition(PmmParser.FunctionDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PmmParser#functionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDefinition(PmmParser.FunctionDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PmmParser#mainDefinition}.
	 * @param ctx the parse tree
	 */
	void enterMainDefinition(PmmParser.MainDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PmmParser#mainDefinition}.
	 * @param ctx the parse tree
	 */
	void exitMainDefinition(PmmParser.MainDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PmmParser#returnType}.
	 * @param ctx the parse tree
	 */
	void enterReturnType(PmmParser.ReturnTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PmmParser#returnType}.
	 * @param ctx the parse tree
	 */
	void exitReturnType(PmmParser.ReturnTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PmmParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(PmmParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PmmParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(PmmParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PmmParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(PmmParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PmmParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(PmmParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PmmParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(PmmParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PmmParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(PmmParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PmmParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(PmmParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PmmParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(PmmParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PmmParser#recordField}.
	 * @param ctx the parse tree
	 */
	void enterRecordField(PmmParser.RecordFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link PmmParser#recordField}.
	 * @param ctx the parse tree
	 */
	void exitRecordField(PmmParser.RecordFieldContext ctx);
}