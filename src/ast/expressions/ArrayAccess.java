package ast.expressions;

import visitor.Visitor;

public class ArrayAccess extends AbstractExpression {
    // REPRESENTATION -> "expression[index]"
    // Example -> a[3]

    private final Expression expression;
    private final Expression index;

    public ArrayAccess(int line, int column, Expression expression, Expression index) {
        super(line, column);
        this.expression = expression;
        this.index = index;
    }

    public Expression getExpression() {
        return expression;
    }
    public Expression getIndex() { return index; }

    @Override
    public String toString() {
        return expression.toString() + "[" + index.toString() + "]";
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }
}
