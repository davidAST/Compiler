package ast.types;

import ast.AbstractLocatable;
import ast.Locatable;
import visitor.Visitor;

public class ArrayType extends AbstractType {
    private final int size;
    private final Type of;

    public ArrayType(int size, Type of) {
        this.size = size;
        this.of = of;
    }

    public int getSize() { return size; }
    public Type getOf() { return of; }

    @Override
    public String toString() {
        return "ArrayType[of:" + of + ",size:" + size + "]";
    }

    @Override
    public Type squareBrackets(Type type, Locatable locatable) {
        if (type instanceof ErrorType) return type;

        if (type == IntType.getInstance()) {
            return this.of;
        }

        return new ErrorType("Array index must be an integer, but '" + type + "' was found", locatable);
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }

    @Override
    public int numberOfBytes() {
        return size * of.numberOfBytes();
    }
}
