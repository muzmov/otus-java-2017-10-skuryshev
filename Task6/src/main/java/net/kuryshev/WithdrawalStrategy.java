package net.kuryshev;

import java.util.List;

public interface WithdrawalStrategy {

    void processWithdrawal(List<Cell> sortedCells, int amount) throws CouldNotBeWithdrawnException;
}
