package com.ushine.webapp.model;

import java.util.List;

public class Organization {
    private Link placeName;
    private List<Position> positions;

    public Link getPlaceName() {
        return placeName;
    }

    public void setPlaceName(Link placeName) {
        this.placeName = placeName;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
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
}
