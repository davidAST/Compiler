package ast.types;

import ast.AbstractLocatable;
import visitor.Visitor;

public class VoidType extends AbstractType {
    // REPRESENTATION -> "None"

    private static VoidType instance;

    public VoidType() {

    }

    public static VoidType getInstance() {
        if (instance == null) {
            instance = new VoidType();
        }
        return instance;
    }

    @Override
    public String toString() {
        return "None";
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }

    @Override
    public int numberOfBytes() {
        return 0;
    }
}