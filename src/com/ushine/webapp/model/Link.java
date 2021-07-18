package com.ushine.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Link implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String url;

    public Link() {
    }

    public Link(String name) {
        Objects.requireNonNull(name, "Mustn't be null");
        this.name = name;
    }

    public Link(String name, String url) {
        Objects.requireNonNull(name, "Mustn't be null");
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url == null ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {

        return url == null ? name : name + ' ' + url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return name.equals(link.name) && Objects.equals(url, link.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url);
    }
}
