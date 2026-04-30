package ast.expressions;

import visitor.Visitor;

public class Negation extends AbstractExpression {
    // REPRESENTATION -> "!Expression"
    // Example -> "!true"

    private final Expression expression;

    public Negation(int line, int column, Expression expression) {
        super(line, column);
        this.expression = expression;
    }

    public Expression getExpression() { return expression; }

    @Override
    public String toString() {
        return "!" + expression.toString();
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }
}
