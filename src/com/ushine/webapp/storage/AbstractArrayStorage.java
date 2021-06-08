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

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
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

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    public int size() {
        return size;
    }

    protected abstract int getIndex(String uuid);
}
