package com.ushine.webapp.model;

import java.util.List;

public class OrganizationSection extends AbstractSection {
    private List<Organization> organizations;

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Organization org: organizations
        ) {
            sb.append(org.toString());
        }
        return sb.toString();
    }
}
