package net.kuryshev;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class SorterTest {

    public Object[] params() {
        return $(
                $(new int[]{3, 2, 1}, 2),
                $(new int[] {2, 4, 6, 3, 21, 6, 45, 3, 1}, 4),
                $(new int[] {2, 4, 6, 3, 21, 6, 45, 3, 1, 21, 6, 45, 3, 1, 21, 6, 45, 3, 1, 21, 6, 45, 3, 1}, 8)
        );
    }

    @Test
    @Parameters(method = "params")
    public void shouldSort(int[] array, int numThreads) {
        int[] expected = Arrays.copyOf(array, array.length);
        Arrays.sort(expected);

        new Sorter(numThreads).sort(array);

        assertArrayEquals(expected, array);
    }
}