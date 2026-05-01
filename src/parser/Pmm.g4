grammar Pmm;	

@header {
    import ast.*;
    import ast.definitions.*;
    import ast.expressions.*;
    import ast.statements.*;
    import ast.types.*;
    import ast.expressions.literals.*;
    import java.util.Set;
    import java.util.HashSet;
    import errorhandler.ErrorHandler;
}

program returns [Program ast]
       locals [List<Definition> definitions = new ArrayList<>()]
       : (func=functionDefinition {$definitions.add($func.ast); } |
          var=varDefinition{$definitions.addAll($var.ast); })* main=mainDefinition EOF
          {
            $definitions.add($main.ast);
            $ast = new Program($definitions);

          }
       ;

variables returns [List<VarDefinition> ast = new ArrayList<>()]
        locals [List<String> vars = new ArrayList<>()]
        :  ID1=ID {$vars.add($ID1.text); }
        (',' ID2=ID {$vars.add($ID2.text); })* ':' type
        {
            for (String str: $vars) {
                $ast.add(new VarDefinition($ID1.line, $ID1.getCharPositionInLine() + 1, str, $type.ast));
            }
        }
        ;

varDefinition returns [List<VarDefinition> ast = new ArrayList<>()]
    locals [List<Token> tokens = new ArrayList<>()]
    :   ID1=ID { $tokens.add($ID1); }
        (',' ID2=ID { $tokens.add($ID2); })* ':' type ';'
        {
            Set<String> seenNames = new HashSet<>();

            for (Token t : $tokens) {
                String name = t.getText();

                VarDefinition var = new VarDefinition(
                    t.getLine(),
                    t.getCharPositionInLine() + 1,
                    name,
                    $type.ast
                );

                if (!seenNames.add(name)) {
                    new ErrorType("Variable '" + name + "' already defined", var);
                } else {
                    $ast.add(var);
                }

            }
        }
    ;

params  returns [List<VarDefinition> ast = new ArrayList<>()]:
      (var1=variables {$ast.addAll($var1.ast);}
      (',' var2=variables {$ast.addAll($var2.ast); })*)?
      ;

functionDefinition returns [Definition ast]
    locals [List<VarDefinition> definitions = new ArrayList<>(),
            List<Statement> statements = new ArrayList<>()]
    : 'def' ID '(' par=params ')' '->' tp=returnType ':'
      '{'
          (varDef=varDefinition { $definitions.addAll($varDef.ast); })* (st=statement { $statements.addAll($st.ast); })* '}'
      {
        Type retType = ($tp.ast != null) ? $tp.ast : VoidType.getInstance();
        $ast = new FuncDefinition($ID.line, $ID.getCharPositionInLine() + 1,
               $ID.text, new FunctionType(retType, $par.ast),
               $definitions, $statements);
      }
    ;

mainDefinition returns [Definition ast]
    locals [List<VarDefinition> definitions = new ArrayList<>(),
            List<Statement> statements = new ArrayList<>()]
    : 'def' id='main' '('  ')' '->' 'None' ':'
      '{'
          (varDef=varDefinition { $definitions.addAll($varDef.ast); })* (st=statement { $statements.addAll($st.ast); })* '}'
      {
        $ast = new FuncDefinition($id.line, $id.getCharPositionInLine() + 1,
               $id.text,
               new FunctionType(),
               $definitions, $statements);
      }
    ;

returnType returns [Type ast]
         : tp=type {$ast= $tp.ast; }
         | 'None' {$ast = null; }
         ;

