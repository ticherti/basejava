package com.ushine.webapp.storage;

import com.ushine.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10_000];
    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        if (r == null) return;
        String uuid = r.getUuid();
        if (size == storage.length) {
            System.out.println("The storage is full. The resume hasn't been added. Uuid: " + uuid);
            return;
        }
        if (findResume(uuid) == -1) {
            storage[size] = r;
            size++;
            System.out.println("Successfully added the resume. Uuid: " + uuid);
        } else {
            System.out.println("There is already such resume in the storage. Uuid: " + uuid);
        }
    }

    public Resume get(String uuid) {
        if (uuid == null) return null;
        int index = findResume(uuid);
        if (index > -1) {
            System.out.println("That's the resume. Uuid: " + uuid);
            return storage[index];
        }
        System.out.println("No such resume in the storage. Uuid: " + uuid);
        return null;
    }

    public void update(Resume resume) {
        if (resume == null) {
            System.out.println("Not found for the update");
            return;
        }
        if (findResume(resume.getUuid()) > -1) {
            System.out.println("The resume has been updated. Uuid: " + resume.getUuid());
        }
    }

    public void delete(String uuid) {
        if (uuid == null) return;
        int index = findResume(uuid);
        if (index > -1) {
            storage[index] = null;
            int lastEntry = size - 1;
            if (lastEntry - index >= 0)
                System.arraycopy(storage, index + 1, storage, index, lastEntry - index);
            storage[lastEntry] = null;
            size--;
            System.out.println("The resume has been deleted. Uuid: " + uuid);
        }
        System.out.println("There's no such resume in the storage. Uuid: " + uuid);
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        Resume[] allResumes = new Resume[size];
        System.arraycopy(storage, 0, allResumes, 0, size);
        return allResumes;
    }

    public int size() {
        return size;
    }

    private int findResume(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid() == uuid) {
                return i;
            }
        }
        return -1;
    }
}
