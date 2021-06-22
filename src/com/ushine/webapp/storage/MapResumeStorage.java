package com.ushine.webapp.storage;

import com.ushine.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractMapStorage {

    @Override
    protected Object getSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return searchKey != null;
    }

    @Override
    protected Resume getByKey(Object searchKey) {
        return (Resume) searchKey;
    }

    @Override
    protected void rewrite(Object searchKey, Resume resume) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected void erase(Object searchKey) {
        Resume beingDeleted = (Resume) searchKey;
        storage.remove(beingDeleted.getUuid());
    }
}