expression returns [Expression ast]
          locals [ List<Expression> expressions = new ArrayList<>() ]
          // Parenthesis
          : '(' exp=expression ')'
            {$ast = $exp.ast; }
          // Array Access
          | exp=expression '[' index=expression ']'
            {$ast = new ArrayAccess($exp.ast.getLine(), $exp.ast.getColumn(), $exp.ast, $index.ast); }
          // Field Access
          | exp=expression '.' FIELD=ID
            {$ast = new FieldAccess($exp.ast.getLine(), $exp.ast.getColumn(), $FIELD.text, $exp.ast); }
          // Cast
          | '(' tp=type ')' exp=expression
            {$ast = new Cast($exp.ast.getLine(), $exp.ast.getColumn(), $exp.ast, $tp.ast); }
          // Unary Minus
          | '-' exp=expression
            {$ast = new UnaryMinus($exp.ast.getLine(), $exp.ast.getColumn(), $exp.ast); }
          // Negation
          | '!' exp=expression
            {$ast = new Negation($exp.ast.getLine(), $exp.ast.getColumn(), $exp.ast); }
          // Arithmetic
          | left=expression OP=('*'|'/'|'%') right=expression
            {$ast = ExpressionFactory.createArithmeticOrModulus($left.ast.getLine(), $left.ast.getColumn(), $left.ast, $right.ast, $OP.text); }
          | left=expression OP=('+'|'-') right=expression
            {$ast = new Arithmetic($left.ast.getLine(), $left.ast.getColumn(), $left.ast, $right.ast, $OP.text); }
          // Comparison
          | left=expression OP=('>'|'>='|'<'|'<='|'!='|'==') right=expression
            {$ast = new Comparison($left.ast.getLine(), $left.ast.getColumn(), $left.ast, $right.ast, $OP.text); }
          // Logical
          | left=expression OP=('&&'|'||') right=expression
            {$ast = new Logical($left.ast.getLine(), $left.ast.getColumn(), $left.ast, $right.ast, $OP.text);}
          // Function Invocation
          | ID '(' (exp1=expression {$expressions.add($exp1.ast); }
           (',' exp2=expression {$expressions.add($exp2.ast); } )*)? ')'
                {$ast = new FunctionInvocation($ID.line, $ID.getCharPositionInLine()+1,
                new Variable($ID.line, $ID.getCharPositionInLine()+1,$ID.text),
                $expressions); }
          // Int Literal
          | INT=INT_CONSTANT
            {$ast = new IntLiteral($INT.line, $INT.getCharPositionInLine()+1, LexerHelper.lexemeToInt($INT.text)); }
          // Real Literal
          | REAL=REAL_CONSTANT
            {$ast = new RealLiteral($REAL.line, $REAL.getCharPositionInLine()+1, LexerHelper.lexemeToReal($REAL.text)); }
          // Char Literal
          | CHAR=CHAR_CONSTANT
            {$ast = new CharLiteral($CHAR.line, $CHAR.getCharPositionInLine()+1, LexerHelper.lexemeToChar($CHAR.text)); }
          // Variables
          | VAR=ID
            { $ast = new Variable($VAR.line, $VAR.getCharPositionInLine()+1, $VAR.text); }
          ;

statement returns [ List<Statement> ast = new ArrayList<>() ]
        locals [ List<Statement> statements = new ArrayList<>(),
                 List<Expression> expressions = new ArrayList<>(),
                 Expression returnExpression = null ]
        // Print
        : 'print' exp=expression
            {$ast.add(new Print($exp.ast.getLine(), $exp.ast.getColumn(), $exp.ast));}
            (',' exp2=expression
            {$ast.add(new Print($exp2.ast.getLine(), $exp2.ast.getColumn(), $exp2.ast)); }
            )* ';'
        // Read
        | 'input' exp=expression
            {$ast.add(new Read($exp.ast.getLine(), $exp.ast.getColumn(), $exp.ast)); }
            (',' exp2=expression
            {$ast.add(new Read($exp2.ast.getLine(), $exp2.ast.getColumn(), $exp2.ast)); }
            )* ';'
        // Assignment
        | left=expression '=' right=expression ';'
            {$ast.add(new Assignment($left.ast.getLine(), $left.ast.getColumn(), $left.ast, $right.ast)); }
        // If - Else
        | 'if' exp=expression ':' b1=block 'else' ':'  b2=block
            {$ast.add(new IfElse($exp.ast.getLine(), $exp.ast.getColumn(), $b1.ast, $exp.ast, $b2.ast)); }
        // If
        | 'if' exp=expression ':' b1=block
            {$ast.add(new IfElse($exp.ast.getLine(), $exp.ast.getColumn(), $b1.ast, $exp.ast)); }
        // While
        | 'while' exp=expression ':' b2=block
            {$ast.add(new While($exp.ast.getLine(), $exp.ast.getColumn(), $exp.ast, $b2.ast)); }
        // Return
        | RET='return' (exp=expression { $returnExpression = $exp.ast; })? ';'
            { $ast.add(new Return($RET.line, $RET.getCharPositionInLine()+1, $returnExpression)); }
        // Function Invocation
        | ID '(' (exp1=expression {$expressions.add($exp1.ast); }
          (',' exp2=expression {$expressions.add($exp2.ast); } )*)? ')' ';'
                {$ast.add(new FunctionInvocation($ID.line, $ID.getCharPositionInLine()+1,
                 new Variable($ID.line, $ID.getCharPositionInLine()+1,$ID.text),
                 $expressions)); }
        ;

