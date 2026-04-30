package codegenerator;

import ast.Program;
import ast.RecordField;
import ast.definitions.Definition;
import ast.definitions.FuncDefinition;
import ast.definitions.VarDefinition;
import ast.types.FunctionType;
import ast.types.StructType;
import semantic.AbstractVisitor;

public class OffSetVisitor extends AbstractVisitor<Void, Void> {
    // Fields (Struct)
    @Override
    public Void visit(StructType rt, Void param) {
        int fieldBytesSum = 0;
        for (RecordField rf : rt.getFields()) {
            rf.setOffset(fieldBytesSum);
            fieldBytesSum += rf.getType().numberOfBytes();
        }
        return null;
    }

    // Parameters
    @Override
    public Void visit(FunctionType ft, Void param) {
        int fieldBytesSum = 4;
        for (int count = ft.getParams().size() - 1; count >= 0; count--) {
            VarDefinition varDef = ft.getParams().get(count);
            varDef.setOffset(fieldBytesSum);
            fieldBytesSum += varDef.getType().numberOfBytes();
        }
        ft.setParamsSize(fieldBytesSum - 4);
        return null;
    }

    // Local Variables
    @Override
    public Void visit(FuncDefinition funcDef, Void param) {
        int fieldBytesSum = 0;
        for (VarDefinition varDef : funcDef.getDefinitions()) {
            fieldBytesSum += varDef.getType().numberOfBytes();
            varDef.setOffset(-fieldBytesSum);
            varDef.getType().accept(this, param);
        }
        funcDef.getType().accept(this, param);
        funcDef.setLocalVarsSize(fieldBytesSum);
        return null;
    }

    // Global Variables
    @Override
    public Void visit(Program program, Void param) {
        int fieldBytesSum = 0;
        for (Definition def : program.getDefinitions()) {
            if (def instanceof VarDefinition varDef) {
                varDef.setOffset(fieldBytesSum);
                fieldBytesSum += def.getType().numberOfBytes();
            }
            def.accept(this, param);
        }
        return null;
    }
}

