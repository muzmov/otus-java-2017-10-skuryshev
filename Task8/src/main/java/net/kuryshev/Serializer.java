package net.kuryshev;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

public class Serializer {

    public String toJson(Object object) {
        Class type = object.getClass();
        if (type.isArray() || object instanceof Collection) {
            JsonArrayBuilder jsonArrayBuilder = createJsonArrayBuilder(object);
            return jsonArrayBuilder.build().toString();
        } else if (object instanceof Number || object instanceof Character || object instanceof Boolean) {
            return object.toString();
        } else if (object instanceof String) {
            return (String) object;
        }
        JsonObjectBuilder jsonBuilder = createJsonObjectBuilder(object);
        return jsonBuilder.build().toString();
    }

    private JsonObjectBuilder createJsonObjectBuilder(Object object) {
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            addFieldToBuilder(jsonBuilder, field, object);
        }
        return jsonBuilder;
    }

    private JsonArrayBuilder createJsonArrayBuilder(Object array) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        if (array instanceof Collection) array = ((Collection) array).toArray();
        if (array instanceof byte[]) for (byte b : (byte[]) array) jsonArrayBuilder.add(b);
        else if (array instanceof short[]) for (short b : (short[]) array) jsonArrayBuilder.add(b);
        else if (array instanceof int[]) for (int b : (int[]) array) jsonArrayBuilder.add(b);
        else if (array instanceof long[]) for (long b : (long[]) array) jsonArrayBuilder.add(b);
        else if (array instanceof char[]) for (char b : (char[]) array) jsonArrayBuilder.add(b);
        else if (array instanceof boolean[]) for (boolean b : (boolean[]) array) jsonArrayBuilder.add(b);
        else if (array instanceof float[]) for (float b : (float[]) array) jsonArrayBuilder.add(b);
        else if (array instanceof double[]) for (double b : (double[]) array) jsonArrayBuilder.add(b);
        else if (array instanceof Byte[]) for (Byte b : (Byte[]) array) {
            if (b == null) jsonArrayBuilder.addNull();
            else jsonArrayBuilder.add(b);
        }
        else if (array instanceof Short[]) for (Short b : (Short[]) array) {
            if (b == null) jsonArrayBuilder.addNull();
            else jsonArrayBuilder.add(b);
        }
        else if (array instanceof Integer[]) for (Integer b : (Integer[]) array) {
            if (b == null) jsonArrayBuilder.addNull();
            else jsonArrayBuilder.add(b);
        }
        else if (array instanceof Long[]) for (Long b : (Long[]) array) {
            if (b == null) jsonArrayBuilder.addNull();
            else jsonArrayBuilder.add(b);
        }
        else if (array instanceof Float[]) for (Float b : (Float[]) array) {
            if (b == null) jsonArrayBuilder.addNull();
            else jsonArrayBuilder.add(b);
        }
        else if (array instanceof Double[]) for (Double b : (Double[]) array) {
            if (b == null) jsonArrayBuilder.addNull();
            else jsonArrayBuilder.add(b);
        }
        else if (array instanceof Character[]) for (Character b : (Character[]) array) {
            if (b == null) jsonArrayBuilder.addNull();
            else jsonArrayBuilder.add(b);
        }
        else if (array instanceof Boolean[]) for (Boolean b : (Boolean[]) array) {
            if (b == null) jsonArrayBuilder.addNull();
            else jsonArrayBuilder.add(b);
        }
        else if (array instanceof String[]) for (String b : (String[]) array) jsonArrayBuilder.add(b);
        else for (Object o : (Object[]) array) {
                JsonObjectBuilder jsonObjectBuilder = createJsonObjectBuilder(o);
                jsonArrayBuilder.add(jsonObjectBuilder);
            }
        return jsonArrayBuilder;
    }

    private void addFieldToBuilder(JsonObjectBuilder jsonBuilder, Field field, Object object) {
        field.setAccessible(true);
        if (Modifier.isTransient(field.getModifiers())) return;
        if (Modifier.isStatic(field.getModifiers())) return;
        String fieldName = field.getName();
        Object fieldValue = null;
        try {
            fieldValue = field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (fieldValue == null) {
            jsonBuilder.addNull(fieldName);
            return;
        }
        Class type = field.getType();
        if (type.isArray() || fieldValue instanceof Collection) {
            JsonArrayBuilder jsonArrayBuilder = createJsonArrayBuilder(fieldValue);
            jsonBuilder.add(fieldName, jsonArrayBuilder);
        } else if (type == Long.TYPE || type == Long.class) {
            jsonBuilder.add(fieldName, (Long) fieldValue);
        } else if (type == Integer.TYPE || type == Integer.class) {
            jsonBuilder.add(fieldName, (Integer) fieldValue);
        } else if (type == Character.TYPE || type == Character.class) {
            jsonBuilder.add(fieldName, (Character) fieldValue);
        } else if (type == Byte.TYPE || type == Byte.class) {
            jsonBuilder.add(fieldName, (Byte) fieldValue);
        } else if (type == Boolean.TYPE || type == Boolean.class) {
            jsonBuilder.add(fieldName, (Boolean) fieldValue);
        } else if (type == Short.TYPE || type == Short.class) {
            jsonBuilder.add(fieldName, (Short) fieldValue);
        } else if (type == Float.TYPE || type == Float.class) {
            jsonBuilder.add(fieldName, (Float) fieldValue);
        } else if (type == Double.TYPE || type == Double.class) {
            jsonBuilder.add(fieldName, (Double) fieldValue);
        } else if (type == String.class) {
            jsonBuilder.add(fieldName, (String) fieldValue);
        } else {
            JsonObjectBuilder jsonObjectBuilder = createJsonObjectBuilder(fieldValue);
            jsonBuilder.add(fieldName, jsonObjectBuilder);
        }
    }
}
