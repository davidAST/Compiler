package codegenerator;
import ast.expressions.*;
import ast.expressions.literals.*;
import ast.statements.FunctionInvocation;
import ast.types.FunctionType;

import java.util.List;

/*
 *   The Execute visitor only visits
 *   - Expressions
 */
public class ValueCGVisitor extends AbstractCGVisitor<Void, Void> {
    private AddressCGVisitor addressVisitor;
    private final CodeGenerator cg;

    public ValueCGVisitor(CodeGenerator cg) {
        this.cg = cg;
    }

    public void setAddressVisitor(AddressCGVisitor addressVisitor) {
        this.addressVisitor = addressVisitor;
    }

    @Override
    public Void visit(ArrayAccess arrayAccess, Void param) {
        /*
         * value[[ArrayAccess: expression1 -> expression2 expression3]]() =
         *      address[[expression1]]
         *      <load> expression1.type.suffix()
         *
         */
        arrayAccess.accept(addressVisitor, param);
        cg.load(arrayAccess.getType().suffix());
        return null;
    }

    @Override
    public Void visit(FieldAccess fieldAccess, Void param) {
        /*
         * value[[FieldAccess: expression1 -> expression2 ID]]() =
         *      address[[expression1]]
         *      <load> expression1.type.suffix()
         */
        fieldAccess.accept(addressVisitor, param);
        cg.load(fieldAccess.getType().suffix());
        return null;
    }

    @Override
    public Void visit(Cast cast, Void param) {
        /*
         * value[[Cast: expression1 -> type expression2]]() =
         *      value[[expression2]]
         *      cg.convert(expression2.type, type)
         */
        cast.getExpression().accept(this, param);
        cg.convert(cast.getExpression().getType(), cast.getType());
        return null;
    }

    @Override
    public Void visit(UnaryMinus unaryMinus, Void param) {
        /*
         * value[[UnaryMinus: expression1 -> expression2]]() =
         *      value[[expression2]]
         *      cg.convert(expression2.type, expression1.type)
         *      <push> expr1.type.suffix() -1
         *      <mul> expr1.type.suffix()
         */
        unaryMinus.getExpression().accept(this, param);
        cg.convert(unaryMinus.getExpression().getType(), unaryMinus.getType());
        cg.push(unaryMinus.getType().suffix(), -1);
        cg.mul(unaryMinus.getType().suffix());
        return null;
    }

    @Override
    public Void visit(Negation negation, Void param) {
        /*
         * value[[Negation: expression1 -> expression2]]() =
         *      value[[expression2]]
         *      cg.convert(expression2.type, expression1.type)
         *      <not>
         */
        negation.getExpression().accept(this,  param);
        cg.convert(negation.getExpression().getType(), negation.getType());
        cg.not();
        return null;
    }

    @Override
    public Void visit(Arithmetic arithmetic, Void param) {
        /*
         * value[[Arithmetic: expression1 -> expression2 OP=(+|-|*|/) expression3]]() =
         *      value[[expression2]]
         *      cg.convert(expression2.type, expression1.type)
         *      value[[expression3]]
         *      cg.convert(expression3.type, expression1.type)
         *      switch (OP) {
         *          case "+"
         *              <add> expression1.type.suffix()
         *          case "-"
         *              <sub> expression1.type.suffix()
         *          case "*"
         *              <mul> expression1.type.suffix()
         *          case "/"
         *              <div> expression1.type.suffix()
         *      }
         */
        arithmetic.getLeft().accept(this, param);
        cg.convert(arithmetic.getLeft().getType(), arithmetic.getType());
        arithmetic.getRight().accept(this, param);
        cg.convert(arithmetic.getRight().getType(), arithmetic.getType());
        switch (arithmetic.getOperator()) {
            case "+":
                cg.add(arithmetic.getType().suffix());
                break;
            case "-":
                cg.sub(arithmetic.getType().suffix());
                break;
            case "*":
                cg.mul(arithmetic.getType().suffix());
                break;
            case "/":
                cg.div(arithmetic.getType().suffix());
                break;
            default:
                throw new RuntimeException("Invalid arithmetic operator " + arithmetic.getOperator());
        }

        return null;
    }

