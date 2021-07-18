package com.ushine.webapp.storage.strategy;

import com.ushine.webapp.model.*;

import java.io.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamStrategy implements SerializeStrategy {
    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            //name and uuid
            dos.writeUTF(resume.getFullName());
            dos.writeUTF(resume.getUuid());

            //contacts
            dos.writeInt(resume.getContacts().size());
            for (Map.Entry<ContactType, Link> entry : resume.getContacts().entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue().getName());
                dos.writeUTF(entry.getValue().getUrl());
            }
            //TO DO insert foreach

            //text section
            writeTextSection(resume, dos, SectionType.OBJECTIVE);
            writeTextSection(resume, dos, SectionType.PERSONAL);

//            list section
            writeListSection(resume, dos, SectionType.ACHIEVEMENT);
            writeListSection(resume, dos, SectionType.QUALIFICATIONS);

//            //organization section
            writeOrgSection(resume, dos, SectionType.EXPERIENCE);
            writeOrgSection(resume, dos, SectionType.EDUCATION);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());

            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                ContactType cType = ContactType.valueOf(read(dis));
                String name = read(dis);
                String url = read(dis);
                resume.addContact(cType, new Link(name, url));
            }

//            text sections
            readTextSection(dis, resume);
            readTextSection(dis, resume);

//            List sections
            readListSection(dis, resume);
            readListSection(dis, resume);

//            //Organization section

            readOrgSection(dis, resume);
            readOrgSection(dis, resume);

            return resume;
        }
    }

    private void writeTextSection(Resume resume, DataOutputStream dos, SectionType type) throws IOException {
        TextSection ts;
        dos.writeUTF(type.name());
        ts = (TextSection) (resume.getSections().get(type));
        dos.writeUTF(ts.getText());
    }

    private void writeListSection(Resume resume, DataOutputStream dos, SectionType type) throws IOException {
        dos.writeUTF(type.name());
        List<String> list = ((ListSection) resume.getSections().get(type)).getLines();
        int size = list.size();
        dos.writeInt(size);
        for (int i = 0; i < size; i++) {
            dos.writeUTF(list.get(i));
        }
    }

    private void writeOrgSection(Resume resume, DataOutputStream dos, SectionType type) throws IOException {
        dos.writeUTF(type.name());
        List<Organization> listOrg = ((OrganizationSection) resume.getSections().get(type)).getOrganizations();
        int size = listOrg.size();
        dos.writeInt(size);
        for (int i = 0; i < size; i++) {
            writeOrganization(dos, listOrg.get(i));
        }
    }

    private void writeOrganization(DataOutputStream dos, Organization o) throws IOException {
        //write link
        dos.writeUTF(o.getPlaceName().getName());
        dos.writeUTF(o.getPlaceName().getUrl());

        List<Organization.Position> positions = o.getPositions();
        int size = positions.size();
        dos.writeInt(size);
        for (int i = 0; i < size; i++) {
            dos.writeUTF(positions.get(i).getPeriodStart().toString());
            dos.writeUTF(positions.get(i).getPeriodFinish().toString());
            dos.writeUTF(positions.get(i).getName());
            write(dos, positions.get(i).getDescription());
        }
    }

    private void write(DataOutputStream dos, String s) throws IOException {
        if (s != null) {
            dos.writeUTF(s);
        } else {
            dos.writeUTF("");
        }
    }

    private void readTextSection(DataInputStream dis, Resume resume) throws IOException {
        SectionType sType = SectionType.valueOf(dis.readUTF());
        resume.addSection(sType, new TextSection(dis.readUTF()));
    }

    private void readListSection(DataInputStream dis, Resume resume) throws IOException {
        SectionType sType = SectionType.valueOf(dis.readUTF());
        List<String> lines = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            lines.add(dis.readUTF());
        }
        resume.addSection(sType, new ListSection(lines));
    }

    private void readOrgSection(DataInputStream dis, Resume resume) throws IOException {
        List<Organization> listOrg;
        SectionType sType = SectionType.valueOf(dis.readUTF());
        listOrg = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {

            listOrg.add(new Organization(dis.readUTF(), dis.readUTF(), readPosition(dis, dis.readInt())));
        }
        resume.addSection(sType, new OrganizationSection(listOrg));
    }


    private List<Organization.Position> readPosition(DataInputStream dis, int size) throws IOException {
        List<Organization.Position> positions = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            positions.add(new Organization.Position(YearMonth.parse(dis.readUTF()), YearMonth.parse(dis.readUTF()), dis.readUTF(), read(dis)));
        }
        return positions;
    }


    private String read(DataInputStream dis) throws IOException {
        String s = dis.readUTF();
        return s.equals("") ? null : s;
    }
}
