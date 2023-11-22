package com.mjuAppSW.joA.log;

import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnection;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactoryProd {

    private static interface Singleton {
        final ConnectionFactoryProd INSTANCE = new ConnectionFactoryProd();
    }

    private final DataSource dataSource;

    private ConnectionFactoryProd() {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "JJJProject!!!"); // or get properties from some configuration file

        GenericObjectPool<PoolableConnection> pool = new GenericObjectPool<PoolableConnection>();
        DriverManagerConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
                "jdbc:mysql://jproject.czwitpi2bfyx.ap-northeast-2.rds.amazonaws.com:3306/JProject", properties
        );
        new PoolableConnectionFactory(connectionFactory, pool, null, "SELECT 1", 3, false, false, Connection.TRANSACTION_READ_COMMITTED);

        this.dataSource = new PoolingDataSource(pool);
    }

    public static Connection getDatabaseConnection() throws SQLException {
        return Singleton.INSTANCE.dataSource.getConnection();
    }
}