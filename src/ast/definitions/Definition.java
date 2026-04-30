package ast.definitions;

import ast.Locatable;
import ast.types.Type;

public interface Definition extends Locatable {
    public Type getType();
    public String getName();
    public int getScope();
    public void setScope(int scope);
}
