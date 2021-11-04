<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ushine.webapp.model.Resume" %>
<%@ page import="com.ushine.webapp.model.ContactType" %>
<%@ page import="com.ushine.webapp.util.HtmlHelper" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html: charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <%--    TODO Must find out what is the difference between useBean class="" and type=""--%>
    <jsp:useBean id="resume" class="com.ushine.webapp.model.Resume" scope="request"/>
    <title>${resume.fullName}</title>
</head>
<body>
<%--todo check out if i need html util class --%>
<div id="main">
    <jsp:include page="fragments/header.jsp"></jsp:include>
    <div id="site_content">
        <section class="content">
            <h1>${resume.fullName}</h1> <a href="resume?action=edit&uuid=${resume.uuid}">Edit</a>
            <h2>Contacts</h2>
            <p><c:forEach items="${resume.contacts}" var="contactEntry">
            <p>${contactEntry.getKey().toHtml(contactEntry.getValue())}</p>
            </c:forEach>
            </p>
            <p><c:forEach items="${resume.sections}" var="sectionEntry">
            <h2>${sectionEntry.getKey().getTitle()}</h2>
            <p>${HtmlHelper.toHtml(sectionEntry, false)}</p>
            </c:forEach>
            </p>
        </section>
    </div>
</div>
</body>
</html>
