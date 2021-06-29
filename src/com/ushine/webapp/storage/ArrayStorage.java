package com.ushine.webapp.storage;

import com.ushine.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void insert(Resume r, int index) {
        storage[size] = r;
    }

    @Override
    protected void takeOut(int index) {
        storage[index] = storage[size - 1];
    }
}
