package com.ushine.webapp.storage;

public class ObjectStreamFileStorageTest extends AbstractStorageTest {

    public ObjectStreamFileStorageTest(){
        super(new FileStorage(STORAGE_DIR, new ObjectStreamStrategy()));
    }
}