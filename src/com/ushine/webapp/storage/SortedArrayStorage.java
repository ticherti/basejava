package com.ushine.webapp.storage;

import com.ushine.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer getSearchKey(String uuid) {
        Resume searchKey = new Resume("Someone", uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    protected void insert(Resume r, int index) {
        index = Math.abs(index) - 1;
        if (size - index > 0) {
            System.arraycopy(storage, index, storage, index + 1, size - index);
        }
        storage[index] = r;
    }

    @Override
    protected void takeOut(int index) {
        int lastEntry = size - 1;
        if (lastEntry - index >= 0)
            System.arraycopy(storage, index + 1, storage, index, lastEntry - index);
    }
}
