package semantic;

import ast.definitions.FuncDefinition;
import ast.definitions.VarDefinition;
import ast.expressions.*;
import ast.expressions.literals.CharLiteral;
import ast.expressions.literals.IntLiteral;
import ast.expressions.literals.RealLiteral;
import ast.statements.*;
import ast.types.*;


public class TypeCheckingVisitor extends AbstractVisitor<Type, Void> {

    // Definitions
    @Override
    public Void visit(FuncDefinition funcDef, Type parameter) {
        for (VarDefinition varDef : funcDef.getDefinitions()) {
            varDef.accept(this, parameter);
        }
        for (Statement st : funcDef.getStatements()) {
            FunctionType funcType = (FunctionType) funcDef.getType();
            st.accept(this, funcType.getReturnType());
        }
        return null;
    }

    // Expressions ===============================================================

    @Override
    public Void visit(Arithmetic arithmetic, Type parameter) {
        arithmetic.getLeft().accept(this, parameter);
        arithmetic.getRight().accept(this, parameter);
        arithmetic.setLValue(false);
        arithmetic.setType(arithmetic.getLeft().getType().arithmetic(arithmetic.getRight().getType(), arithmetic));
        return null;
    }

    @Override
    public Void visit(ArrayAccess arrayAccess, Type parameter) {
        arrayAccess.getExpression().accept(this, parameter);
        arrayAccess.getIndex().accept(this, parameter);
        arrayAccess.setLValue(true);
        arrayAccess.setType(
                arrayAccess.getExpression().getType().squareBrackets(
                        arrayAccess.getIndex().getType(), arrayAccess));
        return null;
    }

    @Override
    public Void visit(Cast cast, Type parameter) {
        cast.getExpression().accept(this, parameter);
        cast.setLValue(false);
        cast.setType(cast.getExpression().getType().canBeCastTo(cast.getTargetType(), cast));
        return null;
    }

    @Override
    public Void visit(Comparison comparison, Type parameter) {
        comparison.getLeft().accept(this, parameter);
        comparison.getRight().accept(this, parameter);
        comparison.setLValue(false);
        comparison.setType(comparison.getLeft().getType().comparison(comparison.getRight().getType(), comparison));
        return null;
    }

    @Override
    public Void visit(FieldAccess fieldAccess, Type parameter) {
        fieldAccess.getLeft().accept(this, parameter);
        fieldAccess.setLValue(true);
        fieldAccess.setType(fieldAccess.getLeft().getType().dot(fieldAccess.getField(), fieldAccess));
        return null;
    }

    @Override
    public Void visit(Logical logical, Type parameter) {
        logical.getLeft().accept(this, parameter);
        logical.getRight().accept(this, parameter);
        logical.setLValue(false);
        logical.setType(logical.getLeft().getType().logic(logical.getRight().getType(), logical));
        return null;
    }

    @Override
    public Void visit(Modulus modulus, Type parameter) {
        modulus.getLeft().accept(this, parameter);
        modulus.getRight().accept(this, parameter);
        modulus.setLValue(false);
        modulus.setType(modulus.getLeft().getType().modulus(modulus.getRight().getType(), modulus));
        return null;
    }

    @Override
    public Void visit(Negation negation, Type parameter) {
        negation.getExpression().accept(this, parameter);
        negation.setLValue(false);
        negation.setType(negation.getExpression().getType().logic(negation));
        return null;
    }

    @Override
    public Void visit(UnaryMinus minus, Type parameter) {
        minus.getExpression().accept(this, parameter);
        minus.setLValue(false);
        minus.setType(minus.getExpression().getType().arithmetic(minus));
        return null;
    }

    @Override
    public Void visit(Variable variable, Type parameter) {
        variable.setLValue(true);
        variable.setType(variable.getVarDef().getType());
        return null;
    }

    @Override
    public Void visit(Increment increment, Type parameter) {
        increment.setLValue(false);
        increment.getExpression().accept(this, parameter);

        if (!increment.getExpression().getType().equals(IntType.getInstance())) {
            increment.setType(new ErrorType("Incorrect type", increment));
        }
        else {
            increment.setType(IntType.getInstance());
        }
        return null;
    }

    // Expressions (Literals) ===============================================================

    @Override
    public Void visit(CharLiteral charLiteral, Type parameter) {
        charLiteral.setLValue(false);
        charLiteral.setType(CharType.getInstance());
        return null;
    }

    @Override
    public Void visit(IntLiteral intLiteral, Type parameter) {
        intLiteral.setLValue(false);
        intLiteral.setType(IntType.getInstance());
        return null;
    }

    @Override
    public Void visit(RealLiteral realLiteral, Type parameter) {
        realLiteral.setLValue(false);
        realLiteral.setType(RealType.getInstance());
        return null;
    }

    // Statements ===============================================================

    @Override
    public Void visit(Print print, Type parameter) {
        print.getExpression().accept(this, parameter);
        print.getExpression().getType().mustBeBuiltIn(print);

        return null;
    }

    @Override
    public Void visit(Assignment assignment, Type parameter) {
        assignment.getLeft().accept(this, parameter);
        assignment.getRight().accept(this, parameter);

        if (!assignment.getLeft().getLValue()) {
            new ErrorType("The left part of the assignment is not modifiable", assignment.getLeft());
        }

        assignment.getRight().getType().mustPromoteTo(assignment.getLeft().getType(), assignment);
        return null;
    }

    @Override
    public Void visit(IfElse ifElse, Type parameter) {
        ifElse.getCondition().accept(this, parameter);
        ifElse.getCondition().getType().mustBeLogical(ifElse);

        for (Statement ifStatement : ifElse.getIfBody()) {
            ifStatement.accept(this, parameter);
        }
        for (Statement elseStatement : ifElse.getElseBody()) {
            elseStatement.accept(this, parameter);
        }

        return null;
    }

    @Override
    public Void visit(FunctionInvocation functionInvocation, Type parameter) {
        functionInvocation.getVariable().accept(this, parameter);
        for (Expression expression : functionInvocation.getArguments()) {
            expression.accept(this, parameter);
        }
        functionInvocation.setLValue(false);
        Type result = functionInvocation.getVariable().getType().parenthesis(
                functionInvocation.getArguments().stream().map(Expression::getType).toList(),
                functionInvocation);

        functionInvocation.setType(result);
        return null;
    }

    @Override
    public Void visit(Read read, Type parameter) {
        read.getExpression().accept(this, parameter);

        read.getExpression().getType().mustBeBuiltIn(read);
        if (!read.getExpression().getLValue()) {
            new ErrorType("The expression for 'read' must be modifiable", read.getExpression());
        }
        return null;
    }

    @Override
    public Void visit(Return returnStatement, Type parameter) {
        returnStatement.getExpression().accept(this, parameter);

        returnStatement.getExpression().getType().mustPromoteTo(parameter,returnStatement);

        return null;
    }

    @Override
    public Void visit(While whileStatement, Type parameter) {
        whileStatement.getCondition().accept(this, parameter);
        whileStatement.getCondition().getType().mustBeLogical(whileStatement);

        for (Statement st : whileStatement.getBody()) {
            st.accept(this, parameter);
        }
        return null;
    }




}
