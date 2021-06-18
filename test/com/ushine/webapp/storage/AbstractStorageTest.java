package com.ushine.webapp.storage;

import com.ushine.webapp.exception.ExistStorageException;
import com.ushine.webapp.exception.NotExistStorageException;
import com.ushine.webapp.model.Resume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
//@RunWith(Suite.class)
//@Suite.SuiteClasses({
//        ArrayStorageTest.class,
//        SortedArrayStorageTest.class,
//        ListStorageTest.class,
//        MapStorageTest.class
//})
public abstract class AbstractStorageTest {

    protected Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";

    protected static final Resume r1 = new Resume("3", UUID_1);
    protected static final Resume r2 = new Resume("2", UUID_2);
    protected static final Resume r3 = new Resume("2", UUID_3);
    private static final Resume r4 = new Resume("4", UUID_4);

    public AbstractStorageTest(Storage storage) {
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
        assertEquals(4, storage.size());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(r1);
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
        List<Resume> expected = new ArrayList<Resume>();
        expected.add(r2);
        expected.add(r3);
        expected.add(r1);
        List<Resume> actual = storage.getAllSorted();
        assertEquals(expected, actual);
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }
}

