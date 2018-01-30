package net.kuryshev;

import net.kuryshev.entity.AddressDataSet;
import net.kuryshev.entity.PhoneDataSet;
import net.kuryshev.entity.UsersDataSet;
import net.kuryshev.service.DBService;
import net.kuryshev.service.DBServiceHibernateImpl;
import org.h2.tools.Server;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;

public class Main {

    private static UsersDataSet user;
    private static AddressDataSet address;
    private static PhoneDataSet phone1;
    private static PhoneDataSet phone2;

    public static void main(String[] args) throws InterruptedException {
       // Server.createWebServer("-web","-webAllowOthers","-webPort","8082").start();
        prepareData();

        DBService dbServiceHibernate = new DBServiceHibernateImpl();
        dbServiceHibernate.prepareTables();
        dbServiceHibernate.save(user);

        UsersDataSet userLoaded = dbServiceHibernate.load(user.getId(), UsersDataSet.class);
        System.out.println(userLoaded);
        userLoaded = dbServiceHibernate.load(user.getId(), UsersDataSet.class);
        System.out.println(userLoaded);
        System.gc();
        Thread.sleep(1000);
        userLoaded = dbServiceHibernate.load(user.getId(), UsersDataSet.class);
        System.out.println(userLoaded);
        dbServiceHibernate.close();
        dbServiceHibernate.printCacheStatistics();
    }

    private static void prepareData() {
        user = new UsersDataSet();
        user.setAge(1);
        user.setName("ANY_NAME");
        address = new AddressDataSet();
        address.setAddress("ANY_ADDRESS");
        phone1 = new PhoneDataSet();
        phone1.setNumber("12345");
        phone1.setUser(user);
        phone2 = new PhoneDataSet();
        phone2.setNumber("54321");
        phone2.setUser(user);
        user.setAddress(address);
        user.setPhones(new HashSet<>(Arrays.asList(phone1, phone2)));
    }
}
