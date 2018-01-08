package net.kuryshev;

public class Serializer {

    public String toJson(Object object) {
       return new ObjectSerializer(object).toJson();
    }
}
