package com.ushine.webapp.storage;

import com.ushine.webapp.storage.strategy.DataStreamStrategy;

public class DataStreamStrategyTest extends AbstractStorageTest {
    public DataStreamStrategyTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new DataStreamStrategy()));
    }
}