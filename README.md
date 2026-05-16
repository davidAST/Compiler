# P-- Compiler — `feature/increment` branch

> Extends the base compiler with support for the **`++` and `--` increment/decrement operators**.

## What this branch adds

P-- variables of type `int` can now be incremented or decremented using the postfix `++` and `--` operators. These are shorthand for `i = i + 1` and `i = i - 1` respectively.

When used inside an expression (e.g. inside `print`), `i++` and `i--` behave as **postfix**: they return the original value and then apply the increment/decrement.

```
i: int;

def main()->None: {
    i = 1;
    print i++, '\n';   # prints 1, then i becomes 2
    i = 5;
    i--;               # i becomes 4
    print i, '\n';     # prints 4
    i++;               # i becomes 5
    print i, '\n';     # prints 5
}
```

## Language change

| | Before | After |
|---|---|---|
| Increment variable | `i = i + 1` | ✅ `i++` |
| Decrement variable | `i = i - 1` | ✅ `i--` |
| Postfix in expression (e.g. `print i++`) | ❌ Not supported | ✅ Returns value before increment |
| Applied to non-integer types | ❌ N/A | ❌ Type error |

## MAPL output example

Given:

```
i: int;

def main()->None: {
    i = 1;
    print i++, '\n';
    i = 5;
    i--;
    print i, '\n';
    i++;
    print i, '\n';
}
```

Generated assembly:

```asm
#source "inputs&outputs/inc-input.txt"
    ' * IntType i (offset 0)
' Invocation to the main function
call main
halt

#line 3
 main:
    ' * Parameters
    ' * Local variables
    enter  0

#line 4
    ' * Assignment
    pusha  0
    pushi  1
    storei

#line 5
    ' * Write (i++ as expression: push current value, then increment)
    pusha  0
    loadi
    pusha  0
    pusha  0
    loadi
    pushi  1
    addi
    storei
    outi

#line 5
    ' * Write
    pushb  10
    outb

#line 7
    ' * Assignment
    pusha  0
    pushi  5
    storei

    ' * Decrement (i-- as statement)
    pusha  0
    pusha  0
    loadi
    pushi  1
    subi
    storei

#line 9
    ' * Write
    pusha  0
    loadi
    outi

#line 9
    ' * Write
    pushb  10
    outb

    ' * Increment (i++ as statement)
    pusha  0
    pusha  0
    loadi
    pushi  1
    addi
    storei

#line 12
    ' * Write
    pusha  0
    loadi
    outi

#line 12
    ' * Write
    pushb  10
    outb
    ret    0, 0, 0
```

Expected output:
```
1
4
5
```

## Implementation notes

- `i++` and `i--` can be used both as **statements** and as **expressions** inside `print`.
- When used as an expression (postfix), the original value is pushed onto the stack first, then the variable is updated in memory — so the printed value is the one before the increment/decrement.
- Global variables are addressed with `pusha <offset>` instead of `push bp` + relative offset.
- Type checking enforces that the operand is an `int` lvalue; applying `++`/`--` to a `double`, `char`, or non-variable expression is a type error.

## Base

Branched from `master`. See the [main README](../master/README.md) for the full project overview, pipeline, and MAPL instruction reference.
