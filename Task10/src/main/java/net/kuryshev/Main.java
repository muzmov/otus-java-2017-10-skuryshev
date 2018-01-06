package net.kuryshev;

import net.kuryshev.entity.AddressDataSet;
import net.kuryshev.entity.PhoneDataSet;
import net.kuryshev.entity.UsersDataSet;
import net.kuryshev.service.DBService;
import net.kuryshev.service.DBServiceHibernateImpl;
import net.kuryshev.service.DBServiceImpl;

import java.util.Arrays;
import java.util.HashSet;

public class Main {

    private static UsersDataSet user;
    private static AddressDataSet address;
    private static PhoneDataSet phone1;
    private static PhoneDataSet phone2;

    public static void main(String[] args) {
        DBService dbService = new DBServiceImpl();
        prepareData();

        dbService.prepareTables();
        dbService.save(user);

        UsersDataSet userLoaded = dbService.load(1, UsersDataSet.class);
        System.out.println(userLoaded);
        dbService.close();

        DBService dbServiceHibernate = new DBServiceHibernateImpl();
        dbServiceHibernate.prepareTables();
        dbServiceHibernate.save(user);

        userLoaded = dbServiceHibernate.load(user.getId(), UsersDataSet.class);
        System.out.println(userLoaded);
        dbServiceHibernate.close();
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
