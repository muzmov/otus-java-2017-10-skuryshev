package net.kuryshev;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class MyJsonArrayBuilder {
    private List<Object> elems;
    private String jsonCache;

    public MyJsonArrayBuilder(Object[] array) {
        elems = Arrays.asList(array);
    }

    public MyJsonArrayBuilder(Collection collection) {
        elems = new ArrayList<>();
        elems.addAll(collection);
    }

    public String toJson() {
        if (jsonCache == null) jsonCache = buildJson();
        return jsonCache;
    }

    private String buildJson() {
        return elems.stream()
                .map(e -> new MyJsonObjectBuilder(e).toJson())
                .collect(joining(", ", "[", "]"));
    }
}
