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
//        todo Use request.getParameterValues to get parameter map for the parameters with the same name.
//         But how to get them here?
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
            String value = request.getParameter(type.name()).trim();
            LOG.info(type + "" + value);

            if (value.trim().length() != 0) {
                LOG.info("switching");
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        resume.addSection(type, htmlHelperReadTextSection(value));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        resume.addSection(type, htmlHelperReadListSection(value));
                        break;
                }
            } else {
                LOG.info("deleting");
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

//    todo Problems: crush with fast delete
}