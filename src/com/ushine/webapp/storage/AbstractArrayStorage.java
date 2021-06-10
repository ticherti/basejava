package com.ushine.webapp.storage;

import com.ushine.webapp.exception.ExistStorageException;
import com.ushine.webapp.exception.NotExistStorageException;
import com.ushine.webapp.exception.StorageException;
import com.ushine.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage implements Storage {
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
        } else if (size >= STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", r.getUuid());
        } else {
            insert(r, index);
            size++;
            System.out.println("Successfully added the resume. Uuid: " + uuid);
        }
    }

    public void update(Resume resume) {
        if (resume == null) return;
        int index = getIndex(resume.getUuid());
        if (index > -1) {
            storage[index] = resume;
            System.out.println("The resume has been updated. Uuid: " + resume.getUuid());
        } else {
            throw new NotExistStorageException(resume.getUuid());
        }
    }

    public Resume get(String uuid) {
        if (uuid == null) return null;
        int index = getIndex(uuid);
        if (index <= -1) {
            throw new NotExistStorageException(uuid);
        }
        System.out.println("That's the resume. Uuid: " + uuid);
        return storage[index];
    }

    public void delete(String uuid) {
        if (uuid == null) return;
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        } else {
            storage[index] = storage[size - 1];
            takeOut(index);
            size--;
            System.out.println("The resume has been deleted. Uuid: " + uuid);
        }
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
}
