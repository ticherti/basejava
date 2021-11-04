package com.ushine.webapp.util;

import com.ushine.webapp.model.AbstractSection;
import com.ushine.webapp.model.ListSection;
import com.ushine.webapp.model.SectionType;
import com.ushine.webapp.model.TextSection;

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

