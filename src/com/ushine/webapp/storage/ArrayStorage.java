package com.ushine.webapp.storage;

import com.ushine.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    private int firstEmptyCell = 0;

    public void clear() {
        Arrays.fill(storage, 0, firstEmptyCell, null);
        firstEmptyCell = 0;
    }

    public void save(Resume r) {
        if (r == null) return;
        if (firstEmptyCell == storage.length) return;
        if (findResume(r.getUuid()) == -1) {
            storage[firstEmptyCell] = r;
            firstEmptyCell++;
        }
    }

    public Resume get(String uuid) {
        if (uuid == null) return null;
        int position = findResume(uuid);
        if (position > -1) {
            return storage[position];
        }
        return null;
    }

    public void delete(String uuid) {
        if (uuid == null) return;
        int position = findResume(uuid);
        if (position > -1) {
            storage[position] = null;
            int lastEntry = firstEmptyCell - 1;
            if (lastEntry - position >= 0)
                System.arraycopy(storage, position + 1, storage, position, lastEntry - position);
            storage[lastEntry] = null;
            firstEmptyCell--;
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

    public void update(Resume resume) {
        if (resume == null) {
            System.out.println("Not found for the update.");
            return;
        }
        if (findResume(resume.getUuid()) > -1) {
            System.out.println("Resume's been updated.");
        }
    }

    private int findResume(String uuid) {
        for (int i = 0; i < firstEmptyCell; i++) {
            if (storage[i].getUuid() == uuid) {
                return i;
            }
        }
        return -1;
    }
}
