package codegenerator;

import ast.Program;
import ast.RecordField;
import ast.definitions.FuncDefinition;
import ast.definitions.VarDefinition;
import ast.expressions.*;
import ast.expressions.literals.CharLiteral;
import ast.expressions.literals.IntLiteral;
import ast.expressions.literals.RealLiteral;
import ast.statements.*;
import ast.types.*;
import visitor.Visitor;

public abstract class AbstractCGVisitor<TP, TR> implements Visitor<TP, TR> {

    public void throwException(String type) {
        String message = "Undefined template visit(" + type + ") for the code function " +
                this.getClass().getName();
        assert false: message;
        throw new UnsupportedOperationException(message);
    }


    // Program ===============================================================

    @Override
    public TR visit(Program program, TP parameter) {
        throwException("Program");
        return null;
    }

    // Definitions ===============================================================

    @Override
    public TR visit(FuncDefinition funcDefinition, TP parameter) {
        throwException("Function Definition");
        return null;
    }

    @Override
    public TR visit(VarDefinition varDefinition, TP parameter) {
        throwException("Variable Definition");
        return null;
    }

    // Expressions ===============================================================

    @Override
    public TR visit(Arithmetic arithmetic, TP parameter) {
        throwException("Arithmetic Expression");
        return null;
    }

    @Override
    public TR visit(ArrayAccess arrayAccess, TP parameter) {
        throwException("Array Access");
        return null;
    }

    @Override
    public TR visit(Cast cast, TP parameter) {
        throwException("Cast");
        return null;
    }

    @Override
    public TR visit(Comparison comparison, TP parameter) {
        throwException("Comparison");
        return null;
    }

    @Override
    public TR visit(FieldAccess fieldAccess, TP parameter) {
        throwException("Field Access");
        return null;
    }

    @Override
    public TR visit(Logical logical, TP parameter) {
        throwException("Logical Expression");
        return null;
    }

    @Override
    public TR visit(Modulus modulus, TP parameter) {
        throwException("Modulus");
        return null;
    }

    @Override
    public TR visit(Negation negation, TP parameter) {
        throwException("Negation");
        return null;
    }

    @Override
    public TR visit(UnaryMinus minus, TP parameter) {
        throwException("Unary Minus");
        return null;
    }

    @Override
    public TR visit(Variable variable, TP parameter) {
        throwException("Variable");
        return null;
    }

    // Expressions (Literals) ===============================================================

    @Override
    public TR visit(CharLiteral charLiteral, TP parameter) {
        throwException("Char Literal");
        return null;
    }

    @Override
    public TR visit(IntLiteral intLiteral, TP parameter) {
        throwException("Int Literal");
        return null;
    }

    @Override
    public TR visit(RealLiteral realLiteral, TP parameter) {
        throwException("Real Literal");
        return null;
    }

    // Statements ===============================================================

    @Override
    public TR visit(Assignment assignment, TP parameter) {
        throwException("Assignment");
        return null;
    }

    @Override
    public TR visit(FunctionInvocation functionInvocation, TP parameter) {
        throwException("Function Invocation");
        return null;
    }

    @Override
    public TR visit(IfElse ifElse, TP parameter) {
        throwException("If/Else");
        return null;
    }

    @Override
    public TR visit(Print print, TP parameter) {
        throwException("Print");
        return null;
    }

    @Override
    public TR visit(Read read, TP parameter) {
        throwException("Read");
        return null;
    }

    @Override
    public TR visit(Return returnStatement, TP parameter) {
        throwException("Return");
        return null;
    }

    @Override
    public TR visit(While whileStatement, TP parameter) {
        throwException("While");
        return null;
    }

    // Types ===============================================================

    @Override
    public TR visit(ArrayType arrayType, TP parameter) {
        throwException("Array Type");
        return null;
    }

    @Override
    public TR visit(CharType charType, TP parameter) {
        throwException("Char Type");
        return null;
    }

    @Override
    public TR visit(ErrorType errorType, TP parameter) {
        throwException("Error Type");
        return null;
    }

    @Override
    public TR visit(FunctionType functionType, TP parameter) {
        throwException("Function Type");
        return null;
    }

    @Override
    public TR visit(IntType intType, TP parameter) {
        throwException("Int Type");
        return null;
    }

    @Override
    public TR visit(RealType realType, TP parameter) {
        throwException("Real Type");
        return null;
    }

    @Override
    public TR visit(StructType structType, TP parameter) {
        throwException("Struct Type");
        return null;
    }

    @Override
    public TR visit(VoidType voidType, TP parameter) {
        throwException("Void Type");
        return null;
    }

    @Override
    public TR visit(RecordField recordField, TP parameter) {
        throwException("Record Field");
        return null;
    }
}
