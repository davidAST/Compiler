package ast.expressions;

import visitor.Visitor;

public class Arithmetic extends AbstractExpression {
    // REPRESENTATION -> "left operator right"
    // Example -> "a + b", " a - b"...

    private final Expression left;
    private final Expression right;
    private final String operator;

    public Arithmetic(int line, int column, Expression left, Expression right, String operator) {
        super(line, column);
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public String getOperator() { return operator; }
    public Expression getLeft() { return left; }
    public Expression getRight() { return right; }

    @Override
    public String toString() {
        return left.toString() + " " + operator + " " + right.toString();
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }
}
