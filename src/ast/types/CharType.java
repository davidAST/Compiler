package ast.types;

import ast.Locatable;
import codegenerator.CodeGenerator;
import visitor.Visitor;

public class CharType extends AbstractType {
    // REPRESENTATION -> "char"

    private static CharType instance;

    private CharType() {

    }

    public static CharType getInstance() {
        if (instance == null) {
            instance = new CharType();
        }
        return instance;
    }

    @Override
    public String toString() {
        return "CharType";
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }

    @Override
    public void mustBeBuiltIn(Locatable locatable) {
    }

    @Override
    public void mustBeLogical(Locatable locatable) {
    }

    @Override
    public Type arithmetic(Type type, Locatable locatable) {
        if (type == this) {
            return IntType.getInstance();
        } else if (type == IntType.getInstance() || type == RealType.getInstance() || (type instanceof ErrorType)) {
            return type;
        } else {
            String message = "Incompatible types: " + this.toString() + " and " + type.toString();
            return new ErrorType(message, locatable);
        }
    }

    @Override
    public Type arithmetic(Locatable locatable) {
        return this;
    }

    @Override
    public Type modulus(Type type, Locatable locatable) {
        if (type == this || type == IntType.getInstance()) {
            return IntType.getInstance();
        } else if (type instanceof ErrorType) {
            return type;
        } else {
            String message = "Incompatible types: " + this.toString() + " and " + type.toString();
            return new ErrorType(message, locatable);
        }
    }

    @Override
    public Type logic(Type type, Locatable locatable) {
        if (type == this
                || type == IntType.getInstance()) {
            return IntType.getInstance();
        } else if (type instanceof ErrorType) {
            return type;
        }
        String message = "Incompatible types: " + this.toString() + " and " + type.toString();
        return new ErrorType(message, locatable);
    }

    @Override
    public Type logic(Locatable locatable) {
        return IntType.getInstance();
    }

    @Override
    public void mustPromoteTo(Type type, Locatable locatable) {
        if (type != this && type != IntType.getInstance() && type != RealType.getInstance() && !(type instanceof ErrorType)) {
            new ErrorType("Type '" + this + "' cannot be promoted to '" + type + "'", locatable);
        }
    }

    @Override
    public Type canBeCastTo(Type type, Locatable locatable) {
        if (type == this
                || type == IntType.getInstance()
                || type == RealType.getInstance()
                || (type instanceof ErrorType)) {
            return type;
        } else {
            return new ErrorType("Cannot cast '" + this + "' to '" + type + "'", locatable);
        }
    }

    @Override
    public Type comparison(Type type, Locatable locatable) {
        if (type == this
                || type == IntType.getInstance()
                || type == RealType.getInstance()) {
            return IntType.getInstance();
        } else if (type instanceof ErrorType) {
            return type;
        } else {
            return new ErrorType("Incompatible types in comparison: '" + this + "' and '" + type + "'", locatable);
        }
    }

    @Override
    public int numberOfBytes() {
        return 1;
    }

    @Override
    public char suffix() {
        return 'b';
    }

    @Override
    public void convertTo(CodeGenerator cg, Type other) {
        if (other instanceof RealType) {
            cg.b2i();
            cg.i2f();
        } else if (other instanceof IntType) {
            cg.b2i();
        } else if (other != this) {
            throw new RuntimeException("Invalid cast from " + this + " to " + other);
        }
    }
}