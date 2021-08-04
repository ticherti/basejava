package com.ushine.webapp.storage;

import com.ushine.webapp.util.Config;

public class SqlStorageTest extends AbstractStorageTest {

    public SqlStorageTest(){
        super(new SqlStorage(Config.getInstance().getDbUrl(), Config.getInstance().getDbUser(), Config.getInstance().getDbPassword()));
    }
}