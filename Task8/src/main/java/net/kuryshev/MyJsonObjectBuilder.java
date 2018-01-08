package net.kuryshev;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class MyJsonObjectBuilder {
    private static final Set<Class> primitives = new HashSet<>(Arrays.asList(
            Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class, Character.class));

    private Object object;
    private String jsonCache;

    public MyJsonObjectBuilder(Object object) {
        this.object = object;
    }

    public String toJson() {
        if (jsonCache == null) jsonCache = buildJson();
        return jsonCache;
    }

    private String buildJson() {
        String json = "";
        if (object == null) return "null";
        if (primitives.contains(object.getClass())) json += object.toString();
        else if (object.getClass() == String.class) json += "'" + object.toString() + "'";
        else if (object.getClass().isArray()) json += new MyJsonArrayBuilder(ArrayUtils.toObjects(object)).toJson();
        else if (object instanceof Collection) json += new MyJsonArrayBuilder((Collection) object).toJson();
        else {
            Field[] fields = object.getClass().getDeclaredFields();
            json += Stream.of(fields)
                    .map(this::fieldToJson)
                    .filter(Objects::nonNull)
                    .collect(joining(", ", "{", "}"));
        }
        return json;
    }

    private String fieldToJson(Field field) {
        if (Modifier.isTransient(field.getModifiers())) return null;
        if (Modifier.isStatic(field.getModifiers())) return null;
        field.setAccessible(true);
        Object fieldValue = null;
        try {
            fieldValue = field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        String json = field.getName() + "=";
        MyJsonObjectBuilder objectBuilder = new MyJsonObjectBuilder(fieldValue);
        json += objectBuilder.toJson();
        return json;
    }
}
