package ast.statements;

import ast.expressions.Expression;
import ast.expressions.AbstractExpression;
import ast.expressions.Variable;
import visitor.Visitor;

import java.util.List;

public class FunctionInvocation extends AbstractExpression implements Statement {
    // REPRESENTATION -> variable(arguments)
    // Example -> "add(1, 2)"

    private final Variable variable;
    private final List<Expression> arguments;


    public FunctionInvocation(int line, int column, Variable variable, List<Expression> arguments) {
        super(line, column);
        this.variable = variable;
        this.arguments = arguments;
    }

    public Variable getVariable() {
        return variable;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(variable.toString()).append("(");

        for (int i = 0; i < arguments.size(); i++) {
            sb.append(arguments.get(i).toString());

            if (i < arguments.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append(")");
        return sb.toString();
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }

}
