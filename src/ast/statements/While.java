package ast.statements;

import ast.expressions.Expression;
import visitor.Visitor;

import java.util.List;

public class While extends AbstractStatement {
    // REPRESENTATION -> "while condition : { body }"
    // Example -> "while i < 10 : { i = i + 1; j = j * 2; }"

    private final Expression condition;
    private final List<Statement> body;

    public While(int line, int column, Expression condition, List<Statement> body) {
        super(line, column);
        this.condition = condition;
        this.body = body;
    }

    public Expression getCondition() {
        return condition;
    }

    public List<Statement> getBody() {
        return body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("while ").append(condition.toString()).append(": {\n");

        for (Statement st : body) {
            sb.append("\t\t").append(st.toString()).append(";\n");
        }

        sb.append("\t}");

        return sb.toString();
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }
}