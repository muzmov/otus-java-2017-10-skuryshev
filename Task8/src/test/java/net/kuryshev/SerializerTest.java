package net.kuryshev;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SerializerTest {
    private Gson gson;
    private Serializer serializer ;
    private SimpleObject simpleObject;
    private ComplexObject complexObject;

    @Before
    public void setUp() {
        serializer = new Serializer();
        gson = new Gson();
        simpleObject = new SimpleObject("ANY_STRING", 1);
        complexObject = new ComplexObject(simpleObject, "ANOTHER_STRING");
    }

    @Test
    public void shouldSerializeSimpleObject() {
        String json = serializer.toJson(simpleObject);

        SimpleObject simpleObjectDeserialized = gson.fromJson(json, SimpleObject.class);
        assertEquals(simpleObject, simpleObjectDeserialized);
    }

    @Test
    public void shouldSerializeComplexObject() {
        String json = serializer.toJson(complexObject);

        ComplexObject complexObjectDeserialized = gson.fromJson(json, ComplexObject.class);
        assertEquals(complexObject, complexObjectDeserialized);
    }

    @Test
    public void shouldSerializeArrayOfPrimitives() {
        int[] array = {1, 2, 3, 4};
        String json = serializer.toJson(array);

        int[] deserializedArray = gson.fromJson(json, int[].class);
        assertArrayEquals(deserializedArray, array);
    }

    @Test
    public void shouldSerializeArrayOfStrings() {
        String[] array = {"1", "2", "3", "4"};
        String json = serializer.toJson(array);

        String[] deserializedArray = gson.fromJson(json, String[].class);
        assertArrayEquals(deserializedArray, array);
    }

    @Test
    public void shouldSerializeArrayOfIntegers() {
        Integer[] array = {1, 2, 3, 4};
        String json = serializer.toJson(array);

        Integer[] deserializedArray = gson.fromJson(json, Integer[].class);
        assertArrayEquals(deserializedArray, array);
    }

    @Test
    public void shouldSerializeInteger() {
        Integer integer = new Integer(1);
        String json = serializer.toJson(integer);

        Integer deserializedInteger = gson.fromJson(json, Integer.class);
        assertEquals(deserializedInteger, integer);
    }

    @Test
    public void shouldSerializeCollectionOfObjects() {
        List<SimpleObject> simpleObjects = Arrays.asList(new SimpleObject("ANY_STRING", 1), new SimpleObject("ANOTHER_STRING", 2));
        String json = serializer.toJson(simpleObjects);

        Type listType = new TypeToken<ArrayList<SimpleObject>>(){}.getType();
        List<SimpleObject> simpleObjectsDeserialized = gson.fromJson(json, listType);
        assertEquals(simpleObjectsDeserialized, simpleObjects);
    }

    @Test
    public void shouldSerializeCollectionOfIntegers() {
        List<Integer> integers = Arrays.asList(1, 2, 3);
        String json = serializer.toJson(integers);

        Type listType = new TypeToken<ArrayList<Integer>>(){}.getType();
        List<Integer> integersDeserialized = gson.fromJson(json, listType);
        assertEquals(integersDeserialized, integers);
    }

}
