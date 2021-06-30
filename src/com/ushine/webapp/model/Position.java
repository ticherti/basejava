package com.ushine.webapp.model;

import java.time.YearMonth;

public class Position {
    private YearMonth periodStart;
    private YearMonth periodFinish;
    private String name;
    private String description = "";

    public YearMonth getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(YearMonth periodStart) {
        this.periodStart = periodStart;
    }

    public YearMonth getPeriodFinish() {
        return periodFinish;
    }

    public void setPeriodFinish(YearMonth periodFinish) {
        this.periodFinish = periodFinish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        Object now = periodFinish;
        if (periodFinish == null){
            now = "Сейчас";
        }
        return periodStart + " - " + now.toString() +
                "\n" + name +
                "\n" + description;
    }
}
