<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ushine.webapp.model.Resume" %>
<%@ page import="com.ushine.webapp.model.ContactType" %>
<%@ page import="com.ushine.webapp.model.SectionType" %>
<%@ page import="com.ushine.webapp.model.Organization" %>
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
                    <c:choose>
                        <c:when test="${type != SectionType.EDUCATION && type != SectionType.EXPERIENCE}">
                            <dl>
                                <dt><h3>${type.title}</h3></dt>
                                <dd>
                                <textarea name="${type.name()}" cols="50"
                                          rows="8">${HtmlHelper.toHtml(type, resume.getSection(type))}</textarea>
                                </dd>
                            </dl>
                        </c:when>
                        <c:otherwise>
                            <dl>
                                <dt><h3>${type.title}</h3></dt>
                                <c:forEach var="org" items="${resume.getSection(type).organizations}">
                                    <dd>
                                        <p>Name of an organization</p>
                                        <input type="text" name="${type.name()}"
                                               size="50"
                                               value="${org.placeName}">
                                        <c:forEach var="position" items="${org.positions}">
                                            <p>Position</p>
                                            <input type="text" name="${type.name()}"
                                                   size="50"
                                                   value="${position.name}">
                                            <c:if test="${type == SectionType.EXPERIENCE}">
                                                <p>Description</p>
                                                <input type="text" name="${type.name()}"
                                                       size="50"
                                                       value="${position.description}">
                                            </c:if>
                                        </c:forEach>

                                    </dd>
                                </c:forEach>

                                <dd>
                                    <p><b>New organization</b></p>
                                    <p>Add name of an organization</p>
                                    <input type="text" name="${type.name()}"
                                           size="50">
                                    <p>Enter the start date</p>
                                    <input type="date" name="startDate">
                                    <p>Enter the end date</p>
                                    <input type="date" name="endDate">
                                    <p>Add a position name</p>
                                    <input type="text" name="${type.name()}"
                                           size="50">
                                    <c:if test="${type == SectionType.EXPERIENCE}">
                                        <p>Enter a description</p>
                                        <input type="text" name="${type.name()}"
                                               size="50">
                                    </c:if>
                                </dd>
                            </dl>
                        </c:otherwise>
                    </c:choose>
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
