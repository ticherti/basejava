package com.ushine.webapp.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OrganizationSection extends AbstractSection {
    private List<Organization> organizations;

    public OrganizationSection() {
    }

    public OrganizationSection(Organization... organizations) {
        Objects.requireNonNull(organizations, "Mustn't be null");
        this.organizations = Arrays.asList(organizations);
    }

    public OrganizationSection(List<Organization> organizations) {
        Objects.requireNonNull(organizations, "Mustn't be null");
        this.organizations = organizations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationSection that = (OrganizationSection) o;
        return organizations.equals(that.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizations);
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Organization org : organizations
        ) {
            sb.append(org.toString());
        }
        return sb.toString();
    }

}
