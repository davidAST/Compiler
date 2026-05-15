package ast.statements;

import ast.expressions.Expression;
import ast.expressions.AbstractExpression;
import ast.expressions.Variable;
import visitor.Visitor;

import java.util.List;

public class Increment extends AbstractExpression {
    public Expression expression;
    public String operator;

    public Increment(int line, int column, Expression expression, String operator) {
        super(line, column);
        this.expression = expression;
        this.operator = operator;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return "++";
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }

}
