package ast.types;

import ast.ASTNode;
import ast.Locatable;
import codegenerator.CodeGenerator;

import java.util.List;

public interface Type extends ASTNode {
    // Type checking
    void mustBeBuiltIn(Locatable locatable);
    void mustBeLogical(Locatable locatable);
    Type arithmetic(Type type, Locatable locatable);
    Type arithmetic(Locatable locatable);
    Type modulus(Type type, Locatable locatable);
    Type logic(Type type, Locatable locatable);
    Type logic(Locatable locatable);
    void mustPromoteTo(Type type, Locatable locatable);
    Type canBeCastTo(Type type, Locatable locatable);
    Type comparison(Type type, Locatable locatable);
    Type squareBrackets(Type type, Locatable locatable);
    Type dot(String name, Locatable locatable);
    Type parenthesis(List<Type> types, Locatable locatable);

    // Code generation
    int numberOfBytes();
    char suffix();
    void convertTo(CodeGenerator cg, Type other);
}
