package com.ushine.webapp.storage;

import com.ushine.webapp.exception.ExistStorageException;
import com.ushine.webapp.exception.NotExistStorageException;
import com.ushine.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    protected abstract SK getSearchKey(String uuid);

    protected abstract boolean isExist(SK searchKey);

    protected abstract void add(Resume resume, SK searchKey);

    protected abstract Resume getByKey(SK searchKey);

    protected abstract void rewrite(SK searchKey, Resume resume);

    protected abstract void erase(SK searchKey);

    protected abstract List<Resume> getAll();

    public void save(Resume resume) {
        SK searchKey = getSearchKey(resume.getUuid());
        if (isExist(searchKey)) {
            LOG.warning("save " + resume + "doesn't exist");
            throw new ExistStorageException(resume.getUuid());
        }
        add(resume, searchKey);
    }

    public Resume get(String uuid) {
//        LOG.info("get " + uuid);
        return getByKey(getPresentKey(uuid));
    }

    public void update(Resume resume) {
//        LOG.info("update " + resume);
        if (resume == null) return;
        rewrite(getPresentKey(resume.getUuid()), resume);
    }

    public void delete(String uuid) {

//        LOG.info("delete " + uuid);
        erase(getPresentKey(uuid));
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumes = getAll();
        resumes.sort(new ComparatorByFullName().thenComparing(new ComparatorByUuid()));
        return resumes;
    }

    protected SK getPresentKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
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
