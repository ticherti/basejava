package com.ushine.webapp.storage;

import com.ushine.webapp.model.Resume;

import java.util.List;

public interface Storage {


    void save(Resume r);

    Resume get(String uuid);

    void update(Resume r);

    void delete(String uuid);

    List<Resume> getAllSorted();

    int size();

    void clear();
}
