package codegenerator;

import ast.types.Type;

import java.io.*;

public class CodeGenerator {
    private final String inputFile;
    private PrintWriter out;
    private int labels = 0;

    public CodeGenerator(String outputFile, String inputFile) {
        this.inputFile = inputFile;
        try {
            this.out = new PrintWriter(new FileWriter(outputFile));
        } catch (IOException e) {
            System.err.println("Error al crear el archivo de salida: " + e.getMessage());
        }
    }

    // --- Labels ---
    public String getLabel() {
        return "label" + this.labels++;
    }

    // --- Conversions ---
    public void convert(Type from, Type to) {
        from.convertTo(this, to);
    }

    // --- Utility Methods ---
    public void close() {
        if (out != null) out.close();
    }

    public void comment(String msg) {
        out.println("\t' * " + msg);
    }

    public void commentSimple(String msg) {
        out.println("' " + msg);
    }

    public void label(String id) {
        out.println("\n " + id + ":");
    }

    // --- Push Instructions ---
    public void push(int suffix, int val) { out.println("\tpush" + suffix + "\t" + val); }
    public void pushb(int ascii) { out.println("\tpushb\t" + ascii); }
    public void pushi(int val) { out.println("\tpushi\t" + val); }
    public void pushf(double val) { out.println("\tpushf\t" + val); }
    public void pusha(int addr) { out.println("\tpusha\t" + addr); }
    public void pushbp() { out.println("\tpush\tbp"); }

    // --- Load and Store ---
    public void load(char type) { out.println("\tload" + type); }
    public void store(char type) { out.println("\tstore" + type); }

    // --- Pop and Duplicate ---
    public void pop(char type) { out.println("\tpop" + type); }
    public void dup(char type) { out.println("\tdup" + type); }

    // --- Arithmetic ---
    public void add(char type) { out.println("\tadd" + type); }
    public void sub(char type) { out.println("\tsub" + type); }
    public void mul(char type) { out.println("\tmul" + type); }
    public void div(char type) { out.println("\tdiv" + type); }
    public void mod() { out.println("\tmodi"); }

    // --- Comparison ---
    public void gt(char type) { out.println("\tgt" + type); }
    public void lt(char type) { out.println("\tlt" + type); }
    public void ge(char type) { out.println("\tge" + type); }
    public void le(char type) { out.println("\tle" + type); }
    public void eq(char type) { out.println("\teq" + type); }
    public void ne(char type) { out.println("\tne" + type); }

    // --- Logical ---
    public void and() { out.println("\tand"); }
    public void or() { out.println("\tor"); }
    public void not() { out.println("\tnot"); }

    // --- I/O ---
    public void out(char type) { out.println("\tout" + type); }
    public void in(char type) { out.println("\tin" + type); }

    // --- Conversions ---
    public void b2i() { out.println("\tb2i"); }
    public void i2f() { out.println("\ti2f"); }
    public void f2i() { out.println("\tf2i"); }
    public void i2b() { out.println("\ti2b"); }

    // --- Jumps ---
    public void jmp(String label) { out.println("\tjmp\t" + label); }
    public void jz(String label) { out.println("\tjz\t" + label); }
    public void jnz(String label) { out.println("\tjnz\t" + label); }

    // --- Functions ---
    public void call(String id) { out.println("call " + id); }
    public void enter(int bytes) { out.println("\tenter\t" + bytes); }
    public void ret(int retBytes, int localBytes, int argBytes) {
        out.println("\tret\t" + retBytes + ", " + localBytes + ", " + argBytes);
    }
    public void halt() {
        out.println("halt");
        out.println();
    }

    // --- Debugging ---
    public void source() {
        File file = new File(this.inputFile);
        out.println("\n#source\t\"" + file.getName() + "\"");
        out.println();
    }
    public void line(int lineNum) {
        out.println("\n#line\t" + lineNum);
    }
}