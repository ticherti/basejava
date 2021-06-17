package com.ushine.webapp.storage;

import com.ushine.webapp.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage{
    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.values().toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected int getIndex(String uuid) {
        return storage.containsKey(uuid) ? Integer.MAX_VALUE : -1;
    }

    @Override
    protected int checkPresent(Resume resume) {
        return getIndex(resume.getUuid());
    }

    @Override
    protected void add(Resume resume, int index) {
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



}
