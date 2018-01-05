package net.kuryshev;

import net.kuryshev.exception.CouldNotBeWithdrawnException;
import net.kuryshev.strategy.RecursiveStrategy;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class RecursiveStrategyTest {


    @Test
    public void recursiveStrategyTest() {
        List<Cell> cells = Arrays.asList(new Cell(1, 10), new Cell(2, 20));
        Atm atm = new Atm(cells);
        atm.setStrategy(new RecursiveStrategy());
        atm.withdraw(5);
        assertEquals(45, atm.getAmount());
    }

    @Test
    public void recursiveVsGreedyTest() {
        List<Cell> cells = Arrays.asList(new Cell(3, 10), new Cell(5, 20));
        Atm atm = new Atm(cells);
        try {
            atm.withdraw(6);
            fail();
        } catch (CouldNotBeWithdrawnException e) { }
        atm.setStrategy(new RecursiveStrategy());
        atm.withdraw(6);
        assertEquals(124, atm.getAmount());
    }
}