package ast.expressions;

public class ExpressionFactory {
    /**
     * Creates either an arithmetic expression or a modulus expression
     * @param line Line of the expression
     * @param column Column of the expression
     * @param left Left expression
     * @param right Right expression
     * @param operator Operator that decides what expression will be created
     * @return Either an arithmetic or modulus expression depending on the operator
     */
    public static Expression createArithmeticOrModulus(int line, int column, Expression left, Expression right, String operator) {
        if (operator.equals("%")) {
            return new Modulus(line, column, left, right);
        } else {
            return new Arithmetic(line, column, left, right, operator);
        }
    }
}
