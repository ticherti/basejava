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

    public <T> T executeQuery(String query, CodeBlock<T> code) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            return code.execute(ps);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")){
                throw new ExistStorageException(e.getMessage());
            }
            throw new StorageException(e);
        }
    }

    @FunctionalInterface
    public interface CodeBlock<T> {
        T execute(PreparedStatement ps) throws SQLException;
    }
}
