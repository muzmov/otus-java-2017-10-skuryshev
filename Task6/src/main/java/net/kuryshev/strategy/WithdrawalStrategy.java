package net.kuryshev.strategy;

import net.kuryshev.Cell;
import net.kuryshev.exception.CouldNotBeWithdrawnException;

import java.util.List;

public interface WithdrawalStrategy {

    void processWithdrawal(List<Cell> sortedCells, int amount) throws CouldNotBeWithdrawnException;
}
