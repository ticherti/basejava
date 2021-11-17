package com.ushine.webapp.util;

import com.ushine.webapp.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class HtmlHelper {
    private static final String NOSECTION = "No such section to show";
    private static final String OPEN_P = "<p>";
    private static final String CLOSE_P = "</p>";

    public static String toHtml(SectionType type, AbstractSection section) {
        return section == null ? "" : htmlByType(type, section, true);
    }

    //todo Need delete isForEdit boolean
    public static String toHtml(Map.Entry<SectionType, AbstractSection> sectionEntry, boolean isForEdit) {
        AbstractSection as = sectionEntry.getValue();
        return as == null ? "" : htmlByType(sectionEntry.getKey(), as, isForEdit);
    }
    public static String toHtml(Organization org) {

        return org.toString();
    }

    public static String getContactPlaceHolder(ContactType type) {
        switch (type) {
            case PHONENUMBER:
                return "+7(987)6543210";
            case SKYPE:
                return "skype.nickname";
            case EMAIL:
                return "abc@gmail.com";
            default:
                return "";
        }
    }

    public static String getContactPatter(ContactType type) {
        switch (type) {
            case EMAIL:
                return "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";
            default:
                return ".{1,50}";
        }
    }

    private static String htmlByType(SectionType type, AbstractSection as, boolean isForEdit) {
        switch (type) {
            case PERSONAL:
            case OBJECTIVE:
                return getTextHtml((TextSection) as);
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                return isForEdit ? getListHtml((ListSection) as) : toListView((ListSection) as);
            case EXPERIENCE:
            case EDUCATION:
                return isForEdit ? getOrgHtml((OrganizationSection) as) : toOrgView((OrganizationSection) as);
            default:
                return NOSECTION;
        }
    }

    private static String getTextHtml(TextSection ts) {
        return ts.getText();
    }

    private static String getListHtml(ListSection ls) {
        return String.join("\n", ls.getLines());
    }

    private static String toListView(ListSection ls) {
        return String.join("</br>", ls.getLines());
    }

    private static String getOrgHtml(OrganizationSection os) {
        return "";
    }

    private static String toOrgView(OrganizationSection os) {
        List<Organization> list = os.getOrganizations();
        final String OPEN_B = "<b>";
        final String CLOSE_B = "</b>";
        final String OPEN_UL = "<ul>";
        final String CLOSE_UL = "</ul>";
        final String OPEN_LI = "<li>";
        final String CLOSE_LI = "</li>";
        StringBuilder sb = new StringBuilder();
        list.forEach(organization -> {
            sb.append(OPEN_P).append(OPEN_B)
                    .append(organization.getPlaceName())
                    .append(CLOSE_B).append(CLOSE_P)
                    .append(OPEN_P).append(OPEN_UL);
            organization.getPositions().forEach(position -> sb.append(OPEN_LI)
                    .append(positionToView(position))
                    .append(CLOSE_LI));
            sb.append(CLOSE_UL).append(CLOSE_P);
        });
        return sb.toString();
    }
    private static String positionToView(Organization.Position position){
        final String OPEN_I = "<i>";
        final String CLOSE_I = "</i>";
        StringBuilder sb = new StringBuilder();
        LocalDate endDate = position.getPeriodFinish();
        sb.append(OPEN_P)
                .append(position.getPeriodStart())
                .append(" - ")
                .append(endDate.equals(Organization.Position.NOW_DATE) ? "now" : endDate)
                .append(CLOSE_P)
                .append(OPEN_P)
                .append(position.getName())
                .append(CLOSE_P);
        String description = position.getDescription();
        if (description != null && !description.isEmpty()){
            sb.append(OPEN_P).append(OPEN_I)
                    .append(position.getDescription())
                    .append(CLOSE_I).append(CLOSE_P);
        }
        return sb.toString();
    }
}

