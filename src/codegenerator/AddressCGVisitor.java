package codegenerator;

import ast.definitions.VarDefinition;
import ast.expressions.*;
import ast.types.StructType;

/*
 *   The Execute visitor only visits
 *   - Expressions with l-value
 */
public class AddressCGVisitor extends AbstractCGVisitor<Void, Void> {
    private ValueCGVisitor valueVisitor;
    private final CodeGenerator cg;

    public void setValueVisitor(ValueCGVisitor valueVisitor) {
        this.valueVisitor = valueVisitor;
    }

    public AddressCGVisitor(CodeGenerator cg) {
        this.cg = cg;
    }

    @Override
    public Void visit(Variable variable, Void parameter) {
        /*
         * address[[Variable : expression -> ID]] =
         *      if (expression.definition.scope == 0)
         *          <pusha> expression.definition.offset
         *      else
         *          <pusha bp>
         *          <pushi> expression.definition.offset
         *          <addi>
         */

        VarDefinition varDef = (VarDefinition) variable.getVarDef();
        if (variable.getVarDef().getScope() == 0) {
            cg.pusha(varDef.getOffset());
        }
        else {
            cg.pushbp();
            cg.pushi(varDef.getOffset());
            cg.add('i');
        }

        return null;
    }

    // Address = Base Array Access + (Index * Size of the array type)
    @Override
    public Void visit(ArrayAccess arrayAccess, Void param) {
        /*
         * address[[ArrayAccess: expression1 -> expression2 expression3]]() =
         *      address[[expression2]]
         *      value[[expression3]]
         *      <pushi> expression1.type.numberOfBytes()
         *      <muli>
         *      <addi>
         */
        arrayAccess.getExpression().accept(this, param);
        arrayAccess.getIndex().accept(valueVisitor, param);
        cg.pushi(arrayAccess.getType().numberOfBytes());
        cg.mul('i');
        cg.add('i');
        return null;
    }

    @Override
    public Void visit(FieldAccess fieldAccess, Void param) {
        /*
         * address[[FieldAccess: expression1 -> expression2 ID]]=
         *      address[[expression2]]
         *      <pushi> expression2.type.getField(ID).offset
         *      <addi>
         */
        fieldAccess.getLeft().accept(this, param);
        StructType type = (StructType) fieldAccess.getLeft().getType();
        cg.pushi(type.getField(fieldAccess.getField()).getOffset());
        cg.add('i');
        return null;
    }
}
