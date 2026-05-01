# P-- Compiler — `feature/void-return` branch

> Extends the base compiler with support for **early return from void functions** via `return;`.

## What this branch adds

P-- functions that return `None` can now exit early using a bare `return;` statement. Without this feature, the only way to exit a void function was to reach its closing brace.

```
def checkIndex(index:int)->None: {
    if index < 5: {
        print 0,'\n';
        return;          # ← early exit if index is too small
    }
    print index,'\n';
}

def main()->None: {
    value: int;
    input value;
    checkIndex(value);
}
```

## Language change

| | Before | After |
|---|---|---|
| Early exit from `void` function | ❌ Not supported | ✅ `return;` |
| Return with value from `void` | ❌ Type error | ❌ Still a type error |
| `return <expr>;` in typed functions | ✅ Already supported | ✅ Unchanged |

## MAPL output example

Given:

```
def checkIndex(index:int)->None: {
    if index < 5: {
        print 0,'\n';
        return;
    }
    print index,'\n';
}

def main()->None: {
    value: int;
    input value;
    checkIndex(value);
}
```

Generated assembly:
 
```asm
#source "ret-input.txt"
 
' Invocation to the main function
call main
halt
 
 
#line   1
 
 checkIndex:
    ' * Parameters
    ' * IntType index (offset 4)
    ' * Local variables
    enter  0
 
#line   2
    ' * If else
    push   bp
    pushi  4
    addi
    loadi
    pushi  5
    lti
    jz label0
 
#line   3
    ' * Write
    pushi  0
    outi
 
#line   3
    ' * Write
    pushb  10
    outb
 
#line   4
    ' * Return
    ret    0, 0, 2    ; ← early return: pop 0 locals, push 0 return value, 2 bytes of params
    jmp    label1
 
 label0:
 
 label1:
 
#line   6
    ' * Write
    push   bp
    pushi  4
    addi
    loadi
    outi
 
#line   6
    ' * Write
    pushb  10
    outb
    ret    0, 0, 2
 
#line   9
 
 main:
    ' * Parameters
    ' * Local variables
    ' * IntType value (offset -2)
    enter  2
 
#line   11
    ' * Read
    push   bp
    pushi  -2
    addi
    ini
    storei
 
#line   12
    push   bp
    pushi  -2
    addi
    loadi
call checkIndex
    ret    0, 2, 0
```


## Base

Branched from `master`. See the [main README](../master/README.md) for the full project overview, pipeline, and MAPL instruction reference.
