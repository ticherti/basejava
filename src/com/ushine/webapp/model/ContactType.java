package com.ushine.webapp.model;

public enum ContactType {
    PHONENUMBER ("Тел.: "),
    SKYPE ("Skype: "),
    EMAIL ("Почта: "),
    LINKEDIN (""),
    GITHUB (""),
    STACKOVERFLOW (""),
    HOMEPAGE ("");

    private String title;

    ContactType(String title) {
        this.title = title;
    }
    public String getTitle(){
        return title;
    }
}
