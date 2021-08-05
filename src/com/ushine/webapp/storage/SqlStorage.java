package com.ushine.webapp.storage;

import com.ushine.webapp.exception.NotExistStorageException;
import com.ushine.webapp.exception.StorageException;
import com.ushine.webapp.model.Resume;
import com.ushine.webapp.sql.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    private final SqlHelper helper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        helper = new SqlHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void save(Resume r) {
        helper.doIt("INSERT INTO resume (uuid, full_name) VALUES (?,?)", (ps) -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.execute();
//            System.out.println(ps.getResultSet());
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return (Resume) helper.doIt("SELECT * FROM resume r WHERE r.uuid =?", (ps) -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(rs.getString("full_name"), uuid);
        });
    }

    @Override
    public void update(Resume r) {
        helper.doIt("UPDATE resume SET full_name =? WHERE uuid=?", (ps) -> {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(r.getUuid());
            }
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        helper.doIt("DELETE FROM resume WHERE uuid =?", (ps) -> {
            ps.setString(1, uuid);
            ps.execute();
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return (List<Resume>) helper.doIt("SELECT * FROM resume ORDER BY full_name", (ps) -> {
            List<Resume> list = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Resume(rs.getString("full_name"), rs.getString("uuid").trim()));
            }
            return list;
        });
    }

    @Override
    public int size() {
        return (int) helper.doIt("SELECT count(*) FROM resume r ", (ps) -> {
            ResultSet rs = ps.executeQuery();
            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            return count;
        });
    }

    @Override
    public void clear() {
        helper.doIt("DELETE FROM resume", (ps) -> {
            ps.execute();
            return null;
        });
    }

    static class SqlHelper<T> {
        public final ConnectionFactory connectionFactory;

        public SqlHelper(String dbUrl, String dbUser, String dbPassword) {
            connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        }

        private T doIt(String query, CodeBlock<T> code) {
            try (Connection connection = connectionFactory.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query)) {
                return code.execute(ps);
            } catch (SQLException e) {
                throw new StorageException(e);
            }
        }
    }

    @FunctionalInterface
    private interface CodeBlock<T> {
        T execute(PreparedStatement ps) throws SQLException;
    }
}
