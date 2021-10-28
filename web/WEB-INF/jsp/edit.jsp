<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ushine.webapp.model.Resume" %>
<%@ page import="com.ushine.webapp.model.ContactType" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html: charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" class="com.ushine.webapp.model.Resume" scope="request"/>
    <title>Edit resume ${resume.fullName}</title>
</head>
<body>
<%--todo check out if i need html util class --%>
<div id="main">
    <jsp:include page="fragments/header.jsp"></jsp:include>
    <div id="site_content">
        <section class="content">
            <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
                <input type="hidden" name="uuid" value="${resume.uuid}">
                <dl>
                    <dt>Name</dt>
                    <dd><input type="text" name="fullName" size=50 value="${resume.fullName}"></dd>
                </dl>
                <h3>Contacts</h3>
                <p>
                    <c:forEach var="type" items="${ContactType.values()}">
                <dl>
                    <dt>${type.title}</dt>
                    <dd>
                        <input type="text" name="${type.name()}" size="50" value="${resume.getContact(type)}">
                    </dd>
                </dl>
                </c:forEach>
                </p>
                <button type="submit">Save</button>
                <button onclick="window.history.back()">Cancel</button>

            </form>
            </p>
        </section>
    </div>
</div>
</body>
</html>
