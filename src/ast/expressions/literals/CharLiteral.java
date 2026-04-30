package ast.expressions.literals;

import ast.expressions.AbstractExpression;
import visitor.Visitor;

public class CharLiteral extends AbstractExpression {
    private final char value;

    public CharLiteral(int line, int column, char value) {
        super(line, column);
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (value == '\n') {
            return "'\\n'";
        }
        return "'" + value + "'";
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }
}
