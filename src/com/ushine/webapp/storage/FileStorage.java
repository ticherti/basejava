package com.ushine.webapp.storage;

import com.ushine.webapp.exception.StorageException;
import com.ushine.webapp.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public  class FileStorage extends AbstractStorage<File>{
    private final File directory;
    private final SerializeStrategy strategy;

    protected FileStorage(File directory, SerializeStrategy strategy) {
        Objects.requireNonNull(directory, "Directory mustn't be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
        this.strategy = strategy;
    }

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
            strategy.doWrite(resume, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO Error", file.getName(), e);
        }
    }

    @Override
    protected Resume getByKey(File file) {
        Resume r;
        try {
            r = strategy.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO Error", file.getName(), e);
        }
        return r;
    }

    @Override
    protected void rewrite(File file, Resume resume) {
        try {
            strategy.doWrite(resume, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO Error", file.getName(), e);
        }
    }

    @Override
    protected void erase(File file) {
        if (!file.delete()) {
            throw  new StorageException( "The file hasn't been deleted", file.getName());
        }
    }

    @Override
    protected List<Resume> getAll() {
        List<Resume> list = new ArrayList<>();
        for (File f : directory.listFiles()
        ) {
            try {
                list.add(strategy.doRead(new BufferedInputStream(new FileInputStream(f))));
            } catch (IOException e) {
                throw new StorageException("IO Error", f.getName(), e);
            }
        }
        return list;
    }

    @Override
    public int size() {
        String[] list = directory.list();
        if (list == null){
            throw new StorageException("Directory read error", null);
        }
        return list.length;
    }

    @Override
    public void clear() {
        for (File f : directory.listFiles()
        ) {
            f.delete();
        }
    }
}