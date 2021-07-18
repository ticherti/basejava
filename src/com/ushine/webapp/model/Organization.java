package com.ushine.webapp.model;

import com.ushine.webapp.util.YearMonthAdapter;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.XmlAccessType;

import java.io.Serializable;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;

    private Link placeName;
    private List<Position> positions;

    public Organization() {
    }

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
        for (Position pos : positions
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


    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Position implements Serializable {
        private static final long serialVersionUID = 1L;
        private final static YearMonth NOW_DATE = YearMonth.parse("3000-01");
        @XmlJavaTypeAdapter(value = YearMonthAdapter.class)
        private YearMonth periodStart;
        @XmlJavaTypeAdapter(value = YearMonthAdapter.class)
        private YearMonth periodFinish;
        private String name;
        private String description;

        public Position() {
        }

        public Position(YearMonth periodStart, YearMonth periodFinish, String name, String description) {
            Objects.requireNonNull(periodStart, "Mustn't be null");
            Objects.requireNonNull(name, "Mustn't be null");
            this.periodStart = periodStart;
            this.periodFinish = periodFinish == null ? NOW_DATE : periodFinish;
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
            if (periodFinish == NOW_DATE) {
                now = "Сейчас";
            }
            if (printDescription == null) {
                printDescription = "";
            }
            return periodStart + " - " + now +
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
