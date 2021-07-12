package com.ushine.webapp.storage;

import com.ushine.webapp.model.Resume;

public class MapResumeStorage extends AbstractMapStorage<Resume> {

    @Override
    protected Resume getSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected boolean isExist(Resume searchKey) {
        return searchKey != null;
    }

    @Override
    protected Resume getByKey(Resume searchKey) {
        return searchKey;
    }

    @Override
    protected void rewrite(Resume searchKey, Resume resume) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected void erase(Resume searchKey) {
        storage.remove(searchKey.getUuid());
    }
}
