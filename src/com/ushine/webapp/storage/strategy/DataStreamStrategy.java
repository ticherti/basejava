package com.ushine.webapp.storage.strategy;

import com.ushine.webapp.model.*;

import java.io.*;
import java.time.YearMonth;
import java.util.*;

public class DataStreamStrategy implements SerializeStrategy {
    Resume resume;

    public interface Consumer {
        void accept(Object o) throws IOException;
    }

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        this.resume = resume;
        try (DataOutputStream dos = new DataOutputStream(os)) {

            write(dos, resume.getFullName());
            write(dos, resume.getUuid());

            writeWithException(dos, resume.getContacts().entrySet(), (o -> {
                Map.Entry<ContactType, Link> entry = (Map.Entry<ContactType, Link>) o;
                ContactType key = entry.getKey();
                Link value = entry.getValue();
                dos.writeUTF(key.name());
                writeLink(dos, value);
            }));

            writeWithException(dos, resume.getSections().entrySet(), (o -> {
                Map.Entry<SectionType, AbstractSection> entry = (Map.Entry<SectionType, AbstractSection>) o;
                SectionType key = entry.getKey();
                AbstractSection value = entry.getValue();
                write(dos, key.name());
                switch (key) {
                    case OBJECTIVE:
                    case PERSONAL:
                        writeTextSection(dos, value);
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        writeListSection(dos, value);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        writeOrgSection(dos, value);
                }
            }));
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            resume = new Resume(dis.readUTF(), dis.readUTF());

            readWithException(dis, (i) -> {
                ContactType cType = ContactType.valueOf(read(dis));
                resume.addContact(cType, readLink(dis));
            });

            readWithException(dis, (i) -> {
                SectionType st = SectionType.valueOf(dis.readUTF());
                switch (st) {
                    case OBJECTIVE:
                    case PERSONAL:
                        readTextSection(dis, st);
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        readListSection(dis, st);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        readOrgSection(dis, st);
                }
            });
            return resume;
        }
    }

    void writeWithException(DataOutputStream dos, Collection c, Consumer action) throws IOException {
        Objects.requireNonNull(action);
        dos.writeInt(c.size());
        for (Object o : c) {
            action.accept(o);
        }
    }

    private void write(DataOutputStream dos, String s) throws IOException {
        if (s != null) {
            dos.writeUTF(s);
        } else {
            dos.writeUTF("");
        }
    }

    private void writeLink(DataOutputStream dos, Link link) throws IOException {
        write(dos, link.getName());
        write(dos, link.getUrl());
    }

    private void writeTextSection(DataOutputStream dos, AbstractSection as) throws IOException {
        write(dos, ((TextSection) as).getText());
    }

    private void writeListSection(DataOutputStream dos, AbstractSection as) throws IOException {
        Collection<String> list = ((ListSection) as).getLines();
        writeWithException(dos, list, s -> write(dos, (String) s));
    }

    private void writeOrgSection(DataOutputStream dos, AbstractSection as) throws IOException {
        Collection<Organization> list = ((OrganizationSection) as).getOrganizations();
        writeWithException(dos, list, organization -> writeOrganization(dos, (Organization) organization));
    }

    private void writeOrganization(DataOutputStream dos, Organization o) throws IOException {
        writeLink(dos, o.getPlaceName());

        Collection<Organization.Position> list = o.getPositions();
        writeWithException(dos, list, p -> {
            Organization.Position position = (Organization.Position) p;
            write(dos, position.getPeriodStart().toString());
            write(dos, position.getPeriodFinish().toString());
            write(dos, position.getName());
            write(dos, position.getDescription());
        });
    }

    private String read(DataInputStream dis) throws IOException {
        String s = dis.readUTF();
        return s.equals("") ? null : s;
    }

    private Link readLink(DataInputStream dis) throws IOException {
        return new Link(read(dis), read(dis));
    }

    private void readTextSection(DataInputStream dis, SectionType st) throws IOException {
        resume.addSection(st, new TextSection(read(dis)));
    }

    private void readListSection(DataInputStream dis, SectionType st) throws IOException {
        List<String> list = new ArrayList<>();
        readWithException(dis, (i -> list.add(dis.readUTF())));
        resume.addSection(st, new ListSection(list));
    }

    private void readOrgSection(DataInputStream dis, SectionType st) throws IOException {
        List<Organization> list = new ArrayList<>();
        readWithException(dis, (i -> list.add(new Organization(read(dis), read(dis), readPosition(dis)))));
        resume.addSection(st, new OrganizationSection(list));
    }

    private List<Organization.Position> readPosition(DataInputStream dis) throws IOException {
        List<Organization.Position> positions = new ArrayList<>();
        readWithException(dis, (i -> positions.add(new Organization.Position(YearMonth.parse(read(dis)), YearMonth.parse(read(dis)), read(dis), read(dis)))));
        return positions;
    }

    private void readWithException(DataInputStream dis, Consumer action) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            action.accept(i);
        }
    }

}
