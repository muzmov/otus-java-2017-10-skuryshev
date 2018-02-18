package net.kuryshev;

import net.kuryshev.entity.AddressDataSet;
import net.kuryshev.entity.PhoneDataSet;
import net.kuryshev.entity.UsersDataSet;
import net.kuryshev.service.DBService;
import net.kuryshev.service.DBServiceHibernateImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class Main {

    private UsersDataSet user;
    private AddressDataSet address;
    private PhoneDataSet phone1;
    private PhoneDataSet phone2;

    private DBService dbServiceHibernate;

    @Autowired
    public Main(DBService dbServiceHibernate) {
        this.dbServiceHibernate = dbServiceHibernate;
        prepareData();
        createDataManipulationTimer();
    }

    private void prepareData() {
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

    private void createDataManipulationTimer() {
        dbServiceHibernate.prepareTables();
        Timer timer = new Timer();
        timer.schedule(createDataManipulationTask(), 0, 1000);

    }

    private TimerTask createDataManipulationTask() {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    performDataManipulation();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void performDataManipulation() throws InterruptedException {
        dbServiceHibernate.save(user);

        UsersDataSet userLoaded = dbServiceHibernate.load(user.getId(), UsersDataSet.class);
        System.out.println(userLoaded);
        userLoaded = dbServiceHibernate.load(user.getId(), UsersDataSet.class);
        System.out.println(userLoaded);
        System.gc();
        Thread.sleep(1000);
        userLoaded = dbServiceHibernate.load(user.getId(), UsersDataSet.class);
        System.out.println(userLoaded);
    }
}
