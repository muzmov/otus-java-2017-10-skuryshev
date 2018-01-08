package net.kuryshev;

public class ArrayUtils {
    public static Object[] toObjects(Object array) {
        Object[] result;
        if (!array.getClass().isArray()) throw new IllegalArgumentException("Argument should be array");

        if (array instanceof byte[]) {
            byte[] castedArray = (byte[]) array;
            result = new Byte[castedArray.length];
            for (int i = 0; i < castedArray.length; i++) result[i] = castedArray[i];
        }
        else if (array instanceof short[]) {
            short[] castedArray = (short[]) array;
            result = new Short[castedArray.length];
            for (int i = 0; i < castedArray.length; i++) result[i] = castedArray[i];
        }
        else if (array instanceof int[]) {
            int[] castedArray = (int[]) array;
            result = new Integer[castedArray.length];
            for (int i = 0; i < castedArray.length; i++) result[i] = castedArray[i];
        }
        else if (array instanceof long[]) {
            long[] castedArray = (long[]) array;
            result = new Long[castedArray.length];
            for (int i = 0; i < castedArray.length; i++) result[i] = castedArray[i];
        }
        else if (array instanceof float[]) {
            float[] castedArray = (float[]) array;
            result = new Float[castedArray.length];
            for (int i = 0; i < castedArray.length; i++) result[i] = castedArray[i];
        }
        else if (array instanceof double[]) {
            double[] castedArray = (double[]) array;
            result = new Double[castedArray.length];
            for (int i = 0; i < castedArray.length; i++) result[i] = castedArray[i];
        }
        else if (array instanceof char[]) {
            char[] castedArray = (char[]) array;
            result = new Character[castedArray.length];
            for (int i = 0; i < castedArray.length; i++) result[i] = castedArray[i];
        }
        else if (array instanceof boolean[]) {
            boolean[] castedArray = (boolean[]) array;
            result = new Boolean[castedArray.length];
            for (int i = 0; i < castedArray.length; i++) result[i] = castedArray[i];
        }
        else return (Object []) array;
        return result;
    }
}
