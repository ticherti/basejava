package com.ushine.webapp.storage;

import com.ushine.webapp.storage.strategy.DataStreamStrategy;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                ArrayStorageTest.class,
                SortedArrayStorageTest.class,
                ListStorageTest.class,
                MapUuidStorageTest.class,
                MapResumeStorageTest.class,
                ObjectStreamFileStorageTest.class,
                ObjectStreamPathStorageTest.class,
                XmlStreamStrategyTest.class,
                JsonStreamStrategyTest.class,
                DataStreamStrategyTest.class
        })

public class AllStorageTest {
}