package net.kuryshev.service;

import net.kuryshev.cache.Cache;
import net.kuryshev.entity.AddressDataSet;
import net.kuryshev.entity.DataSet;
import net.kuryshev.entity.PhoneDataSet;
import net.kuryshev.entity.UsersDataSet;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.function.Function;

public class DBServiceHibernateImpl implements DBService, AutoCloseable {
    private SessionFactory sessionFactory;
    private final Configuration configuration;

    private final Cache<Long, DataSet> cache;

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
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.connection.useSSL", "false");
        configuration.setProperty("hibernate.enable_lazy_load_no_trans", "true");
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
