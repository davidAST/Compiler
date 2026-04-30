package ast.expressions;

import ast.AbstractLocatable;
import ast.types.Type;

public abstract class AbstractExpression extends AbstractLocatable implements Expression {

    private Type type;
    private boolean lValue;

    public AbstractExpression(int column, int line) {
        super(column, line);
    }

    public boolean getLValue() {
        return lValue;
    }

    public void setLValue(boolean lValue) {
        this.lValue = lValue;
    }

    public Type getType() { return this.type; }
    public void setType(Type type) { this.type = type; }


}
