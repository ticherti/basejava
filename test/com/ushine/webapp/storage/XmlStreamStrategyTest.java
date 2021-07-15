package com.ushine.webapp.storage;

import com.ushine.webapp.storage.strategy.XmlStreamStrategy;

public class XmlStreamStrategyTest extends AbstractStorageTest {
    public XmlStreamStrategyTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new XmlStreamStrategy()));
    }
}