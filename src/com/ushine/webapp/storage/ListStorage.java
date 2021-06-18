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
    protected List<Resume> getAll() {
        return storage;
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
    protected int checkPresent(Resume resume) {
        return storage.contains(resume) ? 0 : -1;
    }

    @Override
    protected void add(Resume resume, int index) {
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

}
