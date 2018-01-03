package net.kuryshev;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class AtmDepartmentTest {

    private AtmDepartment atmDepartment;
    private AtmDepartment superDepartment;
    private Atm atm1;
    private Atm atm2;
    private Atm atm3;

    @Before
    public void setUp() {
        atm1 = new Atm(Arrays.asList(new Cell(1, 10), new Cell(2, 10)));
        atm2 = new Atm(Arrays.asList(new Cell(1, 5), new Cell(2, 5)));
        atm3 = new Atm(Arrays.asList(new Cell(1, 15), new Cell(2, 15)));
        atmDepartment = new AtmDepartment(Arrays.asList(atm1, atm2));
        superDepartment = new AtmDepartment(Arrays.asList(atmDepartment, atm3));
    }

    @Test
    public void shouldGetOverallAmount() {
        assertEquals(45, atmDepartment.getAmount());
    }

    @Test
    public void shouldRestoreInitialStates() {
        atm1.withdraw(10);
        atm2.withdraw(5);
        assertEquals(20, atm1.getAmount());
        assertEquals(10, atm2.getAmount());

        atmDepartment.restoreInitialState();

        assertEquals(30, atm1.getAmount());
        assertEquals(15, atm2.getAmount());
    }

    @Test
    public void couldContainOtherDepartments() {
        assertEquals(90, superDepartment.getAmount());
    }
}