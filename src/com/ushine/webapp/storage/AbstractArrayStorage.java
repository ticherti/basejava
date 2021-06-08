package com.ushine.webapp.storage;

import com.ushine.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10_000;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public Resume get(String uuid) {
        if (uuid == null) return null;
        int index = getIndex(uuid);
        if (index > -1) {
            System.out.println("That's the resume. Uuid: " + uuid);
            return storage[index];
        }
        System.out.println("No such resume in the storage. Uuid: " + uuid);
        return null;
    }

    public int size() {
        return size;
    }

    protected abstract int getIndex(String uuid);
}
