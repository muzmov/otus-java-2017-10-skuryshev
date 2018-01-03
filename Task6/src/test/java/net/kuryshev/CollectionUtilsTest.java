package net.kuryshev;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CollectionUtilsTest {

    @Test
    public void deepCopyTest() {
        List<Cell> cells = Arrays.asList(new Cell(1, 1), new Cell(2, 2));
        List<Cell> copy = CollectionUtils.deepCopy(cells);
        assertEquals(cells, copy);
        shouldBeIndependent(cells, copy);
    }

    private void shouldBeIndependent(List<Cell> cells, List<Cell> copy) {
        copy.get(0).withdraw(1);
        assertEquals(0, copy.get(0).getCount());
        assertEquals(1, cells.get(0).getCount());
    }
}