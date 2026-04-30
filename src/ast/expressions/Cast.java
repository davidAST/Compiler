package ast.expressions;

import ast.types.Type;
import visitor.Visitor;

public class Cast extends AbstractExpression {
    // REPRESENTATION -> (type) expression
    // Example -> "(char) n"

    private final Type targetType;
    private final Expression expression;

    public Cast(int line, int column, Expression expression, Type targetType) {
        super(line, column);
        this.targetType = targetType;
        this.expression = expression;
    }

    public Type getTargetType() { return targetType; }
    public Expression getExpression() { return expression; }

    @Override
    public String toString() {
        return "(" + targetType.toString() + ") " + expression.toString();
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }
}
