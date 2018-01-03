package net.kuryshev;

import java.util.ArrayList;
import java.util.List;

public class AtmDepartment extends Atm {
    private List<Atm> atms;

    public AtmDepartment(List<Atm> atms) {
        super(new ArrayList<>());
        this.atms = atms;
    }

    @Override
    public void setStrategy(WithdrawalStrategy strategy) {
        atms.forEach(atm -> atm.setStrategy(strategy));
    }

    @Override
    public int getAmount() {
        return atms.stream().mapToInt(atm -> atm.getAmount()).sum();
    }

    @Override
    public void withdraw(int amount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void restoreInitialState() {
        atms.forEach(Atm::restoreInitialState);
    }
}
