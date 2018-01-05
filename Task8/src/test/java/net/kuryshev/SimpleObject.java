package net.kuryshev;

import java.io.Serializable;
import java.util.Objects;

public class SimpleObject implements Serializable {
    private String field1;
    private int field2;

    public SimpleObject(String field1, int field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleObject that = (SimpleObject) o;
        return field2 == that.field2 && Objects.equals(field1, that.field1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field1, field2);
    }
}
