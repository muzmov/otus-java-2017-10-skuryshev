package net.kuryshev.message;

import net.kuryshev.ms.Address;

/**
 * Created by tully.
 */
public abstract class Msg {
    public static final String CLASS_NAME_VARIABLE = "className";

    private final String className;
    private final Address frontAddress;

    protected Msg(Class<?> klass, Address frontAddress) {
        this.frontAddress = frontAddress;
        this.className = klass.getName();
    }

    public Address getFrontAddress() {
        return frontAddress;
    }

    @Override
    public String toString() {
        return "Msg{" + "className='" + className + '\'' + ", frontAddress=" + frontAddress + '}';
    }
}
