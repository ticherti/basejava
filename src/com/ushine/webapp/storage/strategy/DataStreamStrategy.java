package com.ushine.webapp.storage.strategy;

import com.ushine.webapp.model.*;

import java.io.*;
import java.time.YearMonth;
import java.util.*;

public class DataStreamStrategy implements SerializeStrategy {
    Resume resume;

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        this.resume = resume;
        try (DataOutputStream dos = new DataOutputStream(os)) {

            write(dos, resume.getFullName());
            write(dos, resume.getUuid());

            writeWithException(dos, resume.getContacts().entrySet(), (o -> {
                Map.Entry<ContactType, String> entry = (Map.Entry<ContactType, String>) o;
                ContactType key = entry.getKey();
                String value = entry.getValue();
                dos.writeUTF(key.name());
                write(dos, value);
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

            readWithException(dis, () -> resume.addContact(ContactType.valueOf(read(dis)), read(dis)));

            readWithException(dis, () -> {
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

    private <T> void writeWithException (DataOutputStream dos, Collection<T> c, Writer writer) throws IOException {
        Objects.requireNonNull(writer);
        dos.writeInt(c.size());
        for (T t : c) {
            writer.write(t);
        }
    }

    private void write(DataOutputStream dos, String s) throws IOException {
        dos.writeUTF(s != null ? s : "");
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
        write(dos, o.getPlaceName().getName());
        write(dos, o.getPlaceName().getUrl());

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

    private void readTextSection(DataInputStream dis, SectionType st) throws IOException {
        resume.addSection(st, new TextSection(read(dis)));
    }

    private void readListSection(DataInputStream dis, SectionType st) throws IOException {
        List<String> list = new ArrayList<>();
        readWithException(dis, (() -> list.add(dis.readUTF())));
        resume.addSection(st, new ListSection(list));
    }

    private void readOrgSection(DataInputStream dis, SectionType st) throws IOException {
        List<Organization> list = new ArrayList<>();
        readWithException(dis, (() -> list.add(new Organization(read(dis), read(dis), readPosition(dis)))));
        resume.addSection(st, new OrganizationSection(list));
    }

    private List<Organization.Position> readPosition(DataInputStream dis) throws IOException {
        List<Organization.Position> positions = new ArrayList<>();
        readWithException(dis, (() -> positions.add(new Organization.Position(YearMonth.parse(read(dis)), YearMonth.parse(read(dis)), read(dis), read(dis)))));
        return positions;
    }

    private void readWithException(DataInputStream dis, Reader reader) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            reader.read();
        }
    }

    private  interface Writer<T> {
        void write(T t) throws IOException;
    }
    private  interface Reader {
        void read() throws IOException;
    }
}
