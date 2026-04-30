package ast.statements;

import ast.expressions.Expression;
import visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

public class IfElse extends AbstractStatement {
    // REPRESENTATION -> "if (condition) { ifBody } else { elseBody }"
    // Example -> "if (a > 0) { print(a); } else { print(0); }"

    private final List<Statement> ifBody;
    private final List<Statement> elseBody;
    private final Expression condition;

    public IfElse(int line, int column, List<Statement> ifStatements, Expression condition, List<Statement> elseStatements) {
        super(line, column);
        this.ifBody = ifStatements;
        this.condition = condition;
        this.elseBody = elseStatements;
    }

    public IfElse(int line, int column, List<Statement> ifStatements, Expression condition) {
        super(line, column);
        this.ifBody = ifStatements;
        this.condition = condition;
        this.elseBody = new ArrayList<>();
    }

    public List<Statement> getIfBody() {
        return ifBody;
    }
    public List<Statement> getElseBody() {
        return elseBody;
    }
    public Expression getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("if ").append(condition.toString()).append(": {\n");

        for (Statement st : ifBody) {
            sb.append("\t\t").append(st.toString()).append(";\n");
        }
        sb.append("\t}");

        if (!elseBody.isEmpty()) {
            sb.append(" else: {\n");
            for (Statement st : elseBody) {
                sb.append("\t\t").append(st.toString()).append(";\n");
            }
            sb.append("\t}");
        }

        return sb.toString();
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }
}
