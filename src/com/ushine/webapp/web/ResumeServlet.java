package com.ushine.webapp.web;

import com.ushine.webapp.model.Resume;
import com.ushine.webapp.storage.Storage;
import com.ushine.webapp.util.Config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init() {
        storage = Config.getInstance().getSqlStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("resumes", storage.getAllSorted());
        request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);

        //        request.setCharacterEncoding("UTF-8");
//        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Content-Type", "text/html; charset=UTF-8");
//        response.setContentType("text/html; charset=UTF-8");
//
//        final String UUID = "uuid";
//        Map<String, String[]> parameters = request.getParameterMap();
//        PrintWriter writer = response.getWriter();
//        if (parameters.containsKey(UUID) && !request.getParameter(UUID).equals("")) {
//            printResume(writer, storage.get(request.getParameter(UUID)));
//        } else printAll(writer, storage);
    }

    private void printResume(PrintWriter writer, Resume r) {
        writer.write(String.format("<h1>%s</h1>", r.getFullName()));
        r.getContacts().forEach((contactType, info) -> writer.write(String.format("<p>%s %s</p>", contactType, info)));
        writer.write("</br>");
        r.getSections().forEach((contactType, info) -> {
            writer.write(String.format("<h3>%s</h3>", contactType));
            writer.write(String.format("<p>%s</p>", info));
        });
    }

    private void printAll(PrintWriter writer, Storage storage) {
        writer.write("<table><tr><th>UUID</th><th>Name</th></tr>");
        storage.getAllSorted().forEach(resume -> writer.write(String.format(
                "<tr><td>%s</td><td>  %s</td></tr>", resume.getUuid(), resume.getFullName())));
        writer.write("</table>");
    }
}