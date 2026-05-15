package visitor;

import ast.definitions.*;
import ast.expressions.*;
import ast.expressions.literals.*;
import ast.statements.*;
import ast.types.*;
import ast.*;

public interface Visitor<TP, TR> {

    // Program
    public TR visit(Program program, TP parameter);

    // Definitions
    public TR visit(FuncDefinition funcDefinition, TP parameter);
    public TR visit(VarDefinition varDefinition, TP parameter);

    // Expressions
    public TR visit(Arithmetic arithmetic, TP parameter);
    public TR visit(ArrayAccess arrayAccess, TP parameter);
    public TR visit(Cast cast, TP parameter);
    public TR visit(Comparison comparison, TP parameter);
    public TR visit(FieldAccess fieldAccess, TP parameter);
    public TR visit(Logical logical, TP parameter);
    public TR visit(Modulus modulus, TP parameter);
    public TR visit(Negation negation, TP parameter);
    public TR visit(UnaryMinus minus, TP parameter);
    public TR visit(Variable variable, TP parameter);
    public TR visit(Increment increment, TP parameter);

    // Expressions (literals)
    public TR visit(CharLiteral charLiteral, TP parameter);
    public TR visit(IntLiteral intLiteral, TP parameter);
    public TR visit(RealLiteral realLiteral, TP parameter);

    // Statements
    public TR visit(Assignment assignment, TP parameter);
    public TR visit(FunctionInvocation functionInvocation, TP parameter);
    public TR visit(IfElse ifElse, TP parameter);
    public TR visit(Print print, TP parameter);
    public TR visit(Read read, TP parameter);
    public TR visit(Return returnStatement, TP parameter);
    public TR visit(While whileStatement, TP parameter);

    // Types
    public TR visit(ArrayType arrayType, TP parameter);
    public TR visit(CharType charType, TP parameter);
    public TR visit(ErrorType errorType, TP parameter);
    public TR visit(FunctionType functionType, TP parameter);
    public TR visit(IntType intType, TP parameter);
    public TR visit(RealType realType, TP parameter);
    public TR visit(StructType structType, TP parameter);
    public TR visit(VoidType voidType, TP parameter);

    // Record Field
    public TR visit (RecordField recordField, TP parameter);
}
