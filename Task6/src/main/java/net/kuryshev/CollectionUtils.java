package net.kuryshev;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtils {

    public static List<Cell> deepCopy(List<Cell> cells) {
        if (cells == null) return null;
        List<Cell> copy = new ArrayList<>();
        for (Cell cell : cells) copy.add(cell.clone());
        return copy;
    }

}
