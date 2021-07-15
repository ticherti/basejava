package com.ushine.webapp.storage.strategy;

import com.ushine.webapp.model.*;
import com.ushine.webapp.util.JsonParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonStreamStrategy implements SerializeStrategy{

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (Writer w = new OutputStreamWriter(os, StandardCharsets.UTF_8)){
            JsonParser.write(resume, w);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)){
            return JsonParser.read(r, Resume.class);
        }
    }
}
