package com.ushine.webapp.storage;

import com.ushine.webapp.exception.ExistStorageException;
import com.ushine.webapp.exception.NotExistStorageException;
import com.ushine.webapp.exception.StorageException;
import com.ushine.webapp.model.Resume;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public abstract class AbstractArrayStorageTest {
    protected Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";

    AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    public void clear() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void save() {
        Resume r = new Resume(UUID_4);
        storage.save(r);
        assertEquals(r, storage.get(UUID_4));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(new Resume(UUID_1));
    }

    @Test (expected = StorageException.class)
    public void saveOverFlowFail() {
        try {
            for (int i = 0; i < 10_000 - 3; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            fail("Too soon to fail!");
        }
        storage.save(new Resume());
    }

    @Test
    public void update() {
        Resume r = new Resume(UUID_3);
        storage.update(r);
        assertEquals(r, storage.get(UUID_3));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(new Resume(UUID_4));
    }

    @Test
    public void get() {
        Resume r = new Resume(UUID_3);
        storage.update(r);
        assertEquals(r, storage.get(UUID_3));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete(UUID_1);
        assertEquals(2, storage.size());
        storage.get(UUID_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete("dummy");
    }

    @Test
    public void getAll() {
        Resume[] expected = new Resume[]{storage.get(UUID_1), storage.get(UUID_2), storage.get(UUID_3)};
        Resume[] resumes = storage.getAll();
        assertArrayEquals(expected, resumes);
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }
}