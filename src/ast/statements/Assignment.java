package ast.statements;

import ast.expressions.Expression;
import visitor.Visitor;

public class Assignment extends AbstractStatement {
    // REPRESENTATION -> left = right
    // Example -> "a = 5", "puntos[0] = x + 1", "p.x = 10"

    private final Expression left;
    private final Expression right;

    public Assignment(int line, int column, Expression left, Expression right) {
        super(line, column);
        this.left = left;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public String toString() {
        return left.toString() + " = " + right.toString();
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }
}
