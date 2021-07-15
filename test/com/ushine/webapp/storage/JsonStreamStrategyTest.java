package com.ushine.webapp.storage;

import com.ushine.webapp.storage.strategy.JsonStreamStrategy;

public class JsonStreamStrategyTest extends AbstractStorageTest {
    public JsonStreamStrategyTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new JsonStreamStrategy()));
    }
}