package ast.expressions;

import visitor.Visitor;

public class Modulus extends AbstractExpression{
    // REPRESENTATION -> "left % right"
    // Example -> "a % b"

    private final Expression left;
    private final Expression right;

    public Modulus(int line, int column, Expression left, Expression right) {
        super(line, column);
        this.left = left;
        this.right = right;
    }

    public Expression getLeft() { return left; }
    public Expression getRight() { return right; }

    @Override
    public String toString() {
        return left.toString() + " % " + right.toString();
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }
}
