package com.ushine.webapp.web;

import com.ushine.webapp.model.ContactType;
import com.ushine.webapp.model.Resume;
import com.ushine.webapp.storage.Storage;
import com.ushine.webapp.util.Config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init() {
        storage = Config.getInstance().getSqlStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume r = storage.get(uuid);
        r.setFullName(fullName);
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.addContact(type, value);
            } else {
//                I do not understand that. So if there was no type in contacts we still do this.
                r.getContacts().remove(type);
            }
        }
//        todo Use request.getParameterValues to get parameter map for the parameters with the same name
        storage.update(r);
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
// todo Find out why we need return here
            return;
        }
        Resume r = null;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
            case "edit":

                break;
            case "get":
                r = storage.get(uuid);
                break;
            case "save":
            default:
                throw new IllegalStateException("The action is incorrect. Action is " + action);
        }
        request.setAttribute("resume", storage.get(uuid));
        request.getRequestDispatcher(action.equals("view") ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
                .forward(request, response);
    }
}