package com.ushine.webapp.storage.strategy;

import com.ushine.webapp.exception.StorageException;
import com.ushine.webapp.model.*;

import java.io.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class DataStreamStrategy implements SerializeStrategy {
    Resume resume;

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        this.resume = resume;
        try (DataOutputStream dos = new DataOutputStream(os)) {

            write(dos, resume.getFullName());
            write(dos, resume.getUuid());

            dos.writeInt(resume.getContacts().size());
            resume.getContacts().forEach((key, value) -> {
                write(dos, key.name());
                writeLink(dos, value);
            });

            dos.writeInt(resume.getSections().size());
            resume.getSections().forEach((key, value) -> {
                AbstractSection as = resume.getSections().get(key);
                Class<? extends AbstractSection> asClass = as.getClass();
                write(dos, asClass.getName());
                write(dos, key.name());
                if (asClass == TextSection.class) {
                    writeTextSection(dos, as);
                } else if (asClass == ListSection.class) {
                    writeListSection(dos, as);
                } else if (asClass == OrganizationSection.class){
                    writeOrgSection(dos, as);
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            resume = new Resume(dis.readUTF(), dis.readUTF());

            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                ContactType cType = ContactType.valueOf(read(dis));
                resume.addContact(cType, readLink(dis));
            }
            size = dis.readInt();
            for (int i = 0; i < size; i++) {
                String abstractSection = dis.readUTF();
                SectionType st = SectionType.valueOf(dis.readUTF());
                if (abstractSection.equals(TextSection.class.getName())) {
                    readTextSection(dis, st);
                } else if (abstractSection.equals(ListSection.class.getName())) {
                    readListSection(dis, st);
                } else {
                    readOrgSection(dis, st);
                }
            }
            return resume;
        }
    }

    private void write(DataOutputStream dos, String s) {
        try {
            if (s != null) {
                dos.writeUTF(s);
            } else {
                dos.writeUTF("");
            }
        } catch (IOException e) {
            throw new StorageException("Data stream write error", e);
        }
    }

    private void writeInt(DataOutputStream dos, List list) {
        try {
            dos.writeInt(list.size());
        } catch (IOException e) {
            throw new StorageException("Data stream write error", e);
        }
    }

    private void writeLink(DataOutputStream dos, Link link) {
        write(dos, link.getName());
        write(dos, link.getUrl());
    }

    private void writeTextSection(DataOutputStream dos, AbstractSection as) {
        write(dos, ((TextSection) as).getText());
    }

    private void writeListSection(DataOutputStream dos, AbstractSection as) {
        List<String> list = ((ListSection) as).getLines();
        writeInt(dos, list);
        list.forEach(s -> write(dos, s));
    }

    private void writeOrgSection(DataOutputStream dos, AbstractSection as) {
        List<Organization> list = ((OrganizationSection) as).getOrganizations();
        writeInt(dos, list);
        list.forEach(organization -> writeOrganization(dos, organization));
    }

    private void writeOrganization(DataOutputStream dos, Organization o) {
        writeLink(dos, o.getPlaceName());

        List<Organization.Position> list = o.getPositions();
        writeInt(dos, list);
        list.forEach(position -> {
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
        resume.addSection(st, new TextSection(dis.readUTF()));
    }

    private void readListSection(DataInputStream dis, SectionType st) throws IOException {
        List<String> list = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            list.add(dis.readUTF());
        }
        resume.addSection(st, new ListSection(list));
    }

    private void readOrgSection(DataInputStream dis, SectionType st) throws IOException {
        List<Organization> list = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            list.add(new Organization(dis.readUTF(), dis.readUTF(), readPosition(dis, dis.readInt())));
        }
        resume.addSection(st, new OrganizationSection(list));
    }

    private List<Organization.Position> readPosition(DataInputStream dis, int size) throws IOException {
        List<Organization.Position> positions = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            positions.add(new Organization.Position(YearMonth.parse(dis.readUTF()), YearMonth.parse(dis.readUTF()), dis.readUTF(), read(dis)));
        }
        return positions;
    }


}
