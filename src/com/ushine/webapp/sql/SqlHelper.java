package com.ushine.webapp.sql;

import com.ushine.webapp.exception.ExistStorageException;
import com.ushine.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    public final ConnectionFactory connectionFactory;

    public SqlHelper(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public <T> T executeQuery(String query, Executor<T> executor) {
        try (Connection connection = connectionFactory.getConnection()) {
            return executePreparedStatement(connection, query, executor);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new ExistStorageException(e.getMessage());
            }
            throw new StorageException(e);
        }
    }

    public <T> T TransactionalExecuteQuery(SqlTransaction<T> executor) {
        try (Connection connection = connectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);
                T res = executor.execute(connection);
                connection.commit();
                return res;
            } catch (SQLException e) {
                connection.rollback();
                if (e.getSQLState().equals("23505")) {
                    throw new ExistStorageException(e.getMessage());
                }
                throw e;
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public <T> T executePreparedStatement(Connection conn, String sql, Executor<T> executor) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            return executor.execute(ps);
        }
    }

    @FunctionalInterface
    public interface Executor<T> {
        T execute(PreparedStatement ps) throws SQLException;
    }

    @FunctionalInterface
    public interface SqlTransaction<T> {
        T execute(Connection conn) throws SQLException;
    }
}
