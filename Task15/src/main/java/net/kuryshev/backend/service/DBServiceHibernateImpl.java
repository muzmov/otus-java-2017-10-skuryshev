package net.kuryshev.backend.service;

import net.kuryshev.app.DBService;
import net.kuryshev.app.MessageSystemContext;
import net.kuryshev.backend.cache.Cache;
import net.kuryshev.backend.entity.AddressDataSet;
import net.kuryshev.backend.entity.DataSet;
import net.kuryshev.backend.entity.PhoneDataSet;
import net.kuryshev.backend.entity.UsersDataSet;
import net.kuryshev.messageSystem.Address;
import net.kuryshev.messageSystem.MessageSystem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class DBServiceHibernateImpl implements DBService, AutoCloseable {
    private SessionFactory sessionFactory;
    private final Configuration configuration;

    private final Cache<Long, DataSet> cache;
    private MessageSystemContext msContext;
    private Address dbAddress;

    @Autowired
    public DBServiceHibernateImpl(Cache<Long, DataSet> cache, MessageSystemContext msContext) {
        this.cache = cache;
        this.msContext = msContext;
        this.dbAddress = new Address("DB");
        this.msContext = msContext;
        msContext.addDbAddressee(this);

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

    @Override
    public Address getAddress() {
        return dbAddress;
    }

    @Override
    public MessageSystem getMS() {
        return msContext.getMs();
    }
}
