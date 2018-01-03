package net.kuryshev;

public class LinkableCell {

    private final Cell cell;
    private LinkableCell next;

    public LinkableCell(Cell cell) {
        this.cell = cell;
    }

    public LinkableCell getNext() {
        return next;
    }

    public void setNext(LinkableCell next) {
        this.next = next;
    }

    public int getNominal() {
        return cell.getNominal();
    }

    public int getCount() {
        return cell.getCount();
    }

    public int getAmount() {
        return cell.getAmount();
    }

    public void withdraw(int count) {
        cell.withdraw(count);
    }
}
