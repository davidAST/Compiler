package ast.types;

import ast.AbstractLocatable;
import ast.Locatable;
import ast.RecordField;
import visitor.Visitor;

import java.util.List;

public class StructType extends AbstractType {
    private final List<RecordField> fields;

    public StructType(List<RecordField> fields) {
        this.fields = fields;
    }

    public List<RecordField> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("RecordType[fields:[");
        for (int i = 0; i < fields.size(); i++) {
            RecordField f = fields.get(i);
            sb.append("Field[name:").append(f.getName())
                    .append(",type:").append(f.getType())
                    .append(" offset:").append(f.getOffset()).append("]");
            if (i < fields.size() - 1) sb.append(", ");
        }
        sb.append("]]");
        return sb.toString();
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP parameter) {
        return visitor.visit(this, parameter);
    }

    @Override
    public Type dot(String name, Locatable locatable) {
        for (RecordField field : fields) {
            if (field.getName().equals(name)) {
                return field.getType();
            }
        }
        String message = "Field '" + name + "' not found in " + this.toString();
        return new ErrorType(message, locatable);
    }

    @Override
    public int numberOfBytes() {
        return fields.stream().mapToInt(rf -> rf.getType().numberOfBytes()).sum();
    }

    public RecordField getField(String name) {
        for (RecordField field : fields) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        throw new IllegalStateException("Field '" + name + "' not found in " + this.toString() + " - THIS SHOULD NEVER HAPPEN!");
    }
}
