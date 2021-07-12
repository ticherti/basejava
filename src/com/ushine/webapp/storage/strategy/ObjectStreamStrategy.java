package com.ushine.webapp.storage.strategy;

import com.ushine.webapp.exception.StorageException;
import com.ushine.webapp.model.Resume;
import java.io.*;

public class ObjectStreamStrategy implements SerializeStrategy {

    @Override
     public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(resume);
        }
    }

    @Override
     public Resume doRead(InputStream is) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(is)){
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Resume read error", null, e);
        }
    }
}