block returns [List<Statement> ast = new ArrayList<>()]
     : st = statement {$ast.addAll($st.ast); }
     | '{' (st = statement {$ast.addAll($st.ast);}) * '}'
     ;

type returns [Type ast]
    locals [ List<RecordField> records = new ArrayList<>() ]
    // Char
    : 'char'
      {$ast = CharType.getInstance(); }
    // Int
    | 'int'
      {$ast = IntType.getInstance(); }
    // Double
    | 'double'
      {$ast = RealType.getInstance(); }
    // Array
    | '[' SIZE=INT_CONSTANT ']' tp=type
      {$ast = new ArrayType(LexerHelper.lexemeToInt($SIZE.text), $type.ast); }
    // Struct
    | 'struct' '{'
      { Set<String> structNames = new HashSet<>(); }
      (sf = recordField
      {
        for(RecordField rf : $sf.ast) {
            if (!structNames.add(rf.getName())) {
                new ErrorType("Field '" + rf.getName() + "' already defined in struct", rf);
            } else {
                $records.add(rf);
            }
        }
      }) +
      '}'
      { $ast = new StructType($records); }
    ;

recordField returns [List<RecordField> ast = new ArrayList<>()]
        locals [List<Token> tokens = new ArrayList<>()]:
        ID1=ID { $tokens.add($ID1); }
        (',' ID2=ID { $tokens.add($ID2); })* ':' type ';'
        {
            Set<String> lineNames = new HashSet<>();
            for (Token t : $tokens) {
                String name = t.getText();
                RecordField rec = new RecordField(
                    t.getLine(),
                    t.getCharPositionInLine() + 1,
                    name,
                    $type.ast
                );
                if (!lineNames.add(name)) {
                    new ErrorType("Field '" + name + "' duplicated in same line", rec);
                } else {
                    $ast.add(rec);
                }
            }
        }
        ;

fragment
DIGIT: [0-9]
     ;

ID: [a-zA-Z_] [a-zA-Z0-9_]*
  ;

INT_CONSTANT: '0'
            | [1-9] DIGIT*
            ;

fragment
EXPONENT: [eE] [+-]? DIGIT+
        ;

REAL_CONSTANT: DIGIT+ '.' DIGIT* EXPONENT?
             | '.' DIGIT+ EXPONENT?
             | DIGIT+ EXPONENT
             ;

CHAR_CONSTANT: '\'' '\\' DIGIT* '\''      // Char constants identifying the ASCII code, e.g. '\126'
            | '\'' '\\n' '\''        // The special char constant '\n'
            | '\'' '\\t' '\''        // The special char constant '\t'
            | '\'' . '\''            // Char constants between ' and '
            ;

SINGLE_LINE_COMMENT: '#' ~[\n\r]* -> skip
                   ;

MULTI_LINE_COMMENT: '"""' .*? '"""' -> skip
                  ;

WS: [ \t\r\n]+ -> skip
    ;

