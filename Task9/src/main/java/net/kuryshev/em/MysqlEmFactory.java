package net.kuryshev.em;

import net.kuryshev.exception.MyOrmException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlEmFactory implements EmFactory {

    public MysqlEmFactory() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new MyOrmException("Something is wrong with mysql driver", e);
        }
    }

    @Override
    public MyEntityManager createEntityManager(String connectionUrl) {
        try {
            Connection connection = DriverManager.getConnection(connectionUrl);
            return new MyEntityManager(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new MyOrmException("Could not create connection for url: " + connectionUrl, e);
        }
    }
}
