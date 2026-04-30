package ast;

public interface Locatable extends ASTNode {
    public int getLine();
    public int getColumn();
}
