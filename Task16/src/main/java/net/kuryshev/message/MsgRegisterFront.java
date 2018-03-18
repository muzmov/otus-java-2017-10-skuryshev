package net.kuryshev.message;

import net.kuryshev.ms.Address;

/**
 * Created by tully.
 */
public class MsgRegisterFront extends Msg {
    public MsgRegisterFront(Address frontAddress) {
        super(MsgRegisterFront.class, frontAddress);
    }

}
