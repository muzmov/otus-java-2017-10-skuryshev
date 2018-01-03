package net.kuryshev;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class AtmTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullCellList() {
        Atm atm = new Atm(null);
    }

    @Test
    public void shouldGetAmount() {
        List<Cell> cells = Arrays.asList(new Cell(1, 10), new Cell(2, 20));
        Atm atm = new Atm(cells);
        assertEquals(50, atm.getAmount());
    }

    @Test
    public void shouldReturnZeroAmountForEmptyCellList() {
        Atm atm = new Atm(new ArrayList<>());
        assertEquals(0, atm.getAmount());
    }

    @Test(expected = CouldNotBeWithdrawnException.class)
    public void shouldThrowExceptionOnTooLargeWithdrawRequest() {
        List<Cell> cells = Arrays.asList(new Cell(1, 10), new Cell(2, 20));
        Atm atm = new Atm(cells);
        atm.withdraw(60);
    }

    @Test(expected = CouldNotBeWithdrawnException.class)
    public void shouldThrowExceptionOnIncorrectRequest() {
        List<Cell> cells = Arrays.asList(new Cell(5, 10), new Cell(2, 20));
        Atm atm = new Atm(cells);
        atm.withdraw(11);
    }

    @Test
    public void shouldRestoreItsStateAfterIncorrectWithdrawalAttempt() {
        List<Cell> cells = Arrays.asList(new Cell(5, 10), new Cell(2, 20));
        Atm atm = new Atm(cells);
        assertEquals(90, atm.getAmount());
        try {
            atm.withdraw(11);
        } catch (Exception e) {}
        assertEquals(90, atm.getAmount());
    }

    @Test
    public void shouldReduceOverallAmountAfterWithdraw() {
        List<Cell> cells = Arrays.asList(new Cell(1, 10), new Cell(2, 20));
        Atm atm = new Atm(cells);
        atm.withdraw(5);
        assertEquals(45, atm.getAmount());
    }

    @Test
    public void shouldRestoreInitialState() {
        List<Cell> cells = Arrays.asList(new Cell(5, 10), new Cell(2, 20));
        Atm atm = new Atm(cells);
        assertEquals(90, atm.getAmount());
        atm.withdraw(10);
        atm.withdraw(20);
        atm.withdraw(4);
        assertEquals(56, atm.getAmount());
        atm.restoreInitialState();
        assertEquals(90, atm.getAmount());


    }
}