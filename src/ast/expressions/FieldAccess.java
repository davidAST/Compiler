package ast.expressions;

import visitor.Visitor;

public class FieldAccess extends AbstractExpression {
    // REPRESENTATION -> left.expression
    // Example -> "student.name"

    private final String field;
    private final Expression left;

    public FieldAccess(int line, int column, String field, Expression left) {
        super(line, column);
        this.field = field;
        this.left = left;
    }

    public String getField() {
        return field;
    }

    public Expression getLeft() {
        return left;
    }

    @Override
    public String toString() {
        return left.toString() + "." + field;
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }
}