    @Override
    public Void visit(Comparison comparison, Void param) {
        /*
         * value[[Comparison: expression1 -> expression2 OP=(>|>=|<|<=|!=|==) expression3]]() =
         *      value[[expression2]]
         *      cg.convert(expression2.type, expression1.type)
         *      value[[expression3]]
         *      cg.convert(expression3.type, expression1.type)
         *      switch (OP) {
         *          case ">":
         *              <gt> expression1.getType().suffix();
         *              break;
         *          case "=>":
         *              <ge> expression1.getType().suffix();
         *              break;
         *          case "<":
         *              <lt> expression1.getType().suffix();
         *              break;
         *          case "<=":
         *              <le> expression1.getType().suffix();
         *              break;
         *          case "!=":
         *              <ne> expression1.getType().suffix();
         *              break;
         *          case "==":
         *              <eq> expression1.getType().suffix();
         *              break;
         *      }
         */
        comparison.getLeft().accept(this, param);
        cg.convert(comparison.getLeft().getType(), comparison.getType());
        comparison.getRight().accept(this, param);
        cg.convert(comparison.getRight().getType(), comparison.getType());
        switch (comparison.getOperator()) {
            case ">":
                cg.gt(comparison.getType().suffix());
                break;
            case ">=":
                cg.ge(comparison.getType().suffix());
                break;
            case "<":
                cg.lt(comparison.getType().suffix());
                break;
            case "<=":
                cg.le(comparison.getType().suffix());
                break;
            case "!=":
                cg.ne(comparison.getType().suffix());
                break;
            case "==":
                cg.eq(comparison.getType().suffix());
                break;
            default:
                throw new RuntimeException("Invalid logical operator " + comparison.getOperator());
        }
        return null;
    }

    @Override
    public Void visit(Logical logical, Void param) {
        /*
         * value[[Logical: expression1 -> expression2 (&& | ||) expression3]]() =
         *      value[[expression2]]
         *      value[[expression3]]
         *      if (OP == "&&") <and>
         *      if (OP == "||") <or>
         */
        logical.getLeft().accept(this, param);
        logical.getRight().accept(this, param);

        if (logical.getOperator().equals("&&")) {
            cg.and();
            return null;
        }

        if (logical.getOperator().equals("||")) {
            cg.or();
            return null;
        }

        throw new RuntimeException("Invalid logical operator " + logical.getOperator());
    }

    @Override
    public Void visit(FunctionInvocation funcInvocation, Void param) {
        /*
         * value[[FuncInvocation: expression1 -> expression2 expression*]]() =
         *      for (int i = 0; i < expression*.size; i++) {
         *          value[[expression*[i]]]()
         *          cg.convert(expression*[i].type, expression2.type.params[i].type)
         *      }
         *      <call> expression2.name
         */
        List<Expression> arguments = funcInvocation.getArguments();
        FunctionType fType = (FunctionType) funcInvocation.getVariable().getType();
        for (int i = 0; i < arguments.size(); i++) {
            arguments.get(i).accept(this, param);
            cg.convert(arguments.get(i).getType(), fType.getParams().get(i).getType());
        }
        cg.call(funcInvocation.getVariable().getName());
        return null;

    }

    @Override
    public Void visit(Modulus modulus, Void param) {
        /*
         * value[[Modulus: expression1 -> expression2  expression3]]() =
         *      value[[expression2]]
         *      cg.convert(expression2.type, expression1.type)
         *      value[[expression3]]
         *      cg.convert(expression3.type, expression1.type)
         *      <mod> expression1.type.suffix()
         */
        modulus.getLeft().accept(this, param);
        cg.convert(modulus.getLeft().getType(), modulus.getType());

        modulus.getRight().accept(this, param);
        cg.convert(modulus.getRight().getType(), modulus.getType());

        cg.mod();
        return null;
    }

    @Override
    public Void visit(IntLiteral intLiteral, Void param) {
        /*
         * value[[Int: expression1 -> INT_CONSTANT]]() =
         *      <pushi> INT_CONSTANT
         */
        cg.pushi(intLiteral.getValue());
        return null;
    }

    @Override
    public Void visit(RealLiteral realLiteral, Void param) {
        /*
         * value[[Real: expression1 -> REAL_CONSTANT]]() =
         *      <pushf> REAL_CONSTANT
         */
        cg.pushf(realLiteral.getValue());
        return null;
    }

    @Override
    public Void visit(CharLiteral charLiteral, Void param) {
        /*
         * value[[Char: expression1 -> CHAR_CONSTANT]]() =
         *      <pushb> CHAR_CONSTANT
         *
         */
        cg.pushb(charLiteral.getValue());
        return null;
    }

    @Override
    public Void visit(Variable variable, Void param) {
        /*
         * value[[Variable: expression -> ID]]() =
         *      address[[expression]]
         *      <load> expression.getType.suffix()
         *
         */
        variable.accept(addressVisitor, param);
        cg.load(variable.getType().suffix());
        return null;
    }
}
