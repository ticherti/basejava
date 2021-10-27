<%@ page import="com.ushine.webapp.model.Resume" %>
<%@ page import="java.util.List" %>
<%@ page import="com.ushine.webapp.model.ContactType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
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
            <table>
                <tr>
                    <th>Name</th>
                    <th>Email</th>
                </tr>
                <%
                    for (Resume resume : (List<Resume>) request.getAttribute("resumes")) {
                %>
                <tr>
                    <td>
                        <a href="resume?uuid=<%=resume.getUuid()%>"><%=resume.getFullName()%>
                        </a>
                    </td>
                    <td><%=resume.getContacts().get(ContactType.EMAIL)%>
                    </td>
                </tr>
                <%}%>
            </table>
        </section>
    </div>
</div>

</body>
</html>
