package com.ushine.webapp.storage;

import com.ushine.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private static final int STORAGE_LIMIT = 10_000;

    private final Resume[] storage = new Resume[STORAGE_LIMIT];
    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        if (r == null) return;
        String uuid = r.getUuid();
        if (getIndex(uuid) > -1) {
            System.out.println("There is already such resume in the storage. Uuid: " + uuid);
        } else if (size >= STORAGE_LIMIT) {
            System.out.println("The storage is full. The resume hasn't been added. Uuid: " + uuid);
        } else {
            storage[size] = r;
            size++;
            System.out.println("Successfully added the resume. Uuid: " + uuid);
        }
    }

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

    public void update(Resume resume) {
        if (resume == null) {
            System.out.println("Not found for the update");
            return;
        }
        int index = getIndex(resume.getUuid());
        if (index > -1) {
            storage[index] = resume;
            System.out.println("The resume has been updated. Uuid: " + resume.getUuid());
        } else System.out.println("No such resume in the storage. Uuid: \" + uuid");
    }

    public void delete(String uuid) {
        if (uuid == null) return;
        int index = getIndex(uuid);
        if (index == -1) {
            System.out.println("There's no such resume in the storage. Uuid: " + uuid);
        } else {
            int lastEntry = size - 1;
            if (lastEntry - index >= 0)
                System.arraycopy(storage, index + 1, storage, index, lastEntry - index);
            storage[lastEntry] = null;
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

    private int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
