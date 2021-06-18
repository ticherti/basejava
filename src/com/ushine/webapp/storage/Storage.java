package com.ushine.webapp.storage;

import com.ushine.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;

public interface Storage {
    void clear();

    void save(Resume r);

    void update(Resume r);

    Resume get(String uuid);

    void delete(String uuid);

    List<Resume> getAllSorted();

    int size();
}
