package com.ushine.webapp.model;

import java.time.YearMonth;
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
    public Organization(Link homepage, List<Position> positions) {
        Objects.requireNonNull(positions, "Mustn't be null");
        this.placeName = homepage;
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

    public static class Position {
        private final YearMonth periodStart;
        private final YearMonth periodFinish;
        private final String name;
        private final String description;

        public Position(YearMonth periodStart, YearMonth periodFinish, String name, String description) {
            Objects.requireNonNull(periodStart, "Mustn't be null");
            Objects.requireNonNull(name, "Mustn't be null");
            this.periodStart = periodStart;
            this.periodFinish = periodFinish;
            this.name = name;
            this.description = description;
        }

        public YearMonth getPeriodStart() {
            return periodStart;
        }

        public YearMonth getPeriodFinish() {
            return periodFinish;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            Object now = periodFinish;
            Object printDescription = description;
            if (periodFinish == null) {
                now = "Сейчас";
            }
            if (printDescription == null){
                printDescription = "";
            }
            return periodStart + " - " + now.toString() +
                    "\n" + name +
                    "\n" + printDescription;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return periodStart.equals(position.periodStart) && Objects.equals(periodFinish, position.periodFinish) && name.equals(position.name) && Objects.equals(description, position.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(periodStart, periodFinish, name, description);
        }
    }
}
