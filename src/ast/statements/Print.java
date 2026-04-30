package ast.statements;

import ast.expressions.Expression;
import visitor.Visitor;

public class Print extends AbstractStatement {
    // REPRESENTATION -> "print expression;"
    // Example -> "print a + 5;"

    private final Expression expression;

    public Print(int line, int column, Expression expression) {
        super(line, column);
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "print " + expression.toString();
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }
}