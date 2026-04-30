package ast.types;

import ast.Locatable;
import errorhandler.ErrorHandler;
import visitor.Visitor;

import java.util.List;

public class ErrorType extends AbstractType {
    private final String message;
    private final Locatable astNode;

    public ErrorType(String message, Locatable astNode) {
        this.message = message;
        this.astNode = astNode;
        ErrorHandler.getInstance().addError(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ERROR: ");
        sb.append(message);
        sb.append(" -- Line:");
        sb.append(astNode.getLine());
        sb.append(" -- Column:");
        sb.append(astNode.getColumn());
        return sb.toString();
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }

    @Override
    public void mustPromoteTo(Type type, Locatable locatable) {
        // No hace nada: si ya hay un error, no generamos otro de promoción
    }

    @Override
    public void mustBeBuiltIn(Locatable locatable) { /* Silencio */ }

    @Override
    public void mustBeLogical(Locatable locatable) { /* Silencio */ }

    @Override
    public Type arithmetic(Type type, Locatable locatable) { return this; }

    @Override
    public Type arithmetic(Locatable locatable) { return this; }

    @Override
    public Type modulus(Type type, Locatable locatable) { return this; }

    @Override
    public Type logic(Type type, Locatable locatable) { return this; }

    @Override
    public Type logic(Locatable locatable) { return this; }

    @Override
    public Type comparison(Type type, Locatable locatable) { return this; }

    @Override
    public Type squareBrackets(Type type, Locatable locatable) { return this; }

    @Override
    public Type dot(String name, Locatable locatable) { return this; }

    @Override
    public Type parenthesis(List<Type> types, Locatable locatable) { return this; }

    @Override
    public Type canBeCastTo(Type type, Locatable locatable) { return this; }

    @Override
    public int numberOfBytes() {
        throw new IllegalArgumentException(
                "This code should never be executed! When there is an error, code generation is not done");
    }
}
