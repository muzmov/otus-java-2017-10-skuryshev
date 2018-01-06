package net.kuryshev.service;

import net.kuryshev.entity.DataSet;
import net.kuryshev.executor.Executor;

import java.lang.reflect.Field;
import java.sql.*;

public class DBServiceImpl implements DBService {
    private static final String CREATE_TABLE_SQL = "create table SIMPLE_ENTITY (id bigint(20) NOT NULL auto_increment primary key, name varchar(255), age int(3));";
    private static final String DROP_TABLE_SQL = "drop table SIMPLE_ENTITY;";
    private Executor exec;
    private SqlGenerator sqlGenerator;

    public DBServiceImpl() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/orm?user=root&password=password&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC");
            exec = new Executor(connection);
            sqlGenerator = new SqlGenerator();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void prepareTables() {
        try {
            exec.execUpdate(DROP_TABLE_SQL);
            exec.execUpdate(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends DataSet> void save(T entity) {
        try {
            String sql = sqlGenerator.generateInsertSql(entity);
            exec.execUpdate(sql);
        }
        catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> clazz) {
        try  {
            Object object = clazz.newInstance();
            String sql = sqlGenerator.generateSelectSql(id, clazz);
            exec.execQuery(sql, rs -> {
                if (rs.next()) fillObjectFields(clazz, object, rs);
                else throw new RuntimeException("No result found");
            });
            return (T) object;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends DataSet> void fillObjectFields(Class<T> clazz, Object object, ResultSet rs) {
        try {
            for (Field field : clazz.getDeclaredFields()) {
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
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        exec.closeConnection();
    }
}
