package com.ushine.webapp.util;

import com.ushine.webapp.storage.SqlStorage;

import java.io.*;
import java.util.Properties;

public class Config {
//    comments here are for old postgres for IDEA database. New lines are for heroku
//    protected static final File PROPS = new File(getHomeDir(),"/resumes.properties");
    protected static final String PROPS = "/resumes.properties";

    private static final Config INSTANCE = new Config();

    private Properties props = new Properties();

    private File storageDir;
    private SqlStorage sqlStorage;

    private Config() {
//        try (InputStream is = new FileInputStream(PROPS)) {
        try (InputStream is = Config.class.getResourceAsStream(PROPS)) {
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            sqlStorage = new SqlStorage(props.getProperty("db.url"), props.getProperty("db.user"), props.getProperty("db.password"));
        } catch (IOException e) {
//            throw new IllegalStateException("Invalid config file " + PROPS.getAbsolutePath());
            throw new IllegalStateException("Invalid config file " + PROPS);
        }
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    public File getStorageDir() {
        return storageDir;
    }

    public SqlStorage getSqlStorage() {
        return sqlStorage;
    }

    private static File getHomeDir() {
        String prop = System.getProperty("homeDir");
        System.out.println(prop);
        File homeDir = new File(prop == null ? "." : prop);
        if (!homeDir.isDirectory()){
            throw new IllegalStateException(homeDir + "is not directory. VM options for tomcat should be -DhomeDir=\"" +
                    " here you put absolute way to the basejava directory\"");
        }
        return homeDir;
    }
}
