package com.ushine.webapp.storage;

import com.ushine.webapp.model.Resume;

import java.util.*;

public class MapUuidStorage extends AbstractMapStorage {

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return storage.containsKey((String) searchKey);
    }

    @Override
    protected Resume getByKey(Object searchKey) {
        return storage.get((String) searchKey);
    }

    @Override
    protected void rewrite(Object searchKey, Resume resume) {
        storage.put((String) searchKey, resume);
    }

    @Override
    protected void erase(Object searchKey) {
        storage.remove((String) searchKey);
    }
}
