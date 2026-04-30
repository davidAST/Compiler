package ast.definitions;

import ast.statements.Statement;
import ast.types.Type;
import visitor.Visitor;

import java.util.List;

public class FuncDefinition extends AbstractDefinition {
    private final List<VarDefinition> definitions;
    private final List<Statement> statements;
    private int localVarsSize;

    public FuncDefinition(int line, int column, String name, Type type, List<VarDefinition> definitions, List<Statement> statements) {
        super(line, column, name, type);
        this.definitions = definitions;
        this.statements = statements;
    }


    public List<VarDefinition> getDefinitions() {
        return definitions;
    }
    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("def ").append(getName()).append(getType()).append(": {\n");

        if (!definitions.isEmpty()) {
            for (Definition def : definitions) {
                sb.append("\t").append(def.toString()).append(";\n");
            }
        }

        for (Statement st : statements) {
            sb.append("\t").append(st.toString()).append(";\n");
        }

        sb.append("}");

        return sb.toString();
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }

    public int getLocalVarsSize() {
        return localVarsSize;
    }

    public void setLocalVarsSize(int localVarsSize) {
        this.localVarsSize = localVarsSize;
    }
}
