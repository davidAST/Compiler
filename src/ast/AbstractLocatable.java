package ast;

public abstract class AbstractLocatable implements Locatable {
    private final int column;
    private final int line;

    public AbstractLocatable(int line, int column) {
        this.column = column;
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}


