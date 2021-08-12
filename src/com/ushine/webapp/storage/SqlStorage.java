package com.ushine.webapp.storage;

import com.ushine.webapp.exception.NotExistStorageException;
import com.ushine.webapp.model.ContactType;
import com.ushine.webapp.model.Resume;
import com.ushine.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {
    private final SqlHelper helper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        helper = new SqlHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void save(Resume r) {
        helper.<Void>TransactionalExecuteQuery(conn -> {
            helper.<Void>executePreparedStatement(conn, "INSERT INTO resume (uuid, full_name) VALUES (?,?)",
                    (ps) -> {
                        ps.setString(1, r.getUuid());
                        ps.setString(2, r.getFullName());
                        ps.execute();
                        return null;
                    });
            insertIntoContacts(r, conn);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return helper.TransactionalExecuteQuery(conn -> helper.executePreparedStatement(conn,
                "        SELECT * FROM resume r " +
                        "LEFT JOIN contact c ON r.uuid = c.resume_uuid " +
                        "WHERE r.uuid =?", (ps) -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume r = new Resume(rs.getString("full_name"), uuid);
                    do {
                        addContact(rs, r);
                    }
                    while (rs.next());
                    return r;
                })
        );
    }

    @Override
    public void update(Resume r) {
        helper.<Void>TransactionalExecuteQuery(conn -> {
            helper.<Void>executePreparedStatement(conn,
                    "    UPDATE resume " +
                            "SET full_name =? " +
                            "WHERE uuid=?", (ps) -> {
                        String uuid = r.getUuid();
                        ps.setString(1, r.getFullName());
                        ps.setString(2, uuid);
                        if (ps.executeUpdate() == 0) {
                            throw new NotExistStorageException(uuid);
                        }
                        return null;
                    });
            helper.executePreparedStatement(conn,
                    "    DELETE FROM contact " +
                            "WHERE resume_uuid=?", (ps) -> {
                        ps.setString(1, r.getUuid());
                        ps.execute();
                        return null;
                    }
            );
            insertIntoContacts(r, conn);
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        helper.<Void>executeQuery("DELETE FROM resume WHERE uuid =?", (ps) -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return helper.TransactionalExecuteQuery(
                conn -> {
                    try (PreparedStatement ps = conn.prepareStatement(
                            "    SELECT * FROM resume r " +
                                    "LEFT JOIN contact c ON r.uuid = c.resume_uuid " +
                                    "ORDER BY full_name, uuid;")) {
                        Map<String, Resume> map = new LinkedHashMap<>();
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            String uuid = rs.getString("uuid");
                            if (!map.containsKey(uuid)) {
                                map.put(uuid, new Resume(rs.getString("full_name"), uuid));
                            }
                            addContact(rs, map.get(uuid));
                        }
                        return new ArrayList<>(map.values());
                    }
                }
        );
    }


    @Override
    public int size() {
        return helper.executeQuery("SELECT COUNT(*) FROM resume r ", (ps) -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    @Override
    public void clear() {
        helper.<Void>executeQuery("DELETE FROM resume", (ps) -> {
            ps.execute();
            return null;
        });
    }

    private void addContact(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            r.addContact(ContactType.valueOf(rs.getString("type")), value);
        }
    }

    private void insertIntoContacts(Resume r, Connection conn) throws SQLException {
        helper.<Void>executePreparedStatement(conn, "INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)", (ps) -> {
                    for (Map.Entry<ContactType, String> entry : r.getContacts().entrySet()
                    ) {
                        ps.setString(1, r.getUuid());
                        ps.setString(2, entry.getKey().name());
                        ps.setString(3, entry.getValue());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                    return null;
                }
        );
    }
}
