package ast.statements;

import ast.expressions.Expression;
import visitor.Visitor;

public class Read extends AbstractStatement {
    // REPRESENTATION -> "input expression;"
    // Example -> "input student.age;", "input a[0];", "input x;"

    private final Expression expression;

    public Read(int line, int column, Expression expression) {
        super(line, column);
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "input " + expression.toString();
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }
}