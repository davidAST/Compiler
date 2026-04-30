package ast.statements;

import ast.expressions.Expression;
import visitor.Visitor;

public class Return extends AbstractStatement {
    // REPRESENTATION -> "return expression;"
    // Example -> "return c1;", "return a + b;"

    private final Expression expression;

    public Return(int line, int column, Expression expression) {
        super(line, column);
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "return " + expression.toString();
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }
}