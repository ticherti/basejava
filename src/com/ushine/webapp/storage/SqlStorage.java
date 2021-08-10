package com.ushine.webapp.storage;

import com.ushine.webapp.exception.NotExistStorageException;
import com.ushine.webapp.model.ContactType;
import com.ushine.webapp.model.Resume;
import com.ushine.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    private final SqlHelper helper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        helper = new SqlHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void save(Resume r) {
        helper.TransactionalExecuteQuery(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }

            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
                for (Map.Entry<ContactType, String> entry : r.getContacts().entrySet()
                ) {
                    ps.setString(1, r.getUuid());
                    ps.setString(2, entry.getKey().name());
                    ps.setString(3, entry.getValue());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return helper.executeQuery(
                "  SELECT * FROM resume r " +
                        "LEFT JOIN contact c ON r.uuid = c.resume_uuid " +
                        "WHERE r.uuid =?",
                (ps) -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume r = new Resume(rs.getString("full_name"), uuid);
                    do {
                        String value = rs.getString("value");
                        ContactType type = ContactType.valueOf(rs.getString("type"));
                        r.addContact(type, value);
                    }
                    while (rs.next());
                    return r;
                });
    }

    @Override
    public void update(Resume r) {
        helper.TransactionalExecuteQuery(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(
                    "    UPDATE resume " +
                            "SET full_name =? " +
                            "WHERE uuid=?")) {
                String uuid = r.getUuid();
                ps.setString(1, r.getFullName());
                ps.setString(2, uuid);
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(uuid);
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "    UPDATE contact " +
                            "SET value =? " +
                            "WHERE resume_uuid=? AND type=?")) {
                for (Map.Entry<ContactType, String> entry : r.getContacts().entrySet()
                ) {
                    ps.setString(1, entry.getKey().name());
                    ps.setString(2, r.getUuid());
                    ps.setString(3, entry.getValue());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        helper.executeQuery("DELETE FROM resume WHERE uuid =?", (ps) -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return getAllSortedCombineByLeftJoin();
//        return getAllSortedCombineByCode();
    }

    public List<Resume> getAllSortedCombineByLeftJoin() {
        return helper.executeQuery(
                "  SELECT * FROM resume r " +
                        "LEFT JOIN contact c ON r.uuid = c.resume_uuid " +
                        "ORDER BY full_name, uuid;",
                (ps) -> {
                    List<Resume> resumes = new ArrayList<>();
                    ResultSet rs = ps.executeQuery();
                    String lastResumeUUID = "";
                    Resume r = null;
                    while (rs.next()) {
                        String uuid = rs.getString("uuid");
                        if (!uuid.equals(lastResumeUUID)) {
                            if (r != null) {
                                resumes.add(r);
                            }
                            r = new Resume(rs.getString("full_name"), uuid);
                            lastResumeUUID = uuid;
                        }
                        r.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                    }
                    resumes.add(r);
                    return resumes;
                });
    }

    public List<Resume> getAllSortedCombineByCode() {
        List<Resume> list = helper.executeQuery(
                "  SELECT * FROM resume r " +
                        "ORDER BY full_name, uuid;",
                (ps) -> {
                    List<Resume> resumes = new ArrayList<>();
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        resumes.add(new Resume(rs.getString("full_name"), rs.getString("uuid")));
                    }
                    return resumes;
                });
        Map<String, Map<ContactType, String>> resumeContacts = helper.executeQuery(
                "  SELECT * FROM contact c " +
                        "ORDER BY resume_uuid",
                (ps) -> {
                    Map<String, Map<ContactType, String>> contacts = new HashMap<>();
                    ResultSet rs = ps.executeQuery();
                    String lastUUID = null;
                    Map<ContactType, String> contactLines;
                    while (rs.next()) {
                        String uuid = rs.getString("resume_uuid");
                        if (!uuid.equals(lastUUID)) {
                            contacts.put(uuid, new HashMap<>());
                            lastUUID = uuid;
                        }
                        contactLines = contacts.get(uuid);
                        contactLines.put(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                    }
                    return contacts;
                });
        for (Resume r : list
        ) {
            Map<ContactType, String> contactPair = resumeContacts.get(r.getUuid());
            for (Map.Entry<ContactType, String> entry : contactPair.entrySet()
            ) {
                r.addContact(entry.getKey(), entry.getValue());
            }
        }

        return list;
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
        helper.executeQuery("DELETE FROM resume", (ps) -> {
            ps.execute();
            return null;
        });
    }
}
