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

    private static final Resume r1 = new Resume(UUID_1);
    private static final Resume r2 = new Resume(UUID_2);
    private static final Resume r3 = new Resume(UUID_3);
    private static final Resume r4 = new Resume(UUID_4);

    AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(r1);
        storage.save(r2);
        storage.save(r3);
    }

    @Test
    public void clear() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void save() {
        storage.save(r4);
        assertEquals(r4, storage.get(UUID_4));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(r1);
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
        storage.update(r3);
        assertEquals(r3, storage.get(UUID_3));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(r4);
    }

    @Test
    public void get() {
        assertEquals(r3, storage.get(UUID_3));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(UUID_4);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete(UUID_1);
        assertEquals(2, storage.size());
        storage.get(UUID_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(UUID_4);
    }

    @Test
    public void getAll() {
        Resume[] expected = new Resume[]{r1, r2, r3};
        Resume[] actual = storage.getAll();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }
}