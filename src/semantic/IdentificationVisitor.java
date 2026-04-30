package semantic;

import ast.definitions.*;
import ast.statements.Statement;
import ast.types.ErrorType;
import ast.expressions.Variable;
import symboltable.SymbolTable;

public class IdentificationVisitor extends AbstractVisitor<Void, Void> {

    private final SymbolTable symbolTable = new SymbolTable();

    @Override
    public Void visit(VarDefinition variableDefinition, Void parameter) {
        // 1. Inserts the variable definition into the symbol table
        //    If the variable is already defined, throw a new Error
        if (!symbolTable.insert(variableDefinition)) {
            new ErrorType(
                "The variable with name " + variableDefinition.getName() + " is already defined.",
                variableDefinition
            );
        }

        // 2. Visit the type of the variable definition
        variableDefinition.getType().accept(this, parameter);

        return null;
    }

    @Override
    public Void visit(FuncDefinition functionDefinition, Void parameter) {
        // 1. Inserts the function definition into the symbol table
        //    If that function is already defined, throw a new Error
        if (!symbolTable.insert(functionDefinition)) {
            new ErrorType(
                    "The function with name " + functionDefinition.getName() + " is already defined.",
                    functionDefinition
            );
        }

        // 3. Increases the scope
        symbolTable.set();

        // 4. Visits attributes of the definition
        functionDefinition.getType().accept(this, parameter);
        for (Definition def : functionDefinition.getDefinitions() ) {
            def.accept(this, parameter);
        }
        for (Statement st : functionDefinition.getStatements() ) {
            st.accept(this, parameter);
        }

        // 5. Resets the scope
        symbolTable.reset();

        return null;
    }

    @Override
    public Void visit(Variable variable, Void parameter) {
        Definition def = symbolTable.find(variable.getName());
        // 1. Check that the variable is already defined
        if (def == null) {
            new ErrorType(
                    "The variable " + variable.getName() + " is not defined yet",
                    variable
            );
        }

        // 2. Set the definition in the variable
        variable.setVarDef(def);

        return null;
    }

}
