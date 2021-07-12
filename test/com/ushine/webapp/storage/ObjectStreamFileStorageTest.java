package com.ushine.webapp.storage;

import com.ushine.webapp.storage.strategy.ObjectStreamStrategy;

public class ObjectStreamFileStorageTest extends AbstractStorageTest {

    public ObjectStreamFileStorageTest(){
        super(new FileStorage(STORAGE_DIR, new ObjectStreamStrategy()));
    }
}