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
                <label for="fullName">Name</label>
                <input type="text" id="fullName"
                       name="fullName" size=50 pattern="[А-Яа-яa-zA-Z0-9\s]{2,}"
                       placeholder="full name"
                       value="${resume.fullName}" required>
                <h3>Contacts</h3>
                <c:forEach var="type" items="${ContactType.values()}">
                    <%--                todo I do not understand why we can use type.title here if the field is private--%>
                    <label for="${type}">${type.title}</label>
                    <input type="text" id="${type}"
                           name="${type.name()}" pattern="${HtmlHelper.getContactPatter(type)}"
                           size="50"
                           placeholder="${HtmlHelper.getContactPlaceHolder(type)}"
                           value="${resume.getContact(type)}">
                    </br>
                </c:forEach>
                <c:forEach var="type" items="${SectionType.values()}">
                    <c:choose>
                        <c:when test="${type != SectionType.EDUCATION && type != SectionType.EXPERIENCE}">
                            <h3><label for="${type}">${type.title}</label></h3>
                            <textarea id="${type}"
                                      name="${type.name()}" cols="50"
                                      rows="8">${HtmlHelper.toHtml(type, resume.getSection(type))}</textarea>
                        </c:when>
                        <c:otherwise>
                            <h3>${type.title}</h3>
                            <c:set var="orgCount" value="${0}"/>
                            <c:choose>
                                <c:when test="${type == SectionType.EDUCATION}">
                                    <c:set var="prefix" value="job"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="prefix" value="edu"/>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach var="org" items="${resume.getSection(type).organizations}">
                                <c:set var="orgCount" value="${orgCount + 0}"/>
                                <p><label>Name of an organization
                                    <input type="text" name="${type.name()}"
                                           size="50"
                                           value="${org.placeName}">
                                </label></p>

                                <c:forEach var="position" items="${org.positions}">
                                    <p><label>Start date
                                        <input type="date" name="${prefix}${orgCount}startDate"
                                               value="${position.periodStart}">
                                    </label></p>
                                    <p><label>End date
                                        <input type="date" name="${prefix}${orgCount}endDate"
                                               value="${position.periodFinish}">
                                    </label></p>
                                    <p><label>Position
                                        <input type="text" name="${prefix}${orgCount}position"
                                               size="50"
                                               value="${position.name}">
                                    </label></p>
                                    <c:if test="${type == SectionType.EXPERIENCE}">
                                        <p><label>Description
                                            <input type="text" name="${prefix}${orgCount}description"
                                                   size="50"
                                                   value="${position.description}"></label>
                                        </p>
                                    </c:if>
                                </c:forEach>
                                <p>Add new position</p>
                                <p><label>Enter the start date
                                    <input type="date" name="${prefix}${orgCount}startDate"></label></p>
                                <p><label>Enter the end date
                                    <input type="date" name="${prefix}${orgCount}endDate"></label></p>
                                <p><label>Add a position name
                                    <input type="text" name="${prefix}${orgCount}position" size="50"></label></p>
                                <c:if test="${type == SectionType.EXPERIENCE}">
                                    <p><label>Enter a description
                                        <input type="text" name="${prefix}${orgCount}description" size="50"></label></p>
                                </c:if>
                            </c:forEach>

                            <p><b>New organization</b></p>
                            <p><label>Add name of an organization
                                <input type="text" name="${type.name()}" size="50"></label></p>
                            <p><label>Enter the start date
                                <input type="date" name="startDate"></label></p>
                            <p><label>Enter the end date
                                <input type="date" name="endDate"></label></p>
                            <p><label>Add a position name
                                <input type="text" name="${type.name()}"
                                       size="50"></label></p>
                            <c:if test="${type == SectionType.EXPERIENCE}">
                                <p><label>Enter a description
                                    <input type="text" name="${type.name()}" size="50"></label></p>
                            </c:if>

                            <c:choose>
                                <c:when test="${type != SectionType.EDUCATION}">
                                    <c:set var="jobOrgCount" value="${orgCount}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="eduOrgCount" value="${orgCount}"/>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                <input type="hidden" name="job" value="${orgCount}">
                <input type="hidden" name="edu" value="${orgCount}">
                <button type="submit">Save</button>
                <button type="reset">Cancel</button>
                <button onclick="window.history.back()">Back</button>
            </form>
        </section>
    </div>
</div>
</body>
</html>
