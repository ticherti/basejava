package com.ushine.webapp.storage;

import com.ushine.webapp.model.Resume;

import static org.junit.Assert.*;

public class MapStorageTest extends AbstractStorageTest {

    public MapStorageTest() {
        super(new MapStorage());
    }

    @Override
    public void getAll() {
        assertEquals(3, storage.getAll().length);
    }
}