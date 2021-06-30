package com.ushine.webapp.model;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume> {

    // Unique identifier
    private final String uuid;
    private String fullName;
    public Map<ContactType, Link> contacts;
    public Map<SectionType, AbstractSection> sections;

    public Resume(String fullName) {
        this(fullName, UUID.randomUUID().toString());
    }

    public Resume(String fullName, String uuid){
        Objects.requireNonNull(fullName, "Full name must not be null");
        Objects.requireNonNull(uuid, "Uuid must not be null");
        this.fullName = fullName;
        this.uuid = uuid;
    }
    
    public String getUuid() {
        return uuid;
    }
    public String getFullName() {
        return fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        if (!Objects.equals(uuid, resume.uuid)) return false;
        return Objects.equals(fullName, resume.fullName);
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return uuid + '(' + fullName + ')';
    }

    @Override
    public int compareTo(Resume o) {
        return uuid.compareTo(o.uuid);
    }
}
