<%@ page import="com.ushine.webapp.model.Resume" %>
<%@ page import="java.util.List" %>
<%@ page import="com.ushine.webapp.model.ContactType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html: charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Title</title>
</head>
<body>
<div id="main">
    <jsp:include page="fragments/header.jsp"></jsp:include>
    <div id="site_content">
        <section class="content">
            <a href="resume?uuid=${resume.uuid}&action=edit">Add new
            </a>
            <table>
                <tr>
                    <th>Name</th>
                    <th>Email</th>
                    <th colspan="2">
                    </th>
                </tr>
                <c:forEach items="${resumes}" var="resume">
                    <jsp:useBean id="resume" class="com.ushine.webapp.model.Resume"/>
                    <tr>
                        <td>
                            <a href="resume?uuid=${resume.uuid}&action=view">${resume.fullName}
                            </a>
                        </td>
                        <td>${ContactType.EMAIL.toHtml(resume.contacts.get(ContactType.EMAIL))}
                        </td>
                        <td><a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png">
                        </a>
                        </td>
                        <td><a href="resume?uuid=${resume.uuid}&action=delete"><img src="img/delete.png">
                        </a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </section>
    </div>
</div>

</body>
</html>
