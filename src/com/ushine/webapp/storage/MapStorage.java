package com.ushine.webapp.storage;

import com.ushine.webapp.exception.ExistStorageException;
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
    public void save(Resume r) {
        if (r == null) return;
        if (storage.containsKey(r.getUuid())) {
            throw new ExistStorageException(r.getUuid());
        }
        storage.put(r.getUuid(), r);
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
    protected Resume getByIndex(Object uuid) {
        return storage.get((String) uuid);
    }

    @Override
    protected void rewrite(int index, Resume resume) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected void erase(int index, String uuid) {
        storage.remove(uuid);
    }



}
