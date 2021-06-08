package com.ushine.webapp.storage;

import com.ushine.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

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

    public void delete(String uuid) {
        if (uuid == null) return;
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.println("There's no such resume in the storage. Uuid: " + uuid);
        } else {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
            System.out.println("The resume has been deleted. Uuid: " + uuid);
        }
    }

    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
