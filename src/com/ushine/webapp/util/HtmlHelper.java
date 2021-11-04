package com.ushine.webapp.util;

import com.ushine.webapp.model.*;

import java.util.Map;

public class HtmlHelper {
    private static final String NOSECTION = "No such section to show";

    public static String toHtml(SectionType type, AbstractSection section) {
        return section == null ? "" : htmlByType(type, section, true);
    }

    public static String toHtml(Map.Entry<SectionType, AbstractSection> sectionEntry, boolean isForEdit) {
        AbstractSection as = sectionEntry.getValue();
        return as == null ? "" :  htmlByType(sectionEntry.getKey(), as, false);
    }

    public static String getContactPatter(ContactType type){
        switch (type){
            case PHONENUMBER:
                return "[0-9]{5,}";
            case SKYPE:
                return "[a-z][a-z0-9]{4,31}";
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
}

