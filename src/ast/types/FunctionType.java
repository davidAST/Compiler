package ast.types;

import ast.AbstractLocatable;
import ast.Locatable;
import ast.definitions.VarDefinition;
import visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

public class FunctionType extends AbstractType {
    private final Type returnType;
    private final List<VarDefinition> params;
    private int paramsSize;

    public FunctionType(Type returnType, List<VarDefinition> params) {
        this.returnType = returnType;
        this.params = params;
    }

    // Main
    public FunctionType() {
        this.returnType = VoidType.getInstance();
        params = new ArrayList<>();
    }

    public Type getReturnType() {
        return returnType;
    }

    public List<VarDefinition> getParams() {
        return params;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).toString());

            if (i < params.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append("): ").append(returnType.toString());

        return sb.toString();
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }

    @Override
    public Type parenthesis(List<Type> types, Locatable locatable) {
        if (types.size() != params.size()) {
            return new ErrorType("Invalid number of arguments: expected " +
                    params.size() + " but got " + types.size(), locatable);
        }

        for (int i = 0; i < params.size(); i++) {
            types.get(i).mustPromoteTo(params.get(i).getType(), locatable);
        }

        return returnType;
    }

    @Override
    public int numberOfBytes() {
        throw new IllegalArgumentException(
                "This code should never be executed! When there is an error, code generation is not done");
    }

    public int getParamsSize() {
        return paramsSize;
    }

    public void setParamsSize(int paramsSize) {
        this.paramsSize = paramsSize;
    }
}
