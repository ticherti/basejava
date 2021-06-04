package com.ushine.webapp.storage;

import com.ushine.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    private int firstEmptyCell = 0;

    public void clear() {
        for (int i = 0; i < firstEmptyCell; i++) {
            storage[i] = null;
        }
        firstEmptyCell = 0;
    }

    public void save(Resume r) {
        if (r == null) return;
        if (firstEmptyCell == storage.length) return;
        storage[firstEmptyCell] = r;
        firstEmptyCell++;
    }

    public Resume get(String uuid) {
        if (uuid == null) return null;
        for (int i = 0; i < firstEmptyCell; i++) {
            if (storage[i].getUuid().equals(uuid))
                return storage[i];
        }
        return null;
    }

    public void delete(String uuid) {
        if (uuid == null) return;
        for (int i = 0; i < firstEmptyCell; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                storage[i] = null;
                int lastEntry = firstEmptyCell - 1;
                if (lastEntry - i >= 0) System.arraycopy(storage, i + 1, storage, i, lastEntry - i);
                storage[lastEntry] = null;
                firstEmptyCell--;
                break;
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        Resume[] allResumes = new Resume[firstEmptyCell];
        System.arraycopy(storage, 0, allResumes, 0, firstEmptyCell);
        return allResumes;
    }

    public int size() {
        return firstEmptyCell;
    }
}
