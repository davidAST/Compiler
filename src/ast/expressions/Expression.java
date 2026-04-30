package ast.expressions;

import ast.Locatable;
import ast.types.Type;

public interface Expression extends Locatable {
    public Type getType();
    public boolean getLValue();
    public void setLValue(boolean value);
}
