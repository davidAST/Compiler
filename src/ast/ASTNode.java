package ast;

import visitor.Visitor;

public interface ASTNode {
    public <TP, TR> TR accept (Visitor<TP, TR> visitor, TP parameter);
}
