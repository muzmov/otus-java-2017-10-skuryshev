package net.kuryshev.app;

import net.kuryshev.db.*;
import net.kuryshev.db.cache.Cache;
import net.kuryshev.db.dataset.DataSet;
import net.kuryshev.db.service.DBService;
import net.kuryshev.db.service.DBServiceHibernateImpl;
import net.kuryshev.front.AddressContext;
import net.kuryshev.front.filter.AuthFilter;
import net.kuryshev.front.servlet.LoginServlet;
import net.kuryshev.front.servlet.StaticServlet;
import net.kuryshev.front.ws.CacheEndpoint;
import net.kuryshev.ms.MsgServer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.DispatcherType;
import javax.websocket.server.ServerContainer;
import java.util.EnumSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private final static int PORT = 8080;
    private final static String PUBLIC_HTML = "public_html";

    // mode, port, mshost, msport
    public static void main(String[] args) throws Exception {
        if (args.length < 2) throw new IllegalArgumentException("You should specify at least mode and port");
        Mode mode = Mode.valueOf(args[0]);
        int port = Integer.parseInt(args[1]);
        String msHost = "";
        int msPort = 0;
        if (args.length > 3) {
            msHost = args[2];
            msPort = Integer.parseInt(args[3]);
        }
        switch (mode) {
            case FRONT:
                startFront(port, msHost, msPort);
                break;
            case MS:
                startMs(port);
                break;
            case DB:
                startDb(port);
                break;
        }
    }

    private static void startDb(int port) {
        Cache<Long, DataSet> cache = new Cache<>();
        DBService dbService = new DBServiceHibernateImpl(cache);
        DataManipulator dm = new DataManipulator(dbService);
        dm.startManipulation();

    }

    private static void startMs(int port) throws Exception {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        startClient(executorService);
        MsgServer server = new MsgServer();
        server.start();
        executorService.shutdown();
    }

    private static void startClient(ScheduledExecutorService executorService) {
        executorService.schedule(() -> {
            try {
                ProcessRunner pr1 = new ProcessRunner();
                pr1.start("java -jar hw16.jar DB 8070 localhost 8080");
                ProcessRunner pr2 = new ProcessRunner();
                pr2.start("java -jar hw16.jar FRONT 8090 localhost 8080");
                ProcessRunner pr3 = new ProcessRunner();
                pr3.start("java -jar hw16.jar FRONT 8091 localhost 8080");
                Thread.sleep(5000);
                System.out.println(pr1.getOutput());
                System.out.println("--------------------------");
                System.out.println(pr2.getOutput());
                System.out.println("--------------------------");
                System.out.println(pr3.getOutput());
                System.out.println("--------------------------");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1, TimeUnit.SECONDS);
    }


    private static void startFront(int myPort, String msHost, int msPort) throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addFilter(AuthFilter.class, "/*", EnumSet.of(DispatcherType.INCLUDE, DispatcherType.REQUEST));
        context.addServlet(new ServletHolder(new LoginServlet()), "/login");
        context.addServlet(new ServletHolder(new StaticServlet()), "/*");
        XmlWebApplicationContext springContext = new XmlWebApplicationContext();
        springContext.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor() {
            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
                AddressContext addressContext = beanFactory.getBean(AddressContext.class);
                addressContext.setMsHost(msHost);
                addressContext.setMsPort(msPort);
                addressContext.setMyPort(myPort);
            }
        });
        springContext.setConfigLocation("classpath:SpringBeans.xml");
        context.addEventListener(new ContextLoaderListener(springContext));
        Server server = new Server(myPort);
        server.setHandler(new HandlerList(context));
        ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);
        wscontainer.addEndpoint(CacheEndpoint.class);

        server.start();
        server.join();
    }
}
