package net.kuryshev;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class RecursiveStrategy implements WithdrawalStrategy{

    @Override
    public void processWithdrawal(List<Cell> sortedCells, int amount) throws CouldNotBeWithdrawnException {
        LinkableCell first = linkCells(sortedCells);
        if (!recursiveWithdraw(first, amount)) throw new CouldNotBeWithdrawnException();
    }

    private boolean recursiveWithdraw(LinkableCell cell, int amount) {
        if (amount == 0) return true;
        if (cell == null) return false;
        int maxCount = Math.min(amount / cell.getNominal(), cell.getCount());
        for (int withdrawnCount = 0; withdrawnCount <= maxCount; withdrawnCount++) {
            if (recursiveWithdraw(cell.getNext(), amount - withdrawnCount * cell.getNominal())) {
                cell.withdraw(withdrawnCount);
                return true;
            }
        }
        return false;
    }

    private LinkableCell linkCells(List<Cell> sortedCells) {
        List<LinkableCell> linkableCells = sortedCells.stream().map(LinkableCell::new).collect(Collectors.toList());
        Iterator<LinkableCell> iterator = linkableCells.iterator();
        if (!iterator.hasNext()) return null;
        LinkableCell cellA = iterator.next();
        while (iterator.hasNext()) {
            LinkableCell cellB = iterator.next();
            cellA.setNext(cellB);
            cellA = cellB;
        }
        return linkableCells.get(0);
    }
}
