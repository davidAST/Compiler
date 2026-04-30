package ast.types;

import ast.AbstractLocatable;
import ast.Locatable;
import codegenerator.CodeGenerator;
import visitor.Visitor;

public class RealType extends AbstractType {
    // REPRESENTATION -> "double"

    private static RealType instance;

    private RealType() {

    }

    public static RealType getInstance() {
        if (instance == null) {
            instance = new RealType();
        }
        return instance;
    }

    @Override
    public String toString() {
        return "RealType";
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }

    @Override
    public void mustBeBuiltIn(Locatable locatable) {
    }

    @Override
    public Type arithmetic(Type type, Locatable locatable) {
        if (type == CharType.getInstance()
                || type == IntType.getInstance()
                || type == RealType.this) {
            return this;
        } else if (type instanceof ErrorType) {
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
    public Type logic(Type type, Locatable locatable) {
        if (type == CharType.getInstance()
                || type == IntType.getInstance()
                || type == this) {
            return IntType.getInstance();
        } else if (type instanceof ErrorType) {
            return type;
        }
        String message = "Incompatible types: " + this.toString() + " and " + type.toString();
        return new ErrorType(message, locatable);
    }

    @Override
    public void mustPromoteTo(Type type, Locatable locatable) {
        if (type != this && !(type instanceof ErrorType)) {
            new ErrorType("Type '" + this + "' cannot be promoted to '" + type + "'", locatable);
        }
    }

    @Override
    public Type canBeCastTo(Type type, Locatable locatable) {
        if (type == CharType.getInstance()
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
        if (type == CharType.getInstance()
                || type == IntType.getInstance()
                || type == this) {
            return IntType.getInstance();
        } else if (type instanceof ErrorType) {
            return type;
        } else {
            return new ErrorType("Incompatible types in comparison: '" + this + "' and '" + type + "'", locatable);
        }
    }

    @Override
    public int numberOfBytes() {
        return 4;
    }

    @Override
    public char suffix() {
        return 'f';
    }

    @Override
    public void convertTo(CodeGenerator cg, Type other) {
        if (other instanceof CharType) {
            cg.f2i();
            cg.i2b();
        } else if (other instanceof IntType) {
            cg.f2i();
        } else if (other != this) {
            throw new RuntimeException("Invalid cast from " + this + " to " + other);
        }
    }
}