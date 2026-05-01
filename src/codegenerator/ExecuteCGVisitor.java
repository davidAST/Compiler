package codegenerator;

import ast.Program;
import ast.definitions.Definition;
import ast.definitions.FuncDefinition;
import ast.definitions.VarDefinition;
import ast.statements.*;
import ast.types.FunctionType;
import ast.types.VoidType;

/*
 *   The Execute visitor only visits
 *   - Program
 *   - Statements
 *   - Definitions
 */
public class ExecuteCGVisitor extends AbstractCGVisitor<FuncDefinition, Void> {
    private final ValueCGVisitor valueV;
    private final AddressCGVisitor addressV;
    private final CodeGenerator cg;

    public ExecuteCGVisitor(CodeGenerator cg) {
        this.cg = cg;
        this.valueV = new ValueCGVisitor(cg);
        this.addressV = new AddressCGVisitor(cg);
        valueV.setAddressVisitor(addressV);
        addressV.setValueVisitor(valueV);
    }

    // Program ===============================================================

    @Override
    public Void visit(Program program, FuncDefinition param) {
        /*
         * execute [[Program : program -> definition*]]() =
         *      for (Definition def : definition*)
         *          if (def instanceof VarDefinition)
         *              execute[[def]]()
         *      <call main>
         *      <halt>
         *      for (Definition def:: definition*)
         *          if (def instanceof FuncDefinition)
         *              execute[[def]]()
         */

        cg.source();
        for (Definition def : program.getDefinitions()) {
            if (def instanceof VarDefinition) {
                def.accept(this, param);
            }
        }
        cg.commentSimple("Invocation to the main function");
        cg.call("main");
        cg.halt();
        for (Definition def : program.getDefinitions()) {
            if (def instanceof FuncDefinition) {
                def.accept(this, param);
            }
        }
        cg.close();
        return null;
    }

    // Definitions ===============================================================

    @Override
    public Void visit(FuncDefinition funcDefinition, FuncDefinition param) {
        /*
         * execute [[FuncDefinition : definition -> type ID definition* statement*]] =
         *      ID<:>
         *      <' * Parameters>
         *      definition.type.params.forEach(parameter -> execute[[parameter]]())
         *      <' * Local variables>
         *      definition*forEach(definition -> execute[[definition]]())
         *      <enter> funcDefinition.localVarsSize
         *      statement*.forEach(statement -> execute[[statement]]())
         *      <ret> type.returnType.numberOfBytes, definition.localVarsSize, type.paramsSize
         */
        cg.line(funcDefinition.getLine());
        cg.label(funcDefinition.getName());
        cg.comment("Parameters");
        FunctionType type = (FunctionType) funcDefinition.getType();
        for (VarDefinition arg : type.getParams()) {
            arg.accept(this, param);
        }
        cg.comment("Local variables");
        for (Definition def : funcDefinition.getDefinitions()) {
            def.accept(this, param);
        }
        cg.enter(funcDefinition.getLocalVarsSize());
        for (Statement statement : funcDefinition.getStatements()) {
            statement.accept(this, funcDefinition);
        }

        FunctionType fType = (FunctionType) funcDefinition.getType();
        if (fType.getReturnType().equals(VoidType.getInstance())) {
            cg.ret(type.getReturnType().numberOfBytes(), funcDefinition.getLocalVarsSize(), type.getParamsSize());

        }

        return null;
    }

    @Override
    public Void visit(VarDefinition varDefinition, FuncDefinition param) {
        /*
         * execute [[VarDefinition : definition -> ID type]] =
         *      <` * > varDefinition.type ID <(offset > varDefinition.offset <)>
         *
         */
        cg.comment(varDefinition.getType() + " " +
                        varDefinition.getName() + " (offset " +
                        varDefinition.getOffset() + ")");
        return null;
    }

    // Statements ===============================================================

    public Void visit(Print print, FuncDefinition param) {
        /*
         * execute [[Print : statement -> expression]]() =
         *      value [[statement]]
         *      <out> expression.type.suffix()
         */
        cg.line(print.getLine());
        cg.comment("Write");
        print.getExpression().accept(valueV, null);
        cg.out(print.getExpression().getType().suffix());
        return null;
    }


    @Override
    public Void visit(Read read, FuncDefinition param) {
        /*
         * execute [[Read : statement -> expression]]() =
         *      address [[statement]]
         *      <in> expression.type.suffix()
         *      <store> expression.type.suffix()
         *
         */
        cg.line(read.getLine());
        cg.comment("Read");
        read.getExpression().accept(addressV, null);
        cg.in(read.getExpression().getType().suffix());
        cg.store(read.getExpression().getType().suffix());
        return null;
    }

