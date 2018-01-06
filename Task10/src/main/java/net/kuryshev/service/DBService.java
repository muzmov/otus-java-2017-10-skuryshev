package net.kuryshev.service;

import net.kuryshev.entity.DataSet;

public interface DBService {

    void prepareTables();

    <T extends DataSet> T load(long id, Class<T> clazz);

    <T extends DataSet> void save(T entity);

    void close();
}
