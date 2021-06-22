package com.ushine.webapp.storage;

import com.ushine.webapp.exception.StorageException;
import com.ushine.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10_000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    protected abstract void insert(Resume r, int index);

    protected abstract void takeOut(int index);

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return (int) searchKey > -1;
    }

    @Override
    protected void add(Resume resume, Object index) {
        if (size >= STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        insert(resume, (Integer) index);
        size++;
    }

    @Override
    protected Resume getByKey(Object index) {
        return storage[(int) index];
    }

    @Override
    protected void rewrite(Object index, Resume resume) {
        storage[(int) index] = resume;
    }

    @Override
    protected void erase(Object index) {
        takeOut((int) index);
        storage[size - 1] = null;
        size--;
    }

    protected List<Resume> getAll() {
        return Arrays.asList(Arrays.copyOfRange(storage, 0, size));
    }
}
