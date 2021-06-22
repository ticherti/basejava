package com.ushine.webapp.storage;

import com.ushine.webapp.model.Resume;

import java.util.*;

public class MapUuidStorage extends AbstractMapStorage {
    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return storage.containsKey((String) searchKey);
    }

    @Override
    protected void add(Resume resume, Object index) {
        storage.put(resume.getUuid(), resume);
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

    @Override
    protected List<Resume> getAll() {
        return new ArrayList<>(storage.values());
    }


}
