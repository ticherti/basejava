package com.ushine.webapp.util;

import com.ushine.webapp.model.*;

import java.util.Map;

public class HtmlHelper {
    private static final String NOSECTION = "No such section to show";

    public static String toHtml(SectionType type, AbstractSection section) {
        return section == null ? "" : htmlByType(type, section, true);
    }

    //todo Need delete isForEdit boolean
    public static String toHtml(Map.Entry<SectionType, AbstractSection> sectionEntry, boolean isForEdit) {
        AbstractSection as = sectionEntry.getValue();
        return as == null ? "" : htmlByType(sectionEntry.getKey(), as, isForEdit);
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
        return os.toString();
    }
}

