package com.ushine.webapp.storage;

import com.ushine.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
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
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) return i;
        }
        return -1;
    }

    @Override
    protected boolean isExist(Integer searchKey) {
        return searchKey > -1;
    }

    @Override
    protected void add(Resume resume, Integer Integer) {
        storage.add(resume);
    }

    @Override
    protected Resume getByKey(Integer index) {
        return storage.get(index);
    }

    @Override
    protected void rewrite(Integer index, Resume resume) {
        storage.set(index, resume);
    }

    @Override
    protected void erase(Integer index) {
        storage.remove((int) index);
    }

    @Override
    protected List<Resume> getAll() {
        return new ArrayList<>(storage);
    }
}
