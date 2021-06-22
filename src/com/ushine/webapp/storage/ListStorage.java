package com.ushine.webapp.storage;

import com.ushine.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private final List<Resume> storage = new ArrayList<>();

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
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) return i;
        }
        return -1;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return (int) searchKey > -1;
    }

    @Override
    protected void add(Resume resume, Object Object) {
        storage.add(resume);
    }

    @Override
    protected Resume getByKey(Object index) {
        return storage.get((int) index);
    }

    @Override
    protected void rewrite(Object index, Resume resume) {
        storage.set((int) index, resume);
    }

    @Override
    protected void erase(Object index) {
        storage.remove((int) index);
    }

    @Override
    protected List<Resume> getAll() {
        return storage;
    }
}
