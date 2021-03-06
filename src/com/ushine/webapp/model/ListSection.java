package com.ushine.webapp.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListSection extends AbstractSection {
    private List<String> lines;

    public ListSection() {
    }

    public ListSection(String... lines) {
        Objects.requireNonNull(lines, "Mustn't be null");
        this.lines = Arrays.asList(lines);
    }

    public ListSection(List<String> lines) {
        Objects.requireNonNull(lines, "Mustn't be null");
        this.lines = lines;
    }

    public List<String> getLines() {
        return lines;
    }

    @Override
    public String toString() {
        return String.join("/n", getLines());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSection that = (ListSection) o;
        return lines.equals(that.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lines);
    }
}
