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

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init() {
        storage = Config.getInstance().getSqlStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume r = "".equals(uuid) ? new Resume(fullName) : storage.get(uuid);
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.addContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }
//        todo Use request.getParameterValues to get parameter map for the parameters with the same name
         if ("".equals(fullName)){
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
        Resume r = null;
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

//    todo Problems: crush with fast delete
//    todo Problem: entering empty values. Still a problem.
//    todo Problem: strange names. Add matcher
}