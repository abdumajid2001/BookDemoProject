package connection;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;

import java.sql.SQLException;

public class DBConnection {

    private static DBConnection instance;

    private final OracleDataSource oracleDataSource;

    private DBConnection(ConnectionProperties properties) throws SQLException {
        oracleDataSource = new OracleDataSource();
        oracleDataSource.setURL(properties.getUrl());
        oracleDataSource.setUser(properties.getUser());
        oracleDataSource.setPassword(properties.getPassword());
    }

    public static synchronized void Init(ConnectionProperties properties) throws SQLException {
        if (instance == null) {
            instance = new DBConnection(properties);
        }
    }

    private static void checkInitialization() {
        if (instance == null) throw new RuntimeException("Database connection is not initialized");
    }

    public static OracleConnection getSingletonConnection() throws SQLException {
        checkInitialization();
        return (OracleConnection) instance.oracleDataSource.getConnection();
    }

}
