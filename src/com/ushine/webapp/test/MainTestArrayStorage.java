package com.ushine.webapp.test;

import com.ushine.webapp.model.Resume;
import com.ushine.webapp.storage.AbstractArrayStorage;
import com.ushine.webapp.storage.ArrayStorage;
import com.ushine.webapp.storage.SortedArrayStorage;

/**
 * Test for your com.ushine.webapp.storage.ArrayStorage implementation
 */
public class MainTestArrayStorage {
    static final AbstractArrayStorage ARRAY_STORAGE = new ArrayStorage();

    public static void main(String[] args) {
        Resume r1 = new Resume();
        r1.setUuid("uuid1");
        Resume r2 = new Resume();
        r2.setUuid("uuid2");
        Resume r3 = new Resume();
        r3.setUuid("uuid3");
        Resume r10 = new Resume();
        r10.setUuid("uuid10");
        Resume r6 = new Resume();
        r6.setUuid("uuid6");
        Resume r5 = new Resume();
        r5.setUuid("uuid5");

        ARRAY_STORAGE.save(r1);
        ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.save(r3);
        ARRAY_STORAGE.save(r10);
        ARRAY_STORAGE.save(r6);
        ARRAY_STORAGE.save(r5);

        System.out.println("\nGet r1: " + ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + ARRAY_STORAGE.size());

        System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));

        System.out.println("\nUpdate a resume");
        ARRAY_STORAGE.update(ARRAY_STORAGE.get("uuid2"));

        System.out.println("\nDon't update a resume");
        ARRAY_STORAGE.update(ARRAY_STORAGE.get("dummy"));

        System.out.println("Null resume update");
        ARRAY_STORAGE.update(null);

        printAll();
        ARRAY_STORAGE.delete(r1.getUuid());
        printAll();
        ARRAY_STORAGE.clear();
        printAll();
        System.out.println("Size: " + ARRAY_STORAGE.size());
    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : ARRAY_STORAGE.getAll()) {
            System.out.println(r);
        }
    }
}
