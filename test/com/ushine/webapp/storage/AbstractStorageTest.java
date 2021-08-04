package com.ushine.webapp.storage;

import com.ushine.webapp.ResumeTestData;
import com.ushine.webapp.exception.ExistStorageException;
import com.ushine.webapp.exception.NotExistStorageException;
import com.ushine.webapp.model.Resume;
import com.ushine.webapp.util.Config;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
public abstract class AbstractStorageTest {
    protected static final File STORAGE_DIR = Config.getInstance().getStorageDir();
    protected Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";

    protected static final Resume R_1 = ResumeTestData.getResume("3", UUID_1);
    protected static final Resume R_2 = ResumeTestData.getResume("2", UUID_2);
    protected static final Resume R_3 = ResumeTestData.getResume("2", UUID_3);
    protected static final Resume R_31 = ResumeTestData.getResume("31", UUID_3);
    private static final Resume R_4 = ResumeTestData.getResume("4", UUID_4);

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }


    @Before
    public void setUp() {
        storage.clear();
        storage.save(R_1);
        storage.save(R_2);
        storage.save(R_3);
    }

    @Test
    public void clear() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void save() {
        storage.save(R_4);
        assertEquals(R_4, storage.get(UUID_4));
        assertEquals(4, storage.size());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(R_1);
    }

    @Test
    public void update() {
        storage.update(R_31);
        assertEquals(R_31, storage.get(UUID_3));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(R_4);
    }

    @Test
    public void get() {
        assertEquals(R_3, storage.get(UUID_3));
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
        List<Resume> expected = new ArrayList<Resume>();
        expected.add(R_2);
        expected.add(R_3);
        expected.add(R_1);
        List<Resume> actual = storage.getAllSorted();
        assertEquals(expected, actual);
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }
}