    @Override
    public Void visit(Assignment assignment, FuncDefinition param) {
        /*
         * execute [[Assignment : statement -> expression1 expression2]]() =
         *      address [[expression1]]
         *      value [[expression2]]
         *      expression.type.convertTo()
         *      <store> statement.type.suffix
         */
        cg.line(assignment.getLine());
        cg.comment("Assignment");
        assignment.getLeft().accept(addressV, null);
        assignment.getRight().accept(valueV, null);
        assignment.getRight().getType().convertTo(cg, assignment.getLeft().getType());
        cg.store(assignment.getLeft().getType().suffix());
        return null;
    }

    @Override
    public Void visit(IfElse ifElse, FuncDefinition param) {
        /*
         * execute [[IfElse : statement -> expression statement2* statement3*]]() =
         *      String else = cg.getLabel()
         *      String end = cg.getLabel()
         *
         *      value [[ expression ]]
         *      <jz else>
         *
         *      statement2*.forEach(stmt -> execute [[stmt]])
         *      <jmp end>
         *
         *      else <:>
         *      statement3*.forEach(stmt -> execute [[stmt]])
         *
         *      end <:>
         */
        cg.line(ifElse.getLine());
        cg.comment("If else");

        String elseLbl = cg.getLabel();
        String endLbl = cg.getLabel();

        ifElse.getCondition().accept(valueV, null);
        cg.jz(elseLbl);

        ifElse.getIfBody().forEach(stmt -> stmt.accept(this, param));
        cg.jmp(endLbl);

        cg.label(elseLbl);
        ifElse.getElseBody().forEach(stmt -> stmt.accept(this, param));

        cg.label(endLbl);
        return null;
    }

    @Override
    public Void visit(While whileStatement, FuncDefinition param) {
        /*
         * execute [[While : statement -> expression statement*]]() =
         *      String condition = cg.getLabel()
         *      String end = cg.getLabel()
         *
         *      condition <:>
         *      value [[expression]]
         *      <jz end>
         *      statement*.forEach(stmt -> execute [[stmt]]())
         *      <jmp condition>
         *
         *      end <:>
         */
        cg.line(whileStatement.getLine());
        cg.comment("While");
        cg.line(whileStatement.getLine());
        String startWhileLabel =  cg.getLabel();
        String endWhileLabel = cg.getLabel();
        cg.label(startWhileLabel);
        whileStatement.getCondition().accept(valueV, null);
        cg.jz(endWhileLabel);
        cg.comment("While body");
        whileStatement.getBody().forEach(stmt -> stmt.accept(this, param));
        cg.jmp(startWhileLabel);
        cg.label(endWhileLabel);
        return null;
    }

    @Override
    public Void visit(Return returnStatement, FuncDefinition funcDef) {
        /*
         * execute [[Return : statement -> expression]](funcDef) =
         *      value[[expression]]
         *      cg.convert(expression.type, funcDef.type.returnType)
         *      <ret>   funcDef.type.returnType.numberOfBytes <,>
         *              funcDef.bytesLocalSum <,>
         *              funcDef.type.bytesParamSum
         */
        cg.line(returnStatement.getLine());
        cg.comment("Return");
        FunctionType fType = (FunctionType) funcDef.getType();

        if (returnStatement.getExpression() != null) {
            returnStatement.getExpression().accept(valueV, null);
            cg.convert(returnStatement.getExpression().getType(), fType.getReturnType());
        }

        int bytesLocalSum = getBytesLocalSum(funcDef);
        int bytesParamSum = getBytesParamSum(fType);

        cg.ret(fType.getReturnType().numberOfBytes(),
                bytesLocalSum,
                bytesParamSum);

        return null;
    }

    private int getBytesLocalSum(FuncDefinition funcDef) {
        int fieldBytesSum = 0;
        for (VarDefinition varDef : funcDef.getDefinitions()) {
            fieldBytesSum += varDef.getType().numberOfBytes();
        }
        return fieldBytesSum;
    }

    private int getBytesParamSum(FunctionType fType) {
        int fieldBytesSum = 0;
        for (int count = fType.getParams().size() - 1; count >= 0; count--) {
            VarDefinition varDef = fType.getParams().get(count);
            fieldBytesSum += varDef.getType().numberOfBytes();
        }
        return fieldBytesSum;
    }

    @Override
    public Void visit(FunctionInvocation funcInvocation, FuncDefinition param) {
        /*
         * value[[FuncInvocation: statement -> expression2 expression*]]() =
         *      value[[(Expression) statement]]()
         *      if (!(Expression) statement).type instanceof VoidType) {
         *          <pop> ((Expression) statement).type.suffix()
         *      }
         */
        cg.line(funcInvocation.getLine());
        funcInvocation.accept(valueV, null);
        if (!(funcInvocation.getType() instanceof VoidType)) {
            cg.pop(funcInvocation.getType().suffix());
        }
        return null;
    }
}
