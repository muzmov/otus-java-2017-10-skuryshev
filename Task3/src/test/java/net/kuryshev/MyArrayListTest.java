package net.kuryshev;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class MyArrayListTest {

    private MyArrayList<Integer> sut;

    public Object[] testElems() {
        return new Object[] {
                new Object[] {3},
                new Object[] {3, 34},
                new Object[] {1, 3, 2, 4, 4, 1, 4, 12, 0, -10, 1, 3, 2, 4, 4, 1, 4, 12, 0, -10, 1, 3, 2, 4, 4, 1, 4, 12, 0, -10},
        };
    }

    @Test
    @Parameters(method = "testElems")
    public void shouldContainAllAddedElements(Integer[] elems) {
        sut = new MyArrayList<>();
        Collections.addAll(sut, elems);
        assertThat(sut, contains(elems));
    }

    @Test
    @Parameters(method = "testElems")
    public void shouldExpand(Integer[] elems) {
        sut = new MyArrayList<>(1);
        Collections.addAll(sut, elems);
        assertThat(sut, contains(elems));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnIncorrectAllocatedSize() {
        sut = new MyArrayList<>(0);
    }

    @Test
    @Parameters(method = "testElems")
    public void shouldContainAllElemsFromSrcAfterCopy(Integer[] elems) {
        MyArrayList<Integer> src = new MyArrayList<>();
        sut = new MyArrayList<>();
        for (Integer ignored : elems)
            sut.add(1);
        Collections.addAll(src, elems);
        Collections.copy(sut, src);

        assertThat(sut, contains(elems));
    }

    @Test
    @Parameters(method = "testElems")
    public void shouldBeSortedAsArrayList(Integer[] unsortedElems) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        sut = new MyArrayList<>();

        Collections.addAll(arrayList, unsortedElems);
        Collections.addAll(sut, unsortedElems);

        Collections.sort(arrayList, Comparator.naturalOrder());
        Collections.sort(sut, Comparator.naturalOrder());

        Object[] sortedElems = arrayList.toArray();

        assertThat(sut, contains(sortedElems));
    }
}