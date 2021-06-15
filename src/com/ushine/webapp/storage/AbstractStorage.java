package com.ushine.webapp.storage;

import com.ushine.webapp.exception.NotExistStorageException;
import com.ushine.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return getByIndex(index, uuid);
    }

    public void update(Resume r) {
        if (r == null) return;
        int index = getIndex(r.getUuid());
        if (index > -1) {
            rewrite(index, r);
        } else {
            throw new NotExistStorageException(r.getUuid());
        }
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        erase(index, uuid);
    }

    protected abstract int getIndex(String uuid);

    protected abstract Resume getByIndex(int index, String uuid);

    protected abstract void rewrite(int index, Resume resume);

    protected abstract void erase(int index, String uuid);
}
