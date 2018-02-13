package net.kuryshev;

import net.kuryshev.entity.AddressDataSet;
import net.kuryshev.entity.PhoneDataSet;
import net.kuryshev.entity.UsersDataSet;
import net.kuryshev.filter.AuthFilter;
import net.kuryshev.service.DBService;
import net.kuryshev.service.DBServiceHibernateImpl;
import net.kuryshev.servlet.CacheServlet;
import net.kuryshev.servlet.LoginServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;

public class Main {

    private static UsersDataSet user;
    private static AddressDataSet address;
    private static PhoneDataSet phone1;
    private static PhoneDataSet phone2;

    private final static int PORT = 8090;
    private final static String PUBLIC_HTML = "public_html";
    private static DBService dbServiceHibernate = new DBServiceHibernateImpl();

    public static void main(String[] args) throws Exception {
       // Server.createWebServer("-web","-webAllowOthers","-webPort","8082").start();
        Server server = startJetty();
        prepareData();
        performDataManipulation();
        server.join();
    }

    private static Server startJetty() throws Exception {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addFilter(AuthFilter.class, "/cache", EnumSet.of(DispatcherType.INCLUDE, DispatcherType.REQUEST));
        context.addServlet(new ServletHolder(new CacheServlet(dbServiceHibernate)), "/cache");
        context.addServlet(new ServletHolder(new LoginServlet()), "/login");

        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
        return server;
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

    private static void performDataManipulation() throws InterruptedException {
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
}
