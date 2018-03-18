package net.kuryshev.message;

import net.kuryshev.ms.Address;

/**
 * Created by tully.
 */
public class MsgToFront extends Msg {
    private String text;

    public MsgToFront(Address frontAddress) {
        super(MsgToFront.class, frontAddress);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
