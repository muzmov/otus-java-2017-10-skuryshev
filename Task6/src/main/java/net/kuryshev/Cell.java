package net.kuryshev;

import java.util.Objects;

public class Cell implements Comparable<Cell>, Cloneable{
    private int nominal;
    private int count;

    public Cell(int nominal, int count) {
        if (nominal <= 0) throw new IllegalArgumentException("Nominal should be greater than zero");
        if (count < 0) throw new IllegalArgumentException("Count should be greater or equal zero");
        this.nominal = nominal;
        this.count = count;
    }

    public int getNominal() {
        return nominal;
    }

    public int getCount() {
        return count;
    }

    public int getAmount() {
        return count * nominal;
    }

    public void withdraw(int count) {
        if (count > this.count) throw new IllegalArgumentException("Trying to withdraw more than the cell contains");
        this.count -= count;
    }

    @Override
    public int compareTo(Cell cell) {
        return getNominal() - cell.getNominal();
    }

    @Override
    protected Cell clone() {
        return new Cell(nominal, count);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return nominal == cell.nominal && count == cell.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nominal, count);
    }
}
