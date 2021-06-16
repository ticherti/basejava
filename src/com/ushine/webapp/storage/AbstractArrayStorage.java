package com.ushine.webapp.storage;

import com.ushine.webapp.exception.ExistStorageException;
import com.ushine.webapp.exception.StorageException;
import com.ushine.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10_000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        if (r == null) return;
        String uuid = r.getUuid();
        int index = getIndex(uuid);
        if (index > -1) {
            throw new ExistStorageException(r.getUuid());
        }
        if (size >= STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", r.getUuid());
        }
        insert(r, index);
        size++;
    }

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    public int size() {
        return size;
    }

    protected abstract int getIndex(String uuid);

    protected abstract void insert(Resume r, int index);

    protected abstract void takeOut(int index);

    @Override
    protected Resume getByKey(Object index) {
        return storage[(int) index];
    }

    @Override
    protected void rewrite(Object searchKey, Resume resume) {
        storage[(int) searchKey] = resume;
    }

    @Override
    protected void erase(Object searchKey) {
        takeOut((int) searchKey);
        storage[size - 1] = null;
        size--;
    }
}
