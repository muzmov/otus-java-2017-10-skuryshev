package net.kuryshev.db.service;

import net.kuryshev.db.cache.Cache;
import net.kuryshev.db.dataset.AddressDataSet;
import net.kuryshev.db.dataset.DataSet;
import net.kuryshev.db.dataset.PhoneDataSet;
import net.kuryshev.db.dataset.UsersDataSet;
import net.kuryshev.front.socket.ClientSocketMsgWorker;
import net.kuryshev.message.MsgRegisterDb;
import net.kuryshev.message.MsgToDb;
import net.kuryshev.message.MsgToFront;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class DBServiceHibernateImpl implements DBService, AutoCloseable {
    private SessionFactory sessionFactory;
    private final Configuration configuration;

    private final Cache<Long, DataSet> cache;

    private ClientSocketMsgWorker client;
    private ExecutorService executorService;

    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public DBServiceHibernateImpl(Cache<Long, DataSet> cache) {
        this.cache = cache;

        configuration = new Configuration();

        configuration.addAnnotatedClass(UsersDataSet.class);
        configuration.addAnnotatedClass(PhoneDataSet.class);
        configuration.addAnnotatedClass(AddressDataSet.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:~/test");
        configuration.setProperty("hibernate.connection.username", "sa");
        configuration.setProperty("hibernate.connection.password", "");
        configuration.setProperty("hibernate.show_sql", "false");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.connection.useSSL", "false");
        configuration.setProperty("hibernate.enable_lazy_load_no_trans", "true");
        startSocketWorker();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void startSocketWorker() {
        try {
            client = new ClientSocketMsgWorker(HOST, PORT);
            client.init();
            client.send(new MsgRegisterDb());

            executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> {
                try {
                    while (true) {
                        MsgToDb msg = (MsgToDb) client.take();
                        System.out.println("Message received: " + msg.toString());
                        MsgToFront message = new MsgToFront(msg.getFrontAddress());
                        message.setText(getCacheStatistics());
                        client.send(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void prepareTables() {
        close();
        sessionFactory = createSessionFactory(configuration);
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends DataSet> T load(long id, Class<T> clazz) {
        DataSet cachedEntity = cache.get(id);
        if (cachedEntity != null) return (T) cachedEntity;
        return runInSession(session -> {
            T entity = session.load(clazz, id);
            if (entity != null) cache.put(id, entity);
            return entity;
        });
    }

    @Override
    public <T extends DataSet> void save(T entity) {
        runInSession(session -> session.save(entity));
        if (entity.getId() != 0) cache.put(entity.getId(), entity);
    }

    @Override
    public void close() {
        if (sessionFactory != null) sessionFactory.close();
    }

    private <R> R runInSession(Function<Session, R> function) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        }
    }

    @Override
    public void printCacheStatistics() {
        cache.printStatistics();
    }

    @Override
    public String getCacheStatistics() {
       return cache.getStatistics();
    }

}
