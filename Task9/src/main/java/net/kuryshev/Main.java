package net.kuryshev;

import net.kuryshev.em.MysqlEmFactory;
import net.kuryshev.em.MyEntityManager;
import net.kuryshev.entity.SimpleEntity;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        String connectionUrl = "jdbc:mysql://localhost:3306/orm?user=root&password=password&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
        MyEntityManager em = new MysqlEmFactory().createEntityManager(connectionUrl);

        SimpleEntity se = new SimpleEntity();
        se.setAge(1);
        se.setName("ANY_NAME");

        em.save(se);

        SimpleEntity seLoaded = em.load(1, SimpleEntity.class);
        System.out.println(seLoaded);
        em.close();
    }
}
