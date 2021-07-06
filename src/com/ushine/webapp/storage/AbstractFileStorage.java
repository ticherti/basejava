package com.ushine.webapp.storage;

import com.ushine.webapp.exception.StorageException;
import com.ushine.webapp.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract  class AbstractFileStorage extends AbstractStorage<File> {
    private File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "Directory mustn't be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    protected abstract void doWrite(Resume resume, File file) throws IOException;
    protected abstract Resume doRead(File file) throws IOException;

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected void add(Resume resume, File file) {
        try {
            file.createNewFile();
            doWrite(resume,file);
        } catch (IOException e) {
            throw  new StorageException("IO Error", file.getName(), e);
        }
    }

    @Override
    protected Resume getByKey(File file) {
        Resume r;
        try {
            r = doRead(file);
        } catch (IOException e) {
            throw  new StorageException("IO Error", file.getName(), e);
        }
        return r;
    }

    @Override
    protected void rewrite(File file, Resume resume) {
        try {
            doWrite(resume,file);
        } catch (IOException e) {
            throw  new StorageException("IO Error", file.getName(), e);
        }
    }

    @Override
    protected void erase(File file) {
        file.delete();
    }

    @Override
    protected List<Resume> getAll() {
        List<Resume> list = new ArrayList<>();
        for (File f: directory.listFiles()
             ) {
            try {
                list.add(doRead(f));
            } catch (IOException e) {
                throw  new StorageException("IO Error", f.getName(), e);
            }
        }
        return list;
    }

    @Override
    public int size() {
        return directory.list().length;
    }

    @Override
    public void clear() {
        for (File f: directory.listFiles()
             ) {
            f.delete();
        }
    }
}
