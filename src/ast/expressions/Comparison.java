package ast.expressions;


import visitor.Visitor;

public class Comparison extends AbstractExpression {
    // REPRESENTATION -> "left operator right"
    // Example -> "a <= b", "true != false"

    private final String operator;
    private final Expression left;
    private final Expression right;

    public Comparison(int line, int column, Expression left, Expression right, String operator) {
        super(line, column);
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public String getOperator() {
        return operator;
    }

    public  Expression getLeft() {
        return left;
    }

    public  Expression getRight() {
        return right;
    }

    @Override
    public String toString() {
        return left.toString() + " " + operator + " " + right.toString();
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }
}
