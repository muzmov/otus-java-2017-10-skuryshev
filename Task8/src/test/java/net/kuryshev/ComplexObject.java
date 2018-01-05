package net.kuryshev;

import java.io.Serializable;
import java.util.Objects;

public class ComplexObject implements Serializable {
    private SimpleObject simpleObject;
    private String field3;

    public ComplexObject(SimpleObject simpleObject, String field3) {
        this.simpleObject = simpleObject;
        this.field3 = field3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexObject that = (ComplexObject) o;
        return Objects.equals(simpleObject, that.simpleObject) && Objects.equals(field3, that.field3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(simpleObject, field3);
    }
}
