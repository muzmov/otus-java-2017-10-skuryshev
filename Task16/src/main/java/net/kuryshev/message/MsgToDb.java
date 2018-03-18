package net.kuryshev.message;

import net.kuryshev.ms.Address;

/**
 * Created by tully.
 */
public class MsgToDb extends Msg {

    public MsgToDb(Address frontAddress) {
        super(MsgToDb.class, frontAddress);
    }

    @Override
    public String toString() {
        return "MsgToDb{} " + super.toString();
    }
}
