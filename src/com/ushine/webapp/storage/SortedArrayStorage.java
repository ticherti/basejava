package com.ushine.webapp.storage;

import com.ushine.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class SortedArrayStorage extends AbstractArrayStorage {

    public void save(Resume r) {
        if (r == null) return;
        String uuid = r.getUuid();
        int index = getIndex(uuid);
        if (index > -1) {
            System.out.println("There is already such resume in the storage. Uuid: " + uuid);
        } else if (size >= STORAGE_LIMIT) {
            System.out.println("The storage is full. The resume hasn't been added. Uuid: " + uuid);
        } else {
            index = Math.abs(index) - 1;
            if (size - index > 0) {
                System.arraycopy(storage, index, storage, index + 1, size - index);
            }
            storage[index] = r;
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
            int lastEntry = size - 1;
            if (lastEntry - index >= 0)
                System.arraycopy(storage, index + 1, storage, index, lastEntry - index);
            storage[lastEntry] = null;
            size--;
            System.out.println("The resume has been deleted. Uuid: " + uuid);
        }
    }

    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}
