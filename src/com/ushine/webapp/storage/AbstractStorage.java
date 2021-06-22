package com.ushine.webapp.storage;

import com.ushine.webapp.exception.ExistStorageException;
import com.ushine.webapp.exception.NotExistStorageException;
import com.ushine.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {

    protected abstract Object getSearchKey(String uuid);

    protected abstract boolean isExist(Object searchKey);

    protected abstract void add(Resume resume, Object searchKey);

    protected abstract Resume getByKey(Object searchKey);

    protected abstract void rewrite(Object searchKey, Resume resume);

    protected abstract void erase(Object searchKey);

    protected abstract List<Resume> getAll();


    public void save(Resume resume) {
        Object searchKey = getSearchKey(resume.getUuid());
        if (isExist(searchKey))
            throw new ExistStorageException(resume.getUuid());
        add(resume, searchKey);
    }

    public Resume get(String uuid) {
        return getByKey(getPresentKey(uuid));
    }

    public void update(Resume resume) {
        if (resume == null) return;
        rewrite(getPresentKey(resume.getUuid()), resume);
    }

    public void delete(String uuid) {
        erase(getPresentKey(uuid));
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumes = getAll();
        resumes.sort(new ComparatorByFullName().thenComparing(new ComparatorByUuid()));
        return resumes;
    }

    protected Object getPresentKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (!isExist(searchKey))
            throw new NotExistStorageException(uuid);
        return searchKey;
    }

    static class ComparatorByFullName implements Comparator<Resume> {
        @Override
        public int compare(Resume o1, Resume o2) {
            return o1.getFullName().compareTo(o2.getFullName());
        }
    }

    static class ComparatorByUuid implements Comparator<Resume> {
        @Override
        public int compare(Resume o1, Resume o2) {
            return o1.getUuid().compareTo(o2.getUuid());
        }

    }
}
