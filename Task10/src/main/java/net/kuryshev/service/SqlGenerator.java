package net.kuryshev.service;

import net.kuryshev.entity.DataSet;
import net.kuryshev.entity.MyEntity;

import java.lang.reflect.Field;

public class SqlGenerator {

    public <T extends DataSet> String generateInsertSql(T entity) throws IllegalAccessException {
        Class entityClass = entity.getClass();
        String table = getTableName(entityClass);
        String sql = String.format("insert into %s (", table);

        for (Field field: entityClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (isSimpleField(field)) sql += field.getName() + ", ";
        }
        sql = sql.substring(0, sql.length() - 2);

        sql += ") values (";
        for (Field field: entityClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (isSimpleField(field)) sql += "'" + field.get(entity) + "', ";
        }
        sql = sql.substring(0, sql.length() - 2);
        sql += ");";
        return sql;
    }

    public <T extends DataSet> String generateSelectSql(long id, Class<T> clazz) throws IllegalAccessException {
        String table = getTableName(clazz);
        return String.format("select * from %s where id = %d;", table, id);
    }

    private <T extends DataSet> String getTableName(Class<T> entityClass) {
        MyEntity entityAnnotation = entityClass.getAnnotation(MyEntity.class);
        if (entityAnnotation == null) throw new RuntimeException("No @MyEntity annotation on class " + entityClass);
        return entityAnnotation.table();
    }

    private boolean isSimpleField(Field field) {
        return field.getType().isPrimitive() || field.getType() == String.class;
    }
}
