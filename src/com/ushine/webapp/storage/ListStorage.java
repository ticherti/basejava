package com.ushine.webapp.storage;

import com.ushine.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private final List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected int getIndex(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) return i;
        }
        return -1;
    }

    @Override
    protected boolean isPresent(Resume resume) {
        return storage.contains(resume);
    }

    @Override
    protected void add(Resume resume) {
        storage.add(resume);
    }
    @Override
    protected Resume getByKey(Object index) {
        return storage.get((int) index);
    }



    @Override
    protected void rewrite(Object searchKey, Resume resume) {
        storage.set((int) searchKey, resume);
    }

    @Override
    protected void erase(Object searchKey) {
        storage.remove((int) searchKey);
    }

}
