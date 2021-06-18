package com.ushine.webapp.storage;

import com.ushine.webapp.exception.ExistStorageException;
import com.ushine.webapp.exception.NotExistStorageException;
import com.ushine.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    protected abstract int getIndex(String uuid);

    protected abstract int checkPresent(Resume resume);

    protected abstract void add(Resume resume, int searchKey);

    protected abstract Resume getByKey(Object searchKey);

    protected abstract void rewrite(Object searchKey, Resume resume);

    protected abstract void erase(Object searchKey);

    public void save(Resume resume) {
        if (resume == null) return;
        int present = checkPresent(resume);
        if (present > -1) throw new ExistStorageException(resume.getUuid());
        add(resume, present);
    }

    public Resume get(String uuid) {
        return getByKey(getKey(uuid));
    }

    public void update(Resume resume) {
        if (resume == null) return;
        rewrite(getKey(resume.getUuid()), resume);
    }

    public void delete(String uuid) {
        erase(getKey(uuid));
    }

    protected Object getKey(String uuid) {
        int searchKey = getIndex(uuid);
        if (searchKey < 0) throw new NotExistStorageException(uuid);
        return searchKey == Integer.MAX_VALUE ? uuid : searchKey;
    }
}
