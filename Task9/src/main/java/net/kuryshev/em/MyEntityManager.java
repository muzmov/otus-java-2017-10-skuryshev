package net.kuryshev.em;

import net.kuryshev.entity.MyEntity;
import net.kuryshev.exception.MyOrmException;
import net.kuryshev.entity.DataSet;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyEntityManager {
    private Connection connection;

    public MyEntityManager(Connection connection) {
        this.connection = connection;
    }

    public <T extends DataSet> void save(T entity) throws IllegalAccessException {
        String sql = generateInsertSql(entity);

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new MyOrmException("SQL Exception", e);
        }
    }

    private <T extends DataSet> String generateInsertSql(T entity) throws IllegalAccessException {
        Class entityClass = entity.getClass();
        String table = getTableName(entityClass);
        String sql = String.format("insert into %s (", table);

        for (Field field: entityClass.getDeclaredFields()) {
            field.setAccessible(true);
            sql += field.getName() + ", ";
        }
        sql = sql.substring(0, sql.length() - 2);
        sql += ") values (";
        for (Field field: entityClass.getDeclaredFields()) {
            field.setAccessible(true);
            sql += "'" + field.get(entity) + "', ";
        }
        sql = sql.substring(0, sql.length() - 2);
        sql += ");";
        return sql;
    }

    private <T extends DataSet> String getTableName(Class<T> entityClass) {
        MyEntity entityAnnotation = entityClass.getAnnotation(MyEntity.class);
        if (entityAnnotation == null) throw new MyOrmException("No @MyEntity annotation on class " + entityClass);
        return entityAnnotation.table();
    }

    public <T extends DataSet> T load(long id, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        String sql = generateSelectSql(id, clazz);
        Object object = clazz.newInstance();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                fillObjectFields(clazz, object, rs);
            }
            else throw new MyOrmException("No result found");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new MyOrmException(e);
        }
        return (T) object;
    }

    private <T extends DataSet> String generateSelectSql(long id, Class<T> clazz) throws IllegalAccessException {
        String table = getTableName(clazz);
        return String.format("select * from %s where id = %d;", table, id);
    }

    private <T extends DataSet> void fillObjectFields(Class<T> clazz, Object object, ResultSet rs) throws IllegalAccessException, SQLException, NoSuchFieldException {
        for (Field field: clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Class type = field.getType();
            if (type == Byte.TYPE || type == Byte.class) field.set(object, rs.getByte(field.getName()));
            if (type == Short.TYPE || type == Short.class) field.set(object, rs.getShort(field.getName()));
            if (type == Integer.TYPE || type == Integer.class) field.set(object, rs.getInt(field.getName()));
            if (type == Long.TYPE || type == Long.class) field.set(object, rs.getLong(field.getName()));
            if (type == Boolean.TYPE || type == Boolean.class) field.set(object, rs.getBoolean(field.getName()));
            if (type == Float.TYPE || type == Float.class) field.set(object, rs.getFloat(field.getName()));
            if (type == Double.TYPE || type == Double.class) field.set(object, rs.getDouble(field.getName()));
            if (type == String.class) field.set(object, rs.getString(field.getName()));
        }
        Class superClass = DataSet.class;
        Field id = superClass.getDeclaredField("id");
        id.setAccessible(true);
        id.set(object, rs.getLong("id"));
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
