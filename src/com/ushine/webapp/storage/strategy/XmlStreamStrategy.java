package com.ushine.webapp.storage.strategy;

import com.ushine.webapp.model.*;
import com.ushine.webapp.util.XmlParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class XmlStreamStrategy implements SerializeStrategy{
    private XmlParser parser;

    public XmlStreamStrategy(){
        parser = new XmlParser(
                Resume.class, Link.class, ListSection.class, Organization.class,
                Organization.Position.class, OrganizationSection.class, TextSection.class
                );
    }

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (Writer w = new OutputStreamWriter(os, StandardCharsets.UTF_8)){
            parser.marshall(resume, w);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)){
            return parser.unmarshall(r);
        }
    }
}
