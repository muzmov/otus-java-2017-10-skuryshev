package net.kuryshev.strategy;

import net.kuryshev.Cell;
import net.kuryshev.exception.CouldNotBeWithdrawnException;

import java.util.List;

public class GreedyStrategy implements WithdrawalStrategy {

    @Override
    public void processWithdrawal(List<Cell> sortedCells, int amount) throws CouldNotBeWithdrawnException {
        for (Cell cell : sortedCells) {
            int count = Math.min(amount / cell.getNominal(), cell.getCount());
            cell.withdraw(count);
            amount -= count * cell.getNominal();
            if (amount == 0) break;
        }
        if (amount != 0) throw new CouldNotBeWithdrawnException("Requested amount could not be withdrawn");
    }
}
