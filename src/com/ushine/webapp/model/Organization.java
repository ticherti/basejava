package com.ushine.webapp.model;

import java.util.List;
import java.util.Objects;

public class Organization {
    private final Link placeName;
    private final List<Position> positions;

    public Organization(String name, String url, List<Position> positions) {
        Objects.requireNonNull(positions, "Mustn't be null");
        this.placeName = new Link(name, url);
        this.positions = positions;
    }

    public Link getPlaceName() {
        return placeName;
    }

    public List<Position> getPositions() {
        return positions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(placeName.toString()).append('\n');
        for (Position pos: positions
             ) {
            sb.append(pos).append('\n');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(placeName, that.placeName) && positions.equals(that.positions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeName, positions);
    }
}
