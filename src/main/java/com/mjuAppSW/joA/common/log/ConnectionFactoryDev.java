package com.mjuAppSW.joA.common.log;

import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnection;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactoryDev {
    private static interface Singleton {
        final ConnectionFactoryDev INSTANCE = new ConnectionFactoryDev();
    }

    private final DataSource dataSource;

    private ConnectionFactoryDev() {
        Properties properties = new Properties();
        properties.setProperty("user", "dbmasteruser");
        properties.setProperty("password", "JBpb9G8FtC]^01Z2#?gclx8Vh}s}mt.g");

        GenericObjectPool<PoolableConnection> pool = new GenericObjectPool<PoolableConnection>();
        DriverManagerConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
                "jdbc:mysql://ls-302893828d35fc9b5e26d1c007f75e12825bff3b.c16t0ksf3vij.ap-northeast-2.rds.amazonaws.com:3306/jproject-dev-mysql?createDatabaseIfNotExist=true", properties
        );
        new PoolableConnectionFactory(connectionFactory, pool, null, "SELECT 1", 3, false, false, Connection.TRANSACTION_READ_COMMITTED);

        this.dataSource = new PoolingDataSource(pool);
    }

    public static Connection getDatabaseConnection() throws SQLException {
        return Singleton.INSTANCE.dataSource.getConnection();
    }
}
