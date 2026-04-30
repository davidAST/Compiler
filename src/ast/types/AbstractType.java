package ast.types;

import ast.Locatable;
import codegenerator.CodeGenerator;

import java.util.List;

public abstract class AbstractType implements Type {

    // Checks if the type is a basic/built-in type (e.g., for Read, Write, If, While)
    @Override
    public void mustBeBuiltIn(Locatable locatable) {
        new ErrorType("Invalid type: '" + this + "'. Expected Int, Char, or Real", locatable);
    }

    // Checks if the type can be used as a condition in logical expressions (If, While)
    @Override
    public void mustBeLogical(Locatable locatable) {
        new ErrorType("Invalid type: '" + this + "'. Expected Int or Char", locatable);
    }

    // Binary arithmetic operations: +, -, *, /, %
    @Override
    public Type arithmetic(Type type, Locatable locatable) {
        if (type instanceof ErrorType) return type;
        String message = "Arithmetic operation not allowed between '" + this + "' and '" + type + "'";
        return new ErrorType(message, locatable);
    }

    // Unary arithmetic operation: unary minus (-)
    @Override
    public Type arithmetic(Locatable locatable) {
        return new ErrorType("Unary minus (-) is not supported for type '" + this + "'", locatable);
    }

    // Binary modulus operation: % (Only Int and Char)
    @Override
    public Type modulus(Type type, Locatable locatable) {
        if (type instanceof ErrorType) return type;
        String message = "Modulus operation not allowed between '" + this + "' and '" + type + "'";
        return new ErrorType(message, locatable);
    }

    // Binary logical operations: &&, ||
    @Override
    public Type logic(Type type, Locatable locatable) {
        if (type instanceof ErrorType) return type;
        return new ErrorType("Logical operation not allowed between '" + this + "' and '" + type + "'", locatable);
    }

    // Unary logical operation: logical NOT (!)
    @Override
    public Type logic(Locatable locatable) {
        return new ErrorType("Logical NOT (!) is not supported for type '" + this + "'", locatable);
    }

    // Verification for assignments and parameter passing (e.g., target = value)
    @Override
    public void mustPromoteTo(Type type, Locatable locatable) {
        if (type instanceof ErrorType) return;
        new ErrorType("Type '" + this + "' cannot be promoted to '" + type + "'", locatable);
    }

    // Explicit casting expression: (type) expression
    @Override
    public Type canBeCastTo(Type type, Locatable locatable) {
        if (type instanceof ErrorType) return type;
        return new ErrorType("Cannot cast from '" + this + "' to '" + type + "'", locatable);
    }

    // Comparison operations: >, <, >=, <=, ==, !=
    @Override
    public Type comparison(Type type, Locatable locatable) {
        if (type instanceof ErrorType) return type;
        return new ErrorType("Cannot compare '" + this + "' with '" + type + "'", locatable);
    }

    // Indexing operation: expression[index]
    @Override
    public Type squareBrackets(Type type, Locatable locatable) {
        if (type instanceof ErrorType) return type;
        return new ErrorType("Type '" + this + "' does not support indexing (it is not an array)", locatable);
    }

    // Field access for structures: expression.name
    @Override
    public Type dot(String name, Locatable locatable) {
        return new ErrorType("Type '" + this + "' does not have fields (access with '.')", locatable);
    }

    // Function call: expression(arguments)
    @Override
    public Type parenthesis(List<Type> types, Locatable locatable) {
        return new ErrorType("Type '" + this + "' cannot be called as a function", locatable);
    }


    @Override
    public char suffix() {
        throw new IllegalStateException("This method ('suffix') should never be executed!");
    }

    @Override
    public void convertTo(CodeGenerator cg, Type other) {
        throw new RuntimeException("Invalid cast from " + this + " to " + other);
    }
}