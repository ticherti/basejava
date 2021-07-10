package com.ushine.webapp.storage;

import com.ushine.webapp.exception.StorageException;
import com.ushine.webapp.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public  class AbstractPathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private final RouteStrategy strategy;

    protected AbstractPathStorage(String dir, RouteStrategy strategy) {
        Objects.requireNonNull(dir, "Directory mustn't be null");
        Path obtainedDir = Paths.get(dir);
        if (!Files.isDirectory(obtainedDir) || !Files.isWritable(obtainedDir)) {
            throw new IllegalArgumentException(dir + " is not directory or not readable/writable ");
        }
        directory = Paths.get(dir);
        this.strategy = strategy;
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected void add(Resume resume, Path path) {
        try {
            Files.createFile(path);
            strategy.doWrite(resume, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("IO Error", path.toString(), e);
        }
    }

    @Override
    protected Resume getByKey(Path path) {
        Resume r;
        try {
            r = strategy.doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("IO Error", path.toString(), e);
        }
        return r;
    }

    @Override
    protected void rewrite(Path path, Resume resume) {
        try {
            strategy.doWrite(resume, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("IO Error", path.toString(), e);
        }
    }

    @Override
    protected void erase(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("The Path hasn't been deleted", path.toString());
        }
    }

    @Override
    protected List<Resume> getAll() {
        List<Resume> list = new ArrayList<>();
        try {
            Files.list(directory).forEach(path -> {
                try {
                    list.add(strategy.doRead(new BufferedInputStream(Files.newInputStream(path))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int size() {
        try {
            return (int) Files.list(directory).count();
        } catch (IOException e) {
            throw new StorageException("Directory read error", null, e);
        }
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::erase);
        } catch (IOException e) {
            throw new StorageException("Path delete error", null, e);
        }
    }
}
