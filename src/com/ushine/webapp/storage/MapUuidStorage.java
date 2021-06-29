package com.ushine.webapp.storage;

import com.ushine.webapp.model.Resume;

public class MapUuidStorage extends AbstractMapStorage<String> {

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExist(String searchKey) {
        return storage.containsKey(searchKey);
    }

    @Override
    protected Resume getByKey(String searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected void rewrite(String searchKey, Resume resume) {
        storage.put(searchKey, resume);
    }

    @Override
    protected void erase(String searchKey) {
        storage.remove(searchKey);
    }
}
