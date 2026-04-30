# P-- Compiler

A compiler for **P--** (P minus minus), a statically-typed imperative language inspired by C++ but designed for educational purposes. Built in Java using ANTLR4, it compiles P-- source code into MAPL assembly, which runs on the MAPL virtual machine.

## Language Overview

P-- supports:
- Primitive types: `int`, `double`, `char`
- Composite types: arrays, structs
- Control flow: `if/else`, `while`
- Functions with parameters and return values
- Type casting and arithmetic/logical expressions
- `print` and `input` statements

Example P-- program:
```
def main()->None: {
    i: int;
    i = 0;
    while i < 10: {
        print i, '\n';
        i = i + 1;
    }
}
```

## Project Structure

```
src/
├── ast/
│   ├── definitions/       # VarDefinition, FuncDefinition
│   ├── expressions/       # Arithmetic, logical, access expressions
│   ├── statements/        # Assignment, Print, Read, If, While, Return...
│   └── types/             # IntType, CharType, RealType, ArrayType, StructType...
├── codegenerator/
│   ├── CodeGenerator.java         # Low-level MAPL instruction emitter
│   ├── ExecuteCGVisitor.java      # Code generation for statements/definitions
│   ├── ValueCGVisitor.java        # Code generation for expressions (by value)
│   └── AddressCGVisitor.java      # Code generation for expressions (by address)
├── semantic/
│   ├── IdentificationVisitor.java # Symbol resolution
│   └── TypeCheckingVisitor.java   # Type checking and inference
├── parser/
│   ├── Pmm.g4                     # ANTLR4 grammar
│   └── ...                        # Generated lexer/parser
└── symboltable/
    └── SymbolTable.java
test/
inputs&outputs/                    # Sample programs and expected outputs
mapl/                              # MAPL virtual machine
```

## Compilation Pipeline

```
Source (.pmm)
     │
     ▼
  Lexer/Parser (ANTLR4)
     │
     ▼
  AST Construction
     │
     ▼
  Identification (symbol table)
     │
     ▼
  Type Checking
     │
     ▼
  Code Generation
     │
     ▼
  MAPL Assembly (.mapl)
     │
     ▼
  MAPL Virtual Machine → Output
```

## Requirements

- Java 17+
- ANTLR4 (included in `lib/`)
- MAPL virtual machine (included in `mapl/`)

## Running

1. Compile the project in IntelliJ or with `javac`.
2. Run the `Main` class passing the input source file and the output assembly file as arguments:

```bash
java Main <input.txt> <output.txt>
```

- `input.txt` — P-- source code
- `output.txt` — generated MAPL assembly

If there are semantic or type errors, they will be printed to stderr and no output file will be generated.

3. Run the generated assembly with the MAPL virtual machine:

```bash
java -jar mapl/mapl.jar output.txt
```

Or use the provided `MAPL.cmd` script on Windows.

## Target: MAPL Assembly

The compiler targets MAPL, a stack-based virtual machine. Key instructions:

| Instruction | Description |
|---|---|
| `push`, `pushi`, `pushf` | Push values onto the stack |
| `loadi`, `storei` | Load/store integers |
| `call`, `ret` | Function call/return |
| `jmp`, `jz` | Unconditional/conditional jumps |
| `outi`, `outf`, `outb` | Print int/double/char |
| `enter` | Allocate local variable space |

## Example

Input program (`input.txt`):
```
v:[10]double;

# Main program
def main()->None: {
    value:double;
    i,j:int;
    w:[4][5]int;
    date:struct { 
       day, month, year:int;     
    };
    
    input date.day; 
    date.year = 'a'; 
    date.month = date.day*date.year%12+1;
    print date.day, '\n', date.month, '\n', (double)(date.year), '\n';
    
    input value;
       
    i=0;   
    while i<10: {
       v[i]=value;
       print i,':',v[i], ' ';
       if i%2:
          print 'o','d','d','\n';
       else:
          print 'e','v','e','n','\n';
       i=i+1;
    }
    print '\n';

    i=0;
    while i<4: {
       j=0;
       while j<5: {
          w[i][j]=i+j;
          print w[i][j], ' ';
          j=j+1;
       }
       print '\n';
       i=i+1;
    }
}
```

Running the generated assembly on the MAPL VM:
```
>Integer value: 10
10
11
97.0

>Float value: 20.0
0:20.0 even
1:20.0 odd
2:20.0 even
3:20.0 odd
4:20.0 even
5:20.0 odd
6:20.0 even
7:20.0 odd
8:20.0 even
9:20.0 odd

0 1 2 3 4 
1 2 3 4 5 
2 3 4 5 6 
3 4 5 6 7 
```

## License

MIT
