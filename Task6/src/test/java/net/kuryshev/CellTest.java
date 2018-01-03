package net.kuryshev;

import org.junit.Test;

import static org.junit.Assert.*;

public class CellTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnIncorrectNominal() {
        Cell cell = new Cell(-1, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnIncorrectCount() {
        Cell cell = new Cell(1, -1);
    }

    @Test
    public void shouldGetAmount() {
        Cell cell = new Cell(2, 20);
        assertEquals(40, cell.getAmount());
    }

    @Test
    public void shouldBeComparedByNominal() {
        Cell cell1 = new Cell(1, 10);
        Cell cell11 = new Cell(1, 20);
        Cell cell2 = new Cell(2, 30);
        assertEquals(0, cell1.compareTo(cell11));
        assertTrue(cell1.compareTo(cell2) < 0);
        assertTrue(cell2.compareTo(cell1) > 0);

    }
}