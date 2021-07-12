package com.ushine.webapp.storage;

import com.ushine.webapp.exception.StorageException;
import com.ushine.webapp.model.Resume;
import com.ushine.webapp.storage.strategy.SerializeStrategy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private final SerializeStrategy strategy;

    protected PathStorage(String dir, SerializeStrategy strategy) {
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
        } catch (IOException e) {
            throw new StorageException("IO Error", path.toString(), e);
        }
        rewrite(path, resume);
    }

    @Override
    protected Resume getByKey(Path path) {
        try {
            return strategy.doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("IO Error", path.toString(), e);
        }
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
        return getFiles().map(this::getByKey).collect(Collectors.toList());
    }

    @Override
    public int size() {
        return (int) getFiles().count();
    }

    @Override
    public void clear() {
        getFiles().forEach(this::erase);
    }

    private Stream<Path> getFiles() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("Directory read error", null, e);
        }
    }
}
