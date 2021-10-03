package com.ushine.webapp.storage;

import com.ushine.webapp.exception.NotExistStorageException;
import com.ushine.webapp.model.*;
import com.ushine.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class SqlStorage implements Storage {
    private final SqlHelper helper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        helper = new SqlHelper(dbUrl, dbUser, dbPassword);
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
                    addSection(SectionType.PERSONAL, uuid, conn, resume);
                    addSection(SectionType.OBJECTIVE, uuid, conn, resume);
                    addSection(SectionType.ACHIEVEMENT, uuid, conn, resume);
                    addSection(SectionType.QUALIFICATIONS, uuid, conn, resume);
                    return resume;
                }
        );
    }

    // Also check if it is not safe to use parameter is sql line?
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
            helper.executePreparedStatement(conn,
                    "    DELETE FROM contact " +
                            "WHERE resume_uuid=?", (ps) -> {
                        ps.setString(1, r.getUuid());
                        ps.execute();
                        return null;
                    }
            );
            executePreparedStatementDeleteBySectionType(SectionType.PERSONAL, r, conn);
            executePreparedStatementDeleteBySectionType(SectionType.OBJECTIVE, r, conn);
            executePreparedStatementDeleteBySectionType(SectionType.ACHIEVEMENT, r, conn);
            executePreparedStatementDeleteBySectionType(SectionType.QUALIFICATIONS, r, conn);

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
        getAllSortedCombineByCode();
        return helper.TransactionalExecuteQuery(conn -> {
                    Map<String, Resume> resumes = helper.executePreparedStatement(conn,
                            "    SELECT * FROM resume r " +
                                    "LEFT JOIN contact c ON r.uuid = c.resume_uuid " +
                                    "ORDER BY full_name, uuid;",
                            ps -> {
                                Map<String, Resume> map = new LinkedHashMap<>();
                                ResultSet rs = ps.executeQuery();
                                while (rs.next()) {
                                    String uuid = rs.getString("uuid");
                                    if (!map.containsKey(uuid)) {
                                        map.put(uuid, new Resume(rs.getString("full_name"), uuid));
                                    }
                                    addContact(rs, map.get(uuid));
                                }
                                return map;
                            });

                    addAllTextSections(SectionType.PERSONAL, conn, resumes);
                    addAllTextSections(SectionType.OBJECTIVE, conn, resumes);
                    addAllTextSections(SectionType.ACHIEVEMENT, conn, resumes);
                    addAllTextSections(SectionType.QUALIFICATIONS, conn, resumes);
                    return new ArrayList<>(resumes.values());
                }
        );
    }

    public List<Resume> getAllSortedCombineByCode() {
        return helper.TransactionalExecuteQuery(conn -> {
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
        //check query parameter as table name
        prepareTextSection(r.getSections().get(SectionType.PERSONAL), r, conn, "INSERT INTO personal (resume_uuid, VALUE) VALUES (?,?)");
        prepareTextSection(r.getSections().get(SectionType.OBJECTIVE), r, conn, "INSERT INTO objective (resume_uuid, VALUE) VALUES (?,?)");
        prepareListSection((ListSection) r.getSections().get(SectionType.ACHIEVEMENT), r, conn, "INSERT INTO achievement (resume_uuid, VALUE) VALUES (?,?)");
        prepareListSection((ListSection) r.getSections().get(SectionType.QUALIFICATIONS), r, conn, "INSERT INTO qualifications (resume_uuid, VALUE) VALUES (?,?)");
    }

    private void prepareTextSection(AbstractSection as, Resume r, Connection conn, String query) throws SQLException {
        if (as != null) {
            helper.executePreparedStatement(conn, query,
                    ps -> {
                        ps.setString(1, r.getUuid());
                        ps.setString(2, as.toString());
                        ps.execute();
                        return null;
                    });
        }
    }

    private void prepareListSection(ListSection as, Resume r, Connection conn, String query) throws SQLException {
        if (as != null) {
            String line = String.join("/n", as.getLines());
            helper.executePreparedStatement(conn, query,
                    ps -> {
                        ps.setString(1, r.getUuid());
                        ps.setString(2, line);
                        ps.execute();
                        return null;
                    });
        }
    }

    private void addSection(SectionType st, String uuid, Connection conn, Resume resume) throws SQLException {
        helper.executePreparedStatement(conn,
                "    SELECT * FROM " +
                        st.name() +
                        " WHERE resume_uuid =?", (ps) -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    createTextSection(st, rs, resume);
                    return null;
                });
    }

    private void executePreparedStatementDeleteBySectionType(SectionType type, Resume r, Connection conn) throws SQLException {
        helper.executePreparedStatement(conn,
                "    DELETE FROM " +
                        type.name() +
                        " WHERE resume_uuid=?", (ps) -> {
                    ps.setString(1, r.getUuid());
                    ps.execute();
                    return null;
                }
        );
    }

    private void addAllTextSections(SectionType st, Connection conn, Map<String, Resume> resumes) throws SQLException {
        helper.executePreparedStatement(conn,
                "    SELECT * FROM " +
                        st.name(), (ps) -> {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        createTextSection(st, rs, resumes.get(rs.getString("resume_uuid")));
                    }
                    return null;
                });
    }

    private void createTextSection(SectionType st, ResultSet rs, Resume resume) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            switch (st) {
                case PERSONAL:
                case OBJECTIVE:
                    resume.addSection(st, new TextSection(value));
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    List<String> lines = Arrays.stream(rs.getString("value").split("/n")).collect(Collectors.toList());
                    resume.addSection(st, new ListSection(lines));
                    break;
            }
        }
    }
}
