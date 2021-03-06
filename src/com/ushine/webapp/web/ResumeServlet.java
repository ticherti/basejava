package com.ushine.webapp.web;

import com.ushine.webapp.model.*;
import com.ushine.webapp.storage.AbstractStorage;
import com.ushine.webapp.storage.Storage;
import com.ushine.webapp.util.Config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class ResumeServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());
    private Storage storage;

    @Override
    public void init() {
        storage = Config.getInstance().getSqlStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName").replaceAll(
                "[^А-Яа-яa-zA-Z0-9 ]", "");
        Resume r = "".equals(uuid) ? new Resume(fullName) : storage.get(uuid);
        saveContacts(request, r);
        saveSections(request, r);
        if ("".equals(fullName)) {
            request.setAttribute("resume", r);
            request.getRequestDispatcher("/WEB-INF/jsp/edit.jsp")
                    .forward(request, response);
            return;
        }
        if ("".equals(uuid)) {
            storage.save(r);
        } else {
            r.setFullName(fullName);
            storage.update(r);
        }
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
            case "edit":
            case "get":
                r = storage.get(uuid);
                break;
            case "save":
                r = new Resume();
                break;
            default:
                throw new IllegalStateException("The action is incorrect. Action is " + action);
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(action.equals("view") ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
                .forward(request, response);
    }

    private void saveContacts(HttpServletRequest request, Resume r) {
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.addContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }
    }

    private void saveSections(HttpServletRequest request, Resume resume) {
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            LOG.info(type + "" + value);
            if (value != null && value.trim().length() != 0) {
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        resume.addSection(type, htmlHelperReadTextSection(value));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        resume.addSection(type, htmlHelperReadListSection(value));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        OrganizationSection orgSection = htmlHelperReadOrgSection(request, type);
                        if (orgSection == null) {
                            resume.getSections().remove(type);
                        } else {
                            resume.addSection(type, orgSection);
                        }
                        break;
                }
            } else {
                resume.getSections().remove(type);
            }
        }
    }


    private TextSection htmlHelperReadTextSection(String value) {
        return new TextSection(value);
    }

    private ListSection htmlHelperReadListSection(String value) {
//        The regex here to split the lines by \r\n and remove trailing whitespaces
        List<String> list = Arrays.asList(value.replaceAll("</br>", "").split("\\s*\\r\\n\\s*"));
        return new ListSection(list);
    }

    private OrganizationSection htmlHelperReadOrgSection(HttpServletRequest request, SectionType type) {
        List<Organization> organizations = new ArrayList<>();
        String[] orgNames = request.getParameterValues(type.name() + "orgName");
        for (int i = 0; i < orgNames.length; i++) {
            String orgName = orgNames[i];
            if (isPresent(orgName)) {
                List<Organization.Position> positions = getPositions(request.getParameterValues(type.name() + i + "startDate"),
                        request.getParameterValues(type.name() + i + "endDate"),
                        request.getParameterValues(type.name() + i + "position"),
                        request.getParameterValues(type.name() + i + "description"));
                if (positions.size() != 0) {
                    organizations.add(new Organization(new Link(orgName), positions));
                }
            }
        }
        return organizations.size() == 0 ? null : new OrganizationSection(organizations);
    }

    private List<Organization.Position> getPositions(String[] startDates, String[] endDates,
                                                     String[] positionNames, String[] descriptions) {
        List<Organization.Position> positions = new ArrayList<>();
        for (int i = 0; i < positionNames.length; i++) {
            if (isPresent(positionNames[i]) && isPresent(startDates[i])) {
                String description = descriptions == null ? null : descriptions[i];
                Organization.Position position = new Organization.Position(checkedDate(startDates[i]),
                        checkedDate(endDates[i]), positionNames[i],
                        description);
                positions.add(position);
            }
        }
        return positions;
    }

    private boolean isPresent(String line) {
        return line != null && !line.isEmpty();
    }

    private LocalDate checkedDate(String line) {
        return line.isEmpty() ? null : LocalDate.parse(line);
    }

//    todo Problems: crush with fast delete
}