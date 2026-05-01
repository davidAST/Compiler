package semantic;

import ast.Program;
import ast.RecordField;
import ast.definitions.Definition;
import ast.definitions.FuncDefinition;
import ast.definitions.VarDefinition;
import ast.expressions.*;
import ast.expressions.literals.CharLiteral;
import ast.expressions.literals.IntLiteral;
import ast.expressions.literals.RealLiteral;
import ast.statements.*;
import ast.types.*;
import visitor.Visitor;

public abstract class AbstractVisitor<TP,TR> implements Visitor<TP,TR> {

    // Program ===============================================================

    @Override
    public TR visit(Program program, TP parameter) {
        for (Definition def : program.getDefinitions()) {
            def.accept(this, parameter);
        }
        return null;
    }

    // Definitions ===============================================================

    @Override
    public TR visit(FuncDefinition funcDefinition, TP parameter) {
        funcDefinition.getType().accept(this, parameter);
        for (Definition def : funcDefinition.getDefinitions()) {
            def.accept(this, parameter);
        }
        for (Statement st :  funcDefinition.getStatements()) {
            st.accept(this, parameter);
        }

        return null;
    }

    @Override
    public TR visit(VarDefinition varDefinition, TP parameter) {
        varDefinition.getType().accept(this, parameter);
        return null;
    }


    // Expressions ===============================================================

    @Override
    public TR visit(Arithmetic arithmetic, TP parameter) {
        arithmetic.getLeft().accept(this, parameter);
        arithmetic.getRight().accept(this, parameter);
        return null;
    }

    @Override
    public TR visit(ArrayAccess arrayAccess, TP parameter) {
        arrayAccess.getExpression().accept(this, parameter);
        arrayAccess.getIndex().accept(this, parameter);
        return null;
    }

    @Override
    public TR visit(Cast cast, TP parameter) {
        cast.getExpression().accept(this, parameter);
        return null;
    }

    @Override
    public TR visit(Comparison comparison, TP parameter) {
        comparison.getLeft().accept(this, parameter);
        comparison.getRight().accept(this, parameter);
        return null;
    }

    @Override
    public TR visit(FieldAccess fieldAccess, TP parameter) {
        fieldAccess.getLeft().accept(this, parameter);
        return null;
    }

    @Override
    public TR visit(Logical logical, TP parameter) {
        logical.getLeft().accept(this, parameter);
        logical.getRight().accept(this, parameter);
        return null;
    }

    @Override
    public TR visit(Modulus modulus, TP parameter) {
        modulus.getLeft().accept(this, parameter);
        modulus.getRight().accept(this, parameter);
        return null;
    }

    @Override
    public TR visit(Negation negation, TP parameter) {
        negation.getExpression().accept(this, parameter);
        return null;
    }

    @Override
    public TR visit(UnaryMinus minus, TP parameter) {
        minus.getExpression().accept(this, parameter);
        return null;
    }

    @Override
    public TR visit(Variable variable, TP parameter) {
        return null;
    }

    // Expressions (Literals) ===============================================================

    @Override
    public TR visit(CharLiteral charLiteral, TP parameter) {
        return null;
    }

    @Override
    public TR visit(IntLiteral intLiteral, TP parameter) {
        return null;
    }

    @Override
    public TR visit(RealLiteral realLiteral, TP parameter) {
        return null;
    }

    // Statements ===============================================================

    @Override
    public TR visit(Assignment assignment, TP parameter) {
        assignment.getLeft().accept(this, parameter);
        assignment.getRight().accept(this, parameter);
        return null;
    }

    @Override
    public TR visit(FunctionInvocation functionInvocation, TP parameter) {
        functionInvocation.getVariable().accept(this, parameter);
        for (Expression expression : functionInvocation.getArguments()) {
            expression.accept(this, parameter);
        }

        return null;
    }

    @Override
    public TR visit(IfElse ifElse, TP parameter) {
        for (Statement ifStatement : ifElse.getIfBody()) {
            ifStatement.accept(this, parameter);
        }
        for (Statement elseStatement : ifElse.getElseBody()) {
            elseStatement.accept(this, parameter);
        }
        ifElse.getCondition().accept(this, parameter);
        return null;
    }

    @Override
    public TR visit(Print print, TP parameter) {
        print.getExpression().accept(this, parameter);
        return null;
    }

    @Override
    public TR visit(Read read, TP parameter) {
        read.getExpression().accept(this, parameter);
        return null;
    }

    @Override
    public TR visit(Return returnStatement, TP parameter) {
        if (returnStatement.getExpression() != null) {
            returnStatement.getExpression().accept(this, parameter);
        }
        return null;
    }

    @Override
    public TR visit(While whileStatement, TP parameter) {
        whileStatement.getCondition().accept(this, parameter);
        for (Statement st :  whileStatement.getBody()) {
            st.accept(this, parameter);
        }
        return null;
    }

    // Types ===============================================================

    @Override
    public TR visit(ArrayType arrayType, TP parameter) {
        arrayType.getOf().accept(this, parameter);
        return null;
    }

    @Override
    public TR visit(CharType charType, TP parameter) {
        return null;
    }

    @Override
    public TR visit(ErrorType errorType, TP parameter) {
        return null;
    }

    @Override
    public TR visit(FunctionType functionType, TP parameter) {
        functionType.getReturnType().accept(this, parameter);
        for (VarDefinition vars : functionType.getParams()) {
            vars.accept(this, parameter);
        }
        return null;
    }

    @Override
    public TR visit(IntType intType, TP parameter) {
        return null;
    }

    @Override
    public TR visit(RealType realType, TP parameter) {
        return null;
    }

    @Override
    public TR visit(StructType structType, TP parameter) {
        for (RecordField record : structType.getFields()) {
            record.accept(this, parameter);
        }
        return null;
    }

    @Override
    public TR visit(VoidType voidType, TP parameter) {
        return null;
    }

    @Override
    public TR visit(RecordField recordField, TP parameter) {
        recordField.getType().accept(this, parameter);
        return null;
    }
}
