package com.ushine.webapp.storage;

import com.ushine.webapp.exception.NotExistStorageException;
import com.ushine.webapp.model.*;
import com.ushine.webapp.sql.SqlHelper;
import com.ushine.webapp.util.JsonParser;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {
    private final SqlHelper helper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        helper = new SqlHelper(dbUrl, dbUser, dbPassword);
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e) {
//            TODO check the type of exception
            throw new IllegalStateException("Need to add postgres JDBC driver");
        }
    }

    @Override
    public void save(Resume r) {
        helper.TransactionalExecuteQuery(conn -> {
            helper.executePreparedStatement(conn, "INSERT INTO resume (uuid, full_name) VALUES (?,?)",
                    (ps) -> {
                        ps.setString(1, r.getUuid());
                        ps.setString(2, r.getFullName());
                        ps.execute();
                        return null;
                    });
            insertContacts(r, conn);
            insertSections(r, conn);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return helper.TransactionalExecuteQuery(conn -> {
                    Resume resume = helper.executePreparedStatement(conn,
                            "    SELECT * FROM resume r " +
                                    "LEFT JOIN contact t ON r.uuid = t.resume_uuid " +
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
                            });
                    helper.executePreparedStatement(conn,
                            "    SELECT * FROM section" +
                                    " WHERE resume_uuid =?", (ps) -> {
                                ps.setString(1, uuid);
                                ResultSet rs = ps.executeQuery();
                                while (rs.next()) {
                                    addSection(rs, resume);
                                }
                                return null;
                            });
                    return resume;
                }
        );
    }

    @Override
    public void update(Resume r) {
        helper.TransactionalExecuteQuery(conn -> {
            helper.executePreparedStatement(conn,
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
            deleteContacts(r, conn);
            deleteSections(r, conn);
            insertContacts(r, conn);
            insertSections(r, conn);
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
        return helper.TransactionalExecuteQuery(conn -> {
            Map<String, Resume> resumes = helper.executePreparedStatement(conn,
                    "    SELECT * FROM resume " +
                            "ORDER BY full_name, uuid;",
                    (ps) -> {
                        Map<String, Resume> map = new LinkedHashMap<>();
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            map.put(rs.getString("uuid"), new Resume(rs.getString("full_name"), rs.getString("uuid")));
                        }
                        return map;
                    });
            helper.executePreparedStatement(conn,
                    "    SELECT * FROM contact ",
                    (ps) -> {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            addContact(rs, resumes.get(rs.getString("resume_uuid")));
                        }
                        return null;
                    });
            helper.executePreparedStatement(conn,
                    "    SELECT * FROM section",
                    (ps) -> {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            addSection(rs, resumes.get(rs.getString("resume_uuid")));
                        }
                        return null;
                    });
            return new ArrayList<>(resumes.values());
        });
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

    private void addContact(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            r.addContact(ContactType.valueOf(rs.getString("type")), value);
        }
    }

    private void addSection(ResultSet rs, Resume resume) throws SQLException {
        String type = rs.getString("type");
        SectionType st = SectionType.valueOf(type);
        switch (st) {
            case PERSONAL:
            case OBJECTIVE:
                resume.addSection(st, JsonParser.read(rs.getString("value"), TextSection.class));
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                resume.addSection(st, JsonParser.read(rs.getString("value"), ListSection.class));
                break;
        }
    }

    private void deleteContacts(Resume r, Connection conn) throws SQLException {
        helper.executePreparedStatement(conn,
                "    DELETE FROM contact " +
                        "WHERE resume_uuid=?", (ps) -> {
                    ps.setString(1, r.getUuid());
                    ps.execute();
                    return null;
                }
        );
    }

    private void deleteSections(Resume r, Connection conn) throws SQLException {
        helper.executePreparedStatement(conn,
                "    DELETE FROM section " +
                        "WHERE resume_uuid=?", (ps) -> {
                    ps.setString(1, r.getUuid());
                    ps.execute();
                    return null;
                }
        );
    }

    private void insertContacts(Resume r, Connection conn) throws SQLException {
        helper.executePreparedStatement(conn, "INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)", (ps) -> {
                    for (Map.Entry<ContactType, String> entry : r.getContacts().entrySet()) {
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

    private void insertSections(Resume r, Connection conn) throws SQLException {
        helper.executePreparedStatement(conn, "INSERT INTO section (resume_uuid, type, value) VALUES (?,?,?)", (ps) -> {
                    for (Map.Entry<SectionType, AbstractSection> sectionEntry : r.getSections().entrySet()) {
                        ps.setString(1, r.getUuid());
                        ps.setString(2, sectionEntry.getKey().name());
                        ps.setString(3, JsonParser.write(sectionEntry.getValue()));
                        ps.addBatch();
                    }
                    ps.executeBatch();
                    return null;
                }
        );
    }
}
