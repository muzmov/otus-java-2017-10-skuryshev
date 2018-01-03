package net.kuryshev;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Atm {
    private List<Cell> cells;
    private List<List<Cell>> memento = new ArrayList<>();
    private WithdrawalStrategy strategy = new GreedyStrategy();

    public Atm(List<Cell> cells) {
        if (cells == null) throw new IllegalArgumentException("Cell list should not be null");
        this.cells = CollectionUtils.deepCopy(cells);
        this.cells.sort(Comparator.reverseOrder());
        saveState();
    }

    public void setStrategy(WithdrawalStrategy strategy) {
        this.strategy = strategy;
    }

    public int getAmount() {
        return cells.stream().mapToInt(Cell::getAmount).sum();
    }

    public void withdraw(int amount) {
        try {
            strategy.processWithdrawal(cells, amount);
            saveState();
        } catch (CouldNotBeWithdrawnException e) {
            rollbackCurrentChanges();
            throw e;
        }
    }

    private void saveState() {
        List<Cell> state = CollectionUtils.deepCopy(cells);
        memento.add(state);
    }

    public void restoreInitialState() {
        restoreState(0);
    }

    private void rollbackCurrentChanges() {
        restoreState(memento.size() - 1);
    }

    private void restoreState(int index) {
        cells = CollectionUtils.deepCopy(memento.get(index));
        memento = memento.stream().limit(index + 1).collect(Collectors.toList());
    }

}
