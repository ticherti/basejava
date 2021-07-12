package com.ushine.webapp.storage;

import com.ushine.webapp.storage.strategy.ObjectStreamStrategy;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {

    public ObjectStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new ObjectStreamStrategy()));
    }
}