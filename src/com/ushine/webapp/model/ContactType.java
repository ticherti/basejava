package com.ushine.webapp.model;

public enum ContactType {
    PHONENUMBER("Тел.:"),
    SKYPE("Skype:") {
        @Override
        public String toHtmlNull(String value) {
            return "<a href='skype:" + value + "'>" + value + "</a>";
        }
    },
    EMAIL("Почта:") {
        @Override
        public String toHtmlNull(String value) {
            return "<a href='mailto:" + value + "'>" + value + "</a>";
        }
    },
    LINKEDIN("Linkedin:"),
    GITHUB("GitHub:"),
    STACKOVERFLOW("Stackoverflow:"),
    HOMEPAGE("Домашняя страница:");

    private final String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String toHtml(String value) {
        return value == null ? "" : toHtmlNull(value);
    }

    protected String toHtmlNull(String value) {
        return value == null ? "" : title + " : " + value;
    }
}
