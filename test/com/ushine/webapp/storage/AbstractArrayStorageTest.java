package com.ushine.webapp.storage;

import com.ushine.webapp.exception.StorageException;
import com.ushine.webapp.model.Resume;
import org.junit.Test;

import static org.junit.Assert.*;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
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
}