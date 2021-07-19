package com.ushine.webapp.storage;

import com.ushine.webapp.exception.StorageException;
import com.ushine.webapp.model.Resume;
import com.ushine.webapp.storage.strategy.SerializeStrategy;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
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
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("IO Error", file.getName(), e);
        }
        rewrite(file, resume);
    }

    @Override
    protected Resume getByKey(File file) {
        try {
            return strategy.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO Error", file.getName(), e);
        }
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
            throw new StorageException("The file hasn't been deleted", file.getName());
        }
    }

    @Override
    protected List<Resume> getAll() {
        List<Resume> list = new ArrayList<>();
        File[] files = getFiles();
        Arrays.stream(files).forEach(f -> list.add(getByKey(f)));
        return list;
    }

    @Override
    public int size() {
        File[] list = getFiles();
        return list.length;
    }

    @Override
    public void clear() {
        File[] files = getFiles();
        Arrays.stream(files).forEach(this::erase);
    }

    private File[] getFiles(){
        File[] files = directory.listFiles();
        if (files == null) throw new StorageException("No files in the storage directory", "no uuid");
        return files;
    }
}
