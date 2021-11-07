<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ushine.webapp.model.Resume" %>
<%@ page import="com.ushine.webapp.model.ContactType" %>
<%@ page import="com.ushine.webapp.model.SectionType" %>
<%@ page import="com.ushine.webapp.util.HtmlHelper" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html: charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" class="com.ushine.webapp.model.Resume" scope="request"/>
    <title>Edit resume ${resume.fullName}</title>
</head>
<body>
<div id="main">
    <jsp:include page="fragments/header.jsp"></jsp:include>
    <div id="site_content">
        <section class="content">
            <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
                <input type="hidden" name="uuid" value="${resume.uuid}">
                <dl>
                    <dt>Name</dt>
                    <dd><input type="text" name="fullName" size=50 pattern="[А-Яа-яa-zA-Z0-9\s]{2,}"
                               placeholder="full name"
                               value="${resume.fullName}" required></dd>
                </dl>
                <h3>Contacts</h3>
                <c:forEach var="type" items="${ContactType.values()}">
                    <dl>
                            <%--                todo I do not understand why we can use type.title here if the field is private--%>
                        <dt>${type.title}</dt>
                        <dd>
                            <input type="text" name="${type.name()}" pattern="${HtmlHelper.getContactPatter(type)}"
                                   size="50"
                                   placeholder="${HtmlHelper.getContactPlaceHolder(type)}"
                                   value="${resume.getContact(type)}">
                        </dd>
                    </dl>
                </c:forEach>
                <c:forEach var="type" items="${SectionType.values()}">
                    <c:if test="${type != SectionType.EDUCATION && type != SectionType.EXPERIENCE}">
                        <dl>
                            <dt>${type.title}</dt>
                            <dd>
                                <textarea name="${type.name()}" cols="50"
                                          rows="8">${HtmlHelper.toHtml(type, resume.getSection(type))}</textarea>
                            </dd>
                        </dl>
                    </c:if>
                </c:forEach>
                <button type="submit">Save</button>
                <button type="reset">Cancel</button>
                <button onclick="window.history.back()">Back</button>
            </form>
        </section>
    </div>
</div>
</body>
</html>
